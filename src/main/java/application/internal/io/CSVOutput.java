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

    public synchronized static void printColumnValues(OutputStream os, List<Map<String, Object>> rowList,
            String columnName) {
        try {
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
            List<String> keysOrdered) {
        try {
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
