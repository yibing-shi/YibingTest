package au.yibing;

import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: yibing
 * Date: 8/23/12
 * Time: 10:03 PM
 */
public class Main {
    private static String inputFilePath;
    private static String outputFilePath;
    private static int numberOfThreads;

    public static void main(String[] args) throws Exception {
        parseArguments(args);

        List<Iterator<String>> segments = Splitter.split(inputFilePath, numberOfThreads);

        List<Future<Analytic>> futures = new ArrayList<Future<Analytic>>(segments.size());
        ExecutorService threadPool = Executors.newFixedThreadPool(segments.size());
        for (final Iterator<String> segment : segments) {
            Future<Analytic> future = threadPool.submit(new Callable<Analytic>() {
                @Override
                public Analytic call() throws Exception {
                    return new Analyzer(segment).parse();
                }
            });
            futures.add(future);
        }

        try{
            ArrayList<Analytic> analytics = new ArrayList<Analytic>(futures.size());
            for (Future<Analytic> future : futures) {
                analytics.add(future.get());
            }
            Analytic result = Synthesizer.synthesize(analytics);
            writeResult(result);
        } finally {
            threadPool.shutdown();
        }
    }

    private static void parseArguments(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("input", true, "The input file path");
        options.addOption("output", true, "The output file path");
        options.addOption("threads", true, "The number of threads");

        CommandLine commandLine = new PosixParser().parse(options, args);

        inputFilePath = commandLine.getOptionValue("input", "price_moves.csv");
        outputFilePath = commandLine.getOptionValue("output", "result.txt");
        numberOfThreads = Integer.valueOf(commandLine.getOptionValue("threads", "1"));
    }

    private static void writeResult(Analytic result) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(outputFilePath));
        printWriter.println(result.getMaxProfitLow().getDateTime());
        printWriter.println(result.getMaxProfitHigh().getDateTime());
        BigDecimal profitRatio = result.getMaxProfit().divide(result.getMaxProfitLow().getPrice());
        profitRatio = profitRatio.multiply(BigDecimal.valueOf(100));
        DecimalFormat df = new DecimalFormat("#.###");
        printWriter.println(df.format(profitRatio));
        printWriter.flush();
        printWriter.close();
    }
}
