import java.io.*;
import java.math.BigDecimal;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class buildGraphFromText {


    String fileName;

    private ArrayList<String> lastParagraphAfterParsing2 = new ArrayList<>();
    public ArrayList<String> getLastParagraphAfterParsing2() { return lastParagraphAfterParsing2; }
    public void setLastParagraphAfterParsing2(ArrayList<String> lastParagraphAfterParsing2) { this.lastParagraphAfterParsing2 = lastParagraphAfterParsing2; }

    Graph g = new Graph();
    String[] lines;
    int numOfVertex;

    //constructor that get the file name.
    public buildGraphFromText(String fileName) {
        this.fileName = fileName;
    }

    //main function to build the Graph from the input
    public Graph build() {
        lines = getContext(this.fileName);
        numOfVertex = CheckNumOfVertex();//Check the num of the vertices that wiil be in the graph;
        g = createGraph(numOfVertex);
        return g;
    }


    //Getting the context from the txt.file
    public String[] getContext(String path) {

        InputStream is = null;
        try {
            is = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = null;
        try {
            line = buf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        while (line != null) {
            sb.append(line).append("\n");
            try {
                line = buf.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String fileAsString = sb.toString();
        Pattern p = Pattern.compile("\\n[\\n]+");
        String[] result = p.split(fileAsString);
        return result;
    }

    //Check the num of the vertices that will be in the graph.
    public int CheckNumOfVertex() {
        int numOfVertex = 0;
        String firstCell = lines[0]; //save the first two lines that contain the the vertices.
        String[] first = firstCell.split("\n");
        String[] secondLine = first[1].split(" ", 2);
        String vertices = secondLine[1];
        String[] num = vertices.split(",");
        numOfVertex = num.length;
        return numOfVertex;
    }

    //Build the Graph from the text.
    public Graph createGraph(int numOfVertex) {
        Graph g = new Graph();

        //Getting all vertices name and create new Node and add to the graph
        String[] verticesLine = lines[0].split("\n");
        String[] verticesWithComma = verticesLine[1].split(":");
        String[] vertices = verticesWithComma[1].split(",");
        for (int i = 0; i < vertices.length; i++) {
            Node newNode = new Node(vertices[i].replaceAll(" ", ""));
            g.add(newNode);
        }


        //Handling with adding the info to the Nodes
        for (int i = 1; i < numOfVertex + 1; i++) {
            String current = lines[i];

            //adding the parents and the children of the node

            String[] currentAfterParsing = current.split("\n");
            String[] name = currentAfterParsing[0].split(" ");
            String vertexName = name[1];
            String parentsBeforeSplit = currentAfterParsing[2];
            String[] parents1 = parentsBeforeSplit.split(":", 2);
            parents1[1].replaceAll(" ", "");
            String parents2 = parents1[1].replaceAll(" ", "");
            String[] parents3 = parents2.split(",");
            Node current2 = g.getNodeByName(vertexName);

            for (int j = 0; j < parents3.length; j++) {
                if (parents3[j].length() == 1) {
                    Node parent = g.getNodeByName(parents3[j]);
                    current2.getParents().add(parent);
                    parent.getChildrens().add(current2);
                }
            }
            //Adding the CPT + Values

            String[] nodeContext = current.split("\n");
            String[] values = nodeContext[1].split(":",2);
            String[] valuesWithCoam = values[1].split(",");//VALUES LINE
            //adding values to each node
            for (int j = 0; j < valuesWithCoam.length ; j++) {
                current2.getValues().add(valuesWithCoam[j].replaceAll(" ",""));
            }


            //Parsing each entry
            for(int j = 4 ; j<nodeContext.length ; j++){

                ArrayList<Integer> probIndexes = new ArrayList<>();
                ArrayList<String[]> cptA = new ArrayList<>();
                String entryBefore = nodeContext[j];
                String[] entry = entryBefore.split(",");

                //find where is the sign "=" is
                for (int k = 0; k < entry.length ; k++) {
                    for (int l = 0; l <entry[k].length() ; l++) {
                        if(entry[k].charAt(l) == '='){
                            probIndexes.add(k+1);
                        }
                    }
                }
                int sizeOfEntreyArray = current2.getParents().size();
                String[] start = new String[sizeOfEntreyArray+2];

                //none parents
                if(current2.getParents().size() == 0 ) {
                    cpt cpt = new cpt(current2.getParents().size());
                    String[] firstLine = new String[sizeOfEntreyArray+2];
                    ArrayList<String> valuesCopy = current2.getValues();
                    ArrayList<String> valuesThatSeen = new ArrayList<>();
                    BigDecimal total = new BigDecimal("0.0");
                    firstLine[0] = current2.get_name();
                    firstLine[1] = "P=";
                    for (int k = 0; k <entry.length ; k=k+2) {
                        if(entry[k].contains("=")){
                            String[] entryLine = Arrays.copyOfRange(entry,k,k+2);
                            BigDecimal currentProb = new BigDecimal(entryLine[1]);
                            total = total.add(currentProb);
                            String firstPlace = entryLine[0];
                            firstPlace = firstPlace.replace("=","");
                            entryLine[0] = firstPlace;
                            valuesThatSeen.add(firstPlace);
                            cpt.getCpt().add(entryLine);
                        }
                    }
                    valuesCopy.removeAll(valuesThatSeen);
                    BigDecimal result = new BigDecimal("0.0");
                    BigDecimal one = new BigDecimal("1.0");
                    result = one.subtract(total);
                    //String resultInString = String.valueOf(result);
                    String[] complimentToAdd = {valuesCopy.get(0),result.toString()};
                    cpt.getCpt().add(complimentToAdd);
                    valuesCopy.clear();
                    valuesThatSeen.clear();
                    cptA.add(start);
                    cpt.setFirstline(firstLine);
                    current2.setCpt(cpt);
                }


                //more then one parents
                //Here we need to create the other entry if there more then two values
                //I saved the first two values that in the line
                //and then find who is the value that missing,the calculate the compliment
                //then created new entry and add it to the cpt.

                else if(current2.getParents().size() != 0){
                    ArrayList<String> valuesCopy = new ArrayList<>();
                    valuesCopy.addAll(current2.getValues());
                    ArrayList<String> valuesThatSeen = new ArrayList<>();
                    BigDecimal total = new BigDecimal("0.0");

                    ArrayList<String[]> arr = new ArrayList<>();
                    cpt cpt2 = new cpt(current2.getParents().size());
                    String[] firstLine = new String[sizeOfEntreyArray+2];
                    int runner = 0 ;
                    for(int k = 0 ; k < current2.getParents().size(); k++){
                        firstLine[runner] = current2.getParents().get(k).get_name();
                        runner++;
                    }
                    firstLine[runner] = current2.get_name();
                    firstLine[firstLine.length-1] ="P=" ;
                    cpt2.setFirstline(firstLine);

                    String[] twoVaribels  = Arrays.copyOfRange(entry,0,probIndexes.get(0)-1);

                    runner = 0;
                    String[] entreyToAdd = new String[sizeOfEntreyArray+2];
                    for(int l = 0 ; l < twoVaribels.length; l++){
                        entreyToAdd[runner] = twoVaribels[l];
                        runner++;
                    }
                    ArrayList<String[]> addToTheTwo = new ArrayList<>();
                    for (int a = 1 ; a < entry.length;a++){
                        if(entry[a].contains("=")){


                            String[] toadd = new String[2];
                            toadd[0] = entry[a].replaceAll("=","");
                            toadd[1] = entry[a+1];
                            BigDecimal currentProb = new BigDecimal(toadd[1]);
                            total = total.add(currentProb);
                            addToTheTwo.add(toadd);
                            valuesThatSeen.add(toadd[0]);
                        }
                    }
                    valuesCopy.removeAll(valuesThatSeen);
                    BigDecimal result = new BigDecimal("0.0");
                    BigDecimal one = new BigDecimal("1.0");
                    result = one.subtract(total);
                    String[] complimentToAdd = {valuesCopy.get(0),result.toString()};
                    addToTheTwo.add(complimentToAdd);
                    valuesCopy.clear();
                    valuesThatSeen.clear();
                    for (String[] add : addToTheTwo){
                        String[] completeEntrey = new String[sizeOfEntreyArray+2] ;
                        runner = 0 ;
                        for (int w = 0 ; w < twoVaribels.length; w++){
                            completeEntrey[runner] = twoVaribels[w].replaceAll(" ","");
                            runner++;
                        }
                        for(int u = 0 ; u < add.length ; u++){
                            completeEntrey[runner] = add[u].replaceAll(" ","");
                            runner++;
                        }
                        arr.add(completeEntrey);
                    }
                    current2.getCpt().getCpt().addAll(arr);
                    current2.getCpt().setFirstline(firstLine);
                }
            }
        }
         String lastParagraph = lines[lines.length - 1];
        String[] lastParagraphAfterParsing = lastParagraph.split("\n");
       // g.setLastParagraphAfterParsing2(Arrays.asList(lastParagraphAfterParsing));
        for (int i = 0 ; i < lastParagraphAfterParsing.length;i++){
            g.getLastParagraphAfterParsing2().add(lastParagraphAfterParsing[i]);
        }

        ArrayList<String> p = new ArrayList<>();

        for (String s : lastParagraphAfterParsing) {
            if(s.charAt(0) == 'P'){
                p.add(s);
            }
        }
        g.setCheckPropebilties(p);

        return g;
    }


    //Get the queries from, the input text.
    public ArrayList<ArrayList<String>> getQuries() {
        ArrayList<ArrayList<String>> queries = new ArrayList<>();

        String lastParagraph = lines[lines.length - 1];
        String[] lastParagraphAfterParsing = lastParagraph.split("\n");

        for (int i = 0; i <lastParagraphAfterParsing.length ; i++) {
            g.getLastParagraphAfterParsing2().add(lastParagraphAfterParsing[i]);
        }
        ArrayList<String> q = new ArrayList<String>();
        ArrayList<String> p = new ArrayList<String>();

        for (String s : lastParagraphAfterParsing) {
            // System.out.println(s);
            if (s.charAt(0) != 'P' && s.charAt(0) != 'Q') {
                q.add(s);
            }
            if(s.charAt(0) == 'P'){
                p.add(s);
            }
        }
        for (String s : q) {
            int index = 0;
            while (s.charAt(index) != '|') {
                index++;
            }
            String left = s.substring(0, index);
            String right = s.substring(index + 1, s.length());
            String[] toAdd = new String[2];
            toAdd[0] = left;
            toAdd[1] = right;
            g.getCheckDependcies().add(toAdd);
        }
        return queries;
    }
}

