package application.internal.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class CSVOutput {

    public synchronized static void print(OutputStream os, Object... args) {
        try {
            for (int i = 0; i < args.length; i++) {
                os.write(args[i].toString().getBytes());
                if (i < args.length - 1) {
                    os.write(',');
                }
            }
            os.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized static void printColumnNames(OutputStream os, List<String> columnNames) {
        try {
            for (int i = 0; i < columnNames.size(); i++) {
                os.write(columnNames.get(i).getBytes());
                if (i < columnNames.size() - 1) {
                    os.write(',');
                }
            }
            os.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void printColumnValues(OutputStream os, List<Map<String, Object>> rowList,
            String columnName, boolean addTimestampAtStart) {
        try {
            if (addTimestampAtStart) {
                os.write("TS".getBytes());
                if (rowList.size() > 0) {
                    os.write(',');
                }
            }
            for (int i = 0; i < rowList.size(); i++) {
                os.write(rowList.get(i).get(columnName).toString().getBytes());
                if (i < rowList.size() - 1) {
                    os.write(',');
                }
            }
            os.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void printListMap(OutputStream os, List<Map<String, Object>> rowList,
            List<String> keysOrdered, boolean addTimestamp) {
        try {
            if (addTimestamp) {
                os.write((System.currentTimeMillis() + "").getBytes());
                if (keysOrdered.size() > 0) {
                    os.write(',');
                }
            }

            for (int i = 0; i < rowList.size(); i++) {
                for (int j = 0; j < keysOrdered.size(); j++) {
                    os.write(rowList.get(i).get(keysOrdered.get(j)).toString().getBytes());
                    if (j < keysOrdered.size() - 1) {
                        os.write(',');
                    }
                }
                if (i < rowList.size() - 1) {
                    os.write(',');
                }
            }
            os.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
