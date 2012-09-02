package au.yibing;

import org.apache.commons.cli.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.concurrent.*;

public class NewMaxProfitFinder {

    private static class DataBlock {
        private final byte[] buffer;
        private final int bufLen;

        private DataBlock(byte[] buffer, int bufLen) {
            this.buffer = buffer;
            this.bufLen = bufLen;
        }

        public boolean isEmpty() {
            return bufLen == 0;
        }

        public byte[] getBuffer() {
            return buffer;
        }

        public int getBufLen() {
            return bufLen;
        }
    }

    private static String inputFilePath;
    private static String outputFilePath;
    private static final BlockingQueue<DataBlock> queue = new ArrayBlockingQueue<DataBlock>(1024);
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    private static class Analyzer implements Callable<Integer> {
        private final BlockingQueue<DataBlock> queue;

        private Analyzer(BlockingQueue<DataBlock> queue) {
            this.queue = queue;
        }

        @Override
        public Integer call() throws Exception {
            PriceInfo low = null;
            PriceInfo high = null;
            double maxProfit = 0;
            PriceInfo sellPoint = null;
            PriceInfo buyPoint = null;
            PriceInfo currentPoint;
            while ((currentPoint = getNextPriceInfo()) != null) {
                if (low == null || currentPoint.getPrice() < low.getPrice())
                    low = currentPoint;
                if (high == null || currentPoint.getPrice() > high.getPrice())
                    high = currentPoint;
                double curPossibleMaxProfit = currentPoint.getPrice() - low.getPrice();
                if (curPossibleMaxProfit > maxProfit) {
                    maxProfit = curPossibleMaxProfit;
                    sellPoint = currentPoint;
                    buyPoint = low;
                }
            }

            try {
                writeResult(buyPoint, sellPoint, maxProfit);
            } catch (Exception e) {
                System.err.println("Failed to write result file: " + e);
            }

            return 0;
        }

        private DataBlock curDataBlock;
        private String[] fields = new String[2];
        private int curField = 0; //First line only contains date.
        private int start = 0;
        private int curPos = 0;
        private String remaining = null;
        private double currentPrice = 1;

        private PriceInfo getNextPriceInfo() {
            while ((curDataBlock != null) || (curDataBlock = getNextDataBlock()) != null) {
                for (; curPos < curDataBlock.getBufLen(); curPos++) {
                    byte b = curDataBlock.getBuffer()[curPos];

                    if (b == '\r') {
                        if (start == curPos)
                            start = curPos + 1;
                        continue;
                    }

                    if (b == ',') {
                        fields[curField] = new String(curDataBlock.getBuffer(), start, curPos - start);
                        if (remaining != null) {
                            fields[curField] = remaining + fields[curField];
                            remaining = null;
                        }
                        start = curPos + 1;
                        curField = 1;
                        continue;
                    }

                    if (b == '\n') {
                        fields[curField] = new String(curDataBlock.getBuffer(), start, curPos - start);
                        if (remaining != null) {
                            fields[curField] = remaining + fields[curField];
                            remaining = null;
                        }
                        start = curPos + 1;
                        PriceInfo result = createPriceInfo();
                        curField = 0;
                        fields[0] = null;
                        fields[1] = null;
                        curPos ++;
                        return result;
                    }
                }

                if (start < curPos)
                    remaining = new String(curDataBlock.getBuffer(), start, curPos - start);
                curDataBlock = null;
                start = 0;
                curPos = 0;
            }

            if (remaining != null) {
                remaining = null;
                return createPriceInfo();
            }

            return null;
        }

        private DataBlock getNextDataBlock() {
            DataBlock dataBlock = null;
            while (dataBlock == null) {
                try {
                    dataBlock = queue.take();
//                    System.out.println("Take one data block at " + System.nanoTime()/1000);
                } catch (InterruptedException e) {
                    System.err.println("Warning: get interrupted when trying to read data block from queue");
                }
            }

            if (dataBlock.isEmpty())
                return null;

            return dataBlock;
        }

        private PriceInfo createPriceInfo() {
            double m;
            String date;
            if (fields[1] == null) {
                m = 0;
                date = fields[0];
            } else {
                m = Double.valueOf(fields[0]);
                date = fields[1];
            }

            currentPrice = currentPrice * (m / 100 + 1);
            return new PriceInfo(currentPrice, date);
        }
    }

    public static void main(String[] args) throws Exception {
        long start = System.nanoTime();

        parseArguments(args);

        Future<Integer> future = threadPool.submit(new Analyzer(queue));

        read();

        future.get();//wait for analyzer to complete

        threadPool.shutdown();

        System.out.println("Total time: " + (System.nanoTime() - start) / 1000000 + " milli seconds");
    }

    private static final int BUF_SIZE = 128 * 1024;
    private static void read() throws IOException, InterruptedException {
        FileInputStream f = new FileInputStream(inputFilePath);
        FileChannel ch = f.getChannel();
        ByteBuffer bb = ByteBuffer.allocateDirect(BUF_SIZE);
        int nRead, nGet;
        while ( (nRead=ch.read( bb )) != -1 )
        {
            if ( nRead == 0 )
                continue;
            bb.position( 0 );
            bb.limit( nRead );
            if( bb.hasRemaining() )
            {
                byte[] buffer = new byte[BUF_SIZE];

                nGet = Math.min(bb.remaining( ), BUF_SIZE);
                bb.get( buffer, 0, nGet );
                queue.put(new DataBlock(buffer, nGet));
//                System.out.println(queue.size());
            }
            bb.clear( );
        }
        queue.put(new DataBlock(new byte[0], 0));
    }

    private static void parseArguments(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("input", true, "The input file path");
        options.addOption("output", true, "The output file path");

        CommandLine commandLine = new PosixParser().parse(options, args);

        inputFilePath = commandLine.getOptionValue("input", "price_moves.csv");
        outputFilePath = commandLine.getOptionValue("output", "result.txt");
    }

    private static void writeResult(PriceInfo buyPoint, PriceInfo sellPoint, double profit) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(outputFilePath));
        printWriter.println(buyPoint.getDateTime());
        printWriter.println(sellPoint.getDateTime());
        double profitRatio = profit / buyPoint.getPrice() * 100;
        DecimalFormat df = new DecimalFormat("#.###");
        printWriter.println(df.format(profitRatio));
        printWriter.flush();
        printWriter.close();
    }
}
