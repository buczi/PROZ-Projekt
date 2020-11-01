package Utils;

import java.io.*;

public class CreateMap {
    public static void Create() {
        String str = "";
        try {
            FileWriter fw = new FileWriter("/home/maciek/Desktop/PROZ_PRO/src/map.txt");
            for (int j = 0; j < 5; j++)
                for (int i = 0; i < 5; i++) {
                    String buf = j + ":" + i + "\n" +
                            "Node" + (j * 5 + i) + "\n" +
                            "NN EE SS WW\n" +
                            "12 12 12 12\n" +
                            "5.75\n" +
                            "4.25\n";
                    System.out.println(buf);
                    str += buf;
                }
            fw.write(str);
            fw.close();
        } catch (IOException e) {

        }
    }
}
