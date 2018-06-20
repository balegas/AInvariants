package application.internal.io;

import java.io.IOException;
import java.io.OutputStream;

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

}
