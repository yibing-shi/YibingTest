package com.yibing.algorithm.questions;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * column1, column2, column3
 * a, b, c
 * a, b1\,b2, c
 */

public class CSVParser {
  
  private static class CharIterator implements Iterator<Character> {
    private final String content;
    private int offset;

    public CharIterator(String content) {
      this.content = content;
      this.offset = 0;
    }

    @Override
    public boolean hasNext() {
      return offset < content.length();
    }

    @Override
    public Character next() {
      return content.charAt(offset++);
    }
  }

  private enum Mode {
    COLUMN_PARSING {
      @Override
      Mode parse(Mode prevMode, CharIterator iter, StringBuilder columnBuilder, List<String> columns) {
        if (!iter.hasNext()) {
          return LINE_END;
        }

        Mode next;
        char ch = iter.next();
        switch (ch) {
          case ',': {
            next = COLUMN_END;
            break;
          }
          case '\\': {
            next = CHAR_ESCAPE;
            break;
          }
          case '"': {
            next = SEGMENT_ESCAPE;
            break;
          }
          default: {
            columnBuilder.append(ch);
            next = COLUMN_PARSING;
          }
        }
        return next;
      }
    },
    COLUMN_END {
      @Override
      Mode parse(Mode prevMode, CharIterator iter, StringBuilder columnBuilder, List<String> columns) {
        columns.add(columnBuilder.toString());
        columnBuilder.setLength(0);
        return COLUMN_PARSING;
      }
    },
    CHAR_ESCAPE {
      @Override
      Mode parse(Mode prevMode, CharIterator iter, StringBuilder columnBuilder, List<String> columns) {
        if (iter.hasNext()) {
          columnBuilder.append(iter.next());
          return COLUMN_PARSING;
        } else {
          columnBuilder.append('\\');
          return LINE_END;
        }
      }
    },
    SEGMENT_ESCAPE {
      @Override
      Mode parse(Mode prevMode, CharIterator iter, StringBuilder columnBuilder, List<String> columns) {
        while (iter.hasNext()) {
          char ch = iter.next();
          if (ch == '"') {
            return COLUMN_PARSING;
          }
          columnBuilder.append(ch);
        }
        // Having an open double quotes, add the double quotes back
        columnBuilder.insert(0, '"');
        return LINE_END;
      }
    },
    LINE_END {
      @Override
      Mode parse(Mode prevMode, CharIterator iter, StringBuilder columnBuilder, List<String> columns) {
        columns.add(columnBuilder.toString());
        return END;
      }
    },
    END {
      @Override
      Mode parse(Mode prevMode, CharIterator iter, StringBuilder columnBuilder, List<String> columns) {
        return null;
      }
    };

    abstract Mode parse(Mode prevMode, CharIterator iter, StringBuilder columnBuilder, List<String> columns);
  }

  public List<List<String>> parse(BufferedReader reader) throws IOException {
    String line = reader.readLine();
    List<List<String>> result = new ArrayList<>();
    while (line != null) {
      result.add(parseLine(line));
      line = reader.readLine();
    }
    return result;
  }

  private List<String> parseLine(String line) {
    Mode mode = Mode.COLUMN_PARSING;
    StringBuilder columnBuilder = new StringBuilder();
    List<String> columns = new ArrayList<>();
    CharIterator iter = new CharIterator(line);
    while (mode != Mode.END) {
      mode = mode.parse(mode, iter, columnBuilder, columns);
    }
    return columns;
  }
}
