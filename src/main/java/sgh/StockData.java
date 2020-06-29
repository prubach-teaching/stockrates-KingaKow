package sgh;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class StockData {

    public static void getAndProcessChange(String stock) throws IOException {
        String filePath = "data_in/" + stock + ".csv";
        File inFile = new File(filePath);
        if (!inFile.exists()) {
            download("https://query1.finance.yahoo.com/v7/finance/download/" + stock +
                            "?period1=1554504399&period2=1586126799&interval=1d&events=history",
                    filePath);
        }

        Scanner scan = new Scanner(inFile);
        String line = scan.nextLine();

        FileWriter dataout = new FileWriter("data_out/" + stock + ".csv");
        dataout.write (line + ", Change" + "\n");

        while (scan.hasNextLine()) {
            line = scan.nextLine();
            String[] values = line.split(",");
            double Open = Double.valueOf(values[1]);
            double Close = Double.valueOf(values[4]);
            double Change = (Close - Open) / Open;
            dataout.write(line + "," + Change * 100 + "\n");
        }
        dataout.close();
    }

    public static void download(String url, String fileName) throws IOException {
        try (InputStream in = URI.create(url).toURL().openStream()) {
            Files.copy(in, Paths.get(fileName));
        }
    }

    public static void main(String[] args) throws IOException {
        String[] stocks = new String[] { "IBM", "MSFT", "GOOG" };
        for (String s : stocks) {
            getAndProcessChange(s);
        }
    }
}
