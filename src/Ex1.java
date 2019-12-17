import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Ex1 {



    public static void main(String[] args) throws IOException {
        buildGraphFromText buildGraphFromText = new buildGraphFromText("input.txt");
        Graph g = buildGraphFromText.build();
        g.getAllResults();

        FileWriter writer = new FileWriter("output.txt");
        for(String str: g.finalReusltOfInput) {
            writer.write(str + System.lineSeparator());
        }
        writer.close();

    }

}


