package au.yibing;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yibing
 * Date: 8/23/12
 * Time: 10:18 PM
 */
public class Splitter {

    private static class SegmentReader implements Iterator<String> {
        private final RandomAccessFile file;
        private final long startPos;
        private final long endPos;
        private String next;

        private SegmentReader(String filePath, long startPos, long endPos) throws IOException {
            this.file = new RandomAccessFile(filePath, "r");
            this.startPos = startPos;
            this.endPos = endPos;
            file.seek(this.startPos);
            this.next = null;
        }

        @Override
        public boolean hasNext() {
            StringBuilder resultBuilder = new StringBuilder();
            try{
                if (file.getFilePointer() >= endPos)
                    return false;

                //skip the possible leading newline characters
                char ch = (char)file.readByte();
                while (Splitter.isNewlineChars(ch))
                    ch = (char)file.readByte();

                do {
                    resultBuilder.append(ch);
                    ch = (char)file.readByte();
                } while(!Splitter.isNewlineChars(ch));
            } catch (EOFException e) {
                // has reached file end, ignore
            } catch (IOException e) {
                throw new RuntimeException("Failed to read file.", e);
            }

            if (resultBuilder.length() == 0) {
                return false;
            }

            next = resultBuilder.toString();
            return true;
        }

        @Override
        public String next() {
            return next;
        }

        @Override
        public void remove() {
            throw new RuntimeException("Unsupported operator!");
        }
    }

    public static List<Iterator<String>> split(String filePath, int numberOfSegments) throws IOException {
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        List<Iterator<String>> result = new LinkedList<Iterator<String>>();
        if (file.length() <= numberOfSegments) {
            result.add(new SegmentReader(filePath, 0, file.length()));
            return result;
        }

        long segmentLength = file.length() / numberOfSegments;
        long startPos = 0;
        for (int i = 0; i < numberOfSegments; i++) {
            long endPos = findNextSplitPoint(file, startPos + segmentLength);
            if (endPos > startPos) {
                result.add(new SegmentReader(filePath, startPos, endPos));
                startPos = endPos;
            }
        }
        return result;
    }

    private static long findNextSplitPoint(RandomAccessFile file, long pos) throws IOException {
        if (pos >= file.length())
            return file.length();

        file.seek(pos);
        do {
            try {
                char ch = (char)file.readByte();
                if (isNewlineChars(ch))
                    break;
            } catch (EOFException e) {
                break;
            }
        } while (file.getFilePointer() < file.length());
        return file.getFilePointer();
    }

    static boolean isNewlineChars(char ch) {
        return ch == '\r' || ch =='\n';
    }
}
