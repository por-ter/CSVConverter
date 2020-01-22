package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Converter {

    public static ArrayList csvReader(String csvPath) {

        File csvFile = new File(csvPath);

        BufferedReader csvReader = null;
        ArrayList arrListHolder;

        String row = "";
        String lineBuilder = "";
        String cellHolder = "";

        arrListHolder = new ArrayList();

        try {
            csvReader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "ISO-8859-1"));

            while ((row = csvReader.readLine()) != null) {
                String data[] = row.split(";");
                lineBuilder = "";
                for (int i = 0; i < data.length; i++) {
                    if (data[i].contains("\"")) {
                        cellHolder = data[i];
                        cellHolder = cellHolder.replaceAll("\"", "");
                        cellHolder = "\"" + cellHolder + "\",";
                    } else {
                        cellHolder = "\"" + data[i] + "\",";
                    }
                    lineBuilder = lineBuilder + cellHolder;

                }
                if (lineBuilder.endsWith(",")) {
                    lineBuilder = lineBuilder.replaceAll(",$", "");
                }

                arrListHolder.add(lineBuilder + "\n");
            }

//            return arrListHolder;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return arrListHolder;
    }

    public static void csvToTextConverter(String csvPath, File textSavePath, ArrayList csvAsStringHolder) {        
        
        try {
            Writer writer = null;
            writer = new OutputStreamWriter(new FileOutputStream(textSavePath + ".txt"), StandardCharsets.UTF_8);

            for (int cou = 0; cou < csvAsStringHolder.size(); cou++) {
                writer.write(csvAsStringHolder.get(cou).toString());
            }

            writer.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
