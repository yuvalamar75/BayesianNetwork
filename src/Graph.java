import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.SQLOutput;
import java.util.*;

import static java.math.RoundingMode.HALF_UP;
import static java.util.Arrays.*;

public class Graph {

    ArrayList<String> finalReusltOfInput = new ArrayList<>();
    static int numOfmultply = 0 ;
    static int numOfSum = 0;

    public List<String> getLastParagraphAfterParsing2() { return lastParagraphAfterParsing2; }

    public void setLastParagraphAfterParsing2(List<String> lastParagraphAfterParsing2) {
        lastParagraphAfterParsing2 = lastParagraphAfterParsing2;
    }

    private  List<String> lastParagraphAfterParsing2 = new ArrayList<>();


    private ArrayList<Node> _nodes;
    private int _edge_count;
    private ArrayList<String[]> checkDependcies;
    private ArrayList<String> checkPropebilties;
    private PriorityQueue<cpt> pqCpt ;


    ArrayList<String[]> resultOf ;
    ArrayList<String> hidden ;
    ArrayList<String[]> evidance ;



    public Graph() {
        _nodes = new ArrayList();
        _edge_count = 0;
        checkDependcies = new ArrayList<>();
        checkPropebilties = new ArrayList<>();
        resultOf = new ArrayList<String[]>();
        hidden = new ArrayList<String>();
        evidance = new ArrayList<String[]>();
    }

    public boolean add(Node d) {

        boolean ans = false;
        d.set_id(_nodes.size());
        this._nodes.add(d);
        ans = true;


        return ans;
    }
    public int size() {
        return this._nodes.size();
    }
    public String toString() {
        String ans = "";
        ans = ans + this.size() + "\n" + this._edge_count + "\n";
        for (int i = 0; i < this.size(); ++i) {
            Node cr = (Node) this._nodes.get(i);
            ans = ans + cr + "\n";
        }
        return ans;
    }
    public int getNodeIndexByName(String s) {
        int ans = -1;
        for (int i = 0; ans == -1 && i < this.size(); ++i) {
            Node cr = (Node) this._nodes.get(i);
            String name = cr.get_name();
            if (s.equals(name)) {
                ans = i;
            }
        }
        return ans;
    }
    public Node getNodeByName(String s) {
        Node ans = null;
        int ind = this.getNodeIndexByName(s);
        if (ind != -1) {
            ans = this._nodes.get(ind);
        }
        return ans;
    }
    public ArrayList<Node> get_nodes() { return _nodes; }
    public void prepareForTheAlgorithem(String[] dependcies){

        String start ="";
        String end = "";
        ArrayList<String> depend = new ArrayList<>();
        String[] startAndEnd = dependcies[0].split("-");
        start = startAndEnd[0];
        end = startAndEnd[1];
        for(int i = 0 ; i < dependcies[1].length() ; i++){
            if(dependcies[1].charAt(i)>64 && dependcies[1].charAt(i)<91){
                depend.add(dependcies[1].charAt(i)+"");
            }
        }
        String result = bayesBallAlgorithm(start,end,depend);
        finalReusltOfInput.add(result);
        //System.out.println(result);
    }

    public void getAllResults(){
        for (String line : lastParagraphAfterParsing2) {
            if (line.charAt(0) != 'P' && line.charAt(0) != 'Q') {
                int index = 0;
                while (line.charAt(index) != '|') {
                    index++;
                }
                String left = line.substring(0, index);
                String right = line.substring(index + 1, line.length());
                String[] toAdd = new String[2];
                toAdd[0] = left;
                toAdd[1] = right;
                prepareForTheAlgorithem(toAdd);

            }
            if(line.charAt(0) == 'P'){
                caluclatePropebilty(line);
            }
        }
    }

    public String bayesBallAlgorithm(String start , String end , ArrayList<String> colored){

        for (Node n :this.get_nodes()) {
            n.setColor("white");
            n.setDiscovered(false);
            n.setColored(false);
        }
        boolean areIndependence = true;
        Queue<Node> queue = new LinkedList<>();

        //Sign all the decencies that given
        if(colored.size()!=0) {
            for (int i = 0; i < colored.size(); i++) {
                Node nodeToColor = this.getNodeByName(colored.get(i));
                nodeToColor.setColored(true);
            }
        }
        //store the start and end Node for checking.
        Node startNode = this.getNodeByName(start);
        Node endNode = this.getNodeByName(end);

        for (Node c : startNode.getChildrens()) {
            c.setCameFrom("P");
            queue.add(c);
        }
        for (Node p : startNode.getParents()) {
            p.setCameFrom("C");
            queue.add(p);
        }

        //Starting the BFS algorithm with some improvements.
        while(!queue.isEmpty()){
            Node v = queue.poll();
            if(v.get_name().equals(endNode.get_name())) areIndependence = false;

            if(!v.isDiscovered()) {
                if (!v.isColored() && v.getCameFrom().equals("C")) {

                    for (Node c : v.getChildrens()) {
                        c.setCameFrom("P");
                        queue.add(c);
                    }
                    for (Node p : v.getParents()) {
                        p.setCameFrom("C");
                        queue.add(p);
                    }
                }
                else if(!v.isColored()  && v.getCameFrom().equals("P")){

                    for (Node c : v.getChildrens()) {
                        c.setCameFrom("P");
                        c.setDiscovered(false);
                        queue.add(c);

                    }
                }
                else if(v.isColored() && v.getCameFrom().equals("P")){

                    for (Node p: v.getParents()) {
                        p.setCameFrom("C");
                        p.setDiscovered(false);
                        queue.add(p);
                    }
                }
            }
            v.setDiscovered(true);
        }
        if(areIndependence) return "yes";
        else return "no";
    }
    public String caluclatePropebilty(String s) {
        numOfSum = 0;
        numOfmultply = 0;
        //Getting all the information from the query
        int indexForLeft = 0;
        int indexForRight = 0;
        while (s.charAt(indexForLeft) != '|') {
            indexForLeft++;
        }
        while (s.charAt(indexForRight) != ')') {
            indexForRight++;
        }
        String left = s.substring(2, indexForLeft);
        String middle = s.substring(indexForLeft + 1, indexForRight);
        String right = s.substring(indexForRight + 2, s.length());


        String[] left2 = left.split(",");
        for (int i = 0; i < left2.length; i++) {
            String[] current = left2[i].split("=");
            resultOf.add(current);
        }

        String[] middle2 = middle.split(",");
        for (int i = 0; i < middle2.length; i++) {
            String[] current = middle2[i].split("=");
            evidance.add(current);
        }
        String[] right2 = right.split("-");
        for (int i = 0; i < right2.length; i++) {
            String[] current = right2[i].split("-");
            for (int j = 0; j < current.length; j++) {
                hidden.add(current[j]);
            }
        }

        String result = joinAlgorithm(this.getResultOf(),this.getEvidance(),this.getHidden());
        finalReusltOfInput.add(result);
        hidden.clear();
        resultOf.clear();
        evidance.clear();
        this.getHidden().clear();
        this.getResultOf().clear();
        this.getEvidance().clear();

        return result;

    }


    public String joinAlgorithm(ArrayList<String[]> resultOf, ArrayList<String[]> evidance, ArrayList<String> hidden ) {
        ArrayList<String> relevantNodes = getRelevantNodes();

        for (int i = 0; i <hidden.size() ; i++) {
            if(!relevantNodes.contains(hidden.get(i))){
                hidden.remove(i);
                i--;
            }
        }

        ArrayList<cpt> copyCpt = getCopyOfCpt(this.get_nodes(),relevantNodes);

        String finalResult = checkIfDirectResult(copyCpt);


/*
        System.out.println(Arrays.toString(hidden.toArray())+"the hidden");
*/



        for ( String[] e : evidance) {
/*
            System.out.println("THE EVIDANCE = " + e[0]);
*/
            for( cpt cpt: copyCpt ){
                if(asList(cpt.firstline).contains(e[0])){
                    String nodeName = e[0];
                    String value = e[1];
                    int index = 0;
                    for (int j = 0; j < cpt.firstline.length ; j++) {
                        if(cpt.firstline[j].equals(nodeName)){
                            index = j;
                        }
                    }
                    for (int j = 0; j <cpt.getCpt().size() ; j++) {
                        if(!cpt.getCpt().get(j)[index].equals(value)){
                            cpt.getCpt().remove(j);
                            j--;
                        }
                    }
                    //remove the the entry that contains
                    for (int j = 0; j <cpt.getCpt().size() ; j++) {
                        List<String> current = new ArrayList<String>(Arrays.asList(cpt.getCpt().get(j)));
                        current.remove(index);
                        current.toArray(cpt.getCpt().get(j));
                    }
                    //remove the index column from the firstLine

                    String[] newFirstLine = new String[cpt.getFirstline().length-1];
                    int runner = 0 ;
                    for (int i = 0; i <cpt.getFirstline().length ; i++) {
                        if(i != index){
                            newFirstLine[runner] = cpt.getFirstline()[i];
                            runner++;

                        }
                    }
                    newFirstLine[newFirstLine.length-1] = "P=";
                    cpt.setFirstline(newFirstLine);
                }
            }
        }

        for (int i = 0; i <copyCpt.size() ; i++) {
            if(copyCpt.get(i).getFirstline().length == 1){
                copyCpt.remove(i);
                i--;
            }
        }

        if(!finalResult.isEmpty()) {
            BigDecimal result1 = new BigDecimal(finalResult);
            String result = result1.setScale(5,HALF_UP).toString()+","+ numOfSum +","+ numOfmultply;
            return result;
        }

        else {

            for (int i = 0; i < hidden.size(); i++) {
                pqCpt = new PriorityQueue<>();
                String currentHidden = hidden.get(i);
                for (int h = 0 ; h<copyCpt.size() ; h++) {
                    for(int j = 0 ; j < copyCpt.get(h).firstline.length-1 ; j++){

                        if(copyCpt.get(h).firstline[j].equals(currentHidden)){
                            pqCpt.add(copyCpt.get(h));
                            copyCpt.remove(h);
                            h--;
                            break;
                        }
                    }
                }
                while(pqCpt.size()>1){
                    cpt cpt1 = pqCpt.poll();
                    cpt cpt2 = pqCpt.poll();
                    cpt cptToAdd = join(cpt1,cpt2);

                    pqCpt.add(cptToAdd);
                }
                cpt catBeforeElimination = pqCpt.poll();
                cpt cptAfterElimination = elimination(catBeforeElimination,currentHidden);


                copyCpt.add(cptAfterElimination);
            }
            PriorityQueue<cpt> pqAfterElimination = new PriorityQueue<>();
            for (cpt cpt :copyCpt) {
                pqAfterElimination.add(cpt);
            }
            while(pqAfterElimination.size() > 1){
                cpt cpt1 = pqAfterElimination.poll();
                cpt cpt2 = pqAfterElimination.poll();
                cpt cptToAdd = join(cpt1,cpt2);
                pqAfterElimination.add(cptToAdd);

            }

           cpt cptFinal = pqAfterElimination.poll();
           // System.out.println(Arrays.toString(cptFinal.firstline));
            for (String last[] : cptFinal.getCpt()) {
            //    System.out.println(Arrays.toString(last));
            }

            BigDecimal mone = new BigDecimal("0");
            BigDecimal mechane = new BigDecimal("0");
            String resultOf2 = resultOf.get(0)[0];
            String valueOf = resultOf.get(0)[1];
            for (String[] s : cptFinal.getCpt()) {
                if(s[getIndex(cptFinal.getFirstline(),resultOf2)].equals(valueOf)){
                    BigDecimal current = new BigDecimal(s[getIndex(cptFinal.getFirstline(),"P=")]);
                    mone = mone.add(current);
                    numOfSum++;
                }
                else{
                    BigDecimal down = new BigDecimal(s[getIndex(cptFinal.getFirstline(),"P=")]);
                    mechane = mechane.add(down);
                }
            }

            mechane = mechane.add(mone);
            //TODO CHANGE THIS LINE
            MathContext canDivde = new MathContext(5, HALF_UP);
            BigDecimal result3 = new BigDecimal("0");
            result3 = mone.divide(mechane,canDivde);
            String result = result3.setScale(5,HALF_UP).toString()+","+ numOfSum +","+ numOfmultply;
            return result;
        }



         /*   System.out.println(numOfmultply + " multyply");
            System.out.println(numOfSum + " sum");*/
           /* System.out.println(mone + " / " + mechane);
            System.out.println(numOfmultply);
            System.out.println(cpt.getNumOfsum());*/


    }
    private String checkIfDirectResult(ArrayList<cpt> copyCpt) {
        String answer = "";
        ArrayList<cpt> containsTheResult = new ArrayList<>();
        for (int i = 0; i <copyCpt.size() ; i++) {
            for (String[] resultOf : resultOf){
                if(copyCpt.get(i).firstline[copyCpt.get(i).firstline.length-2].equals(resultOf[0])){
                    containsTheResult.add(copyCpt.get(i));
                }
            }
        }
        boolean isFound = false;
        int indexOfStrightAnswer = -1;
        for (int i = 0; i <containsTheResult.size(); i++) {
            for (String[] e: evidance) {
                for(int j = 0 ; j < containsTheResult.get(i).firstline.length-2;j++){
                    if(e[0].equals(containsTheResult.get(i).firstline[j])){
                        isFound = true;
                        break;
                    }
                    else {
                        isFound = false;
                    }
                }
            }
            indexOfStrightAnswer = i;
        }
        cpt theTableContainResult = containsTheResult.get(indexOfStrightAnswer);
        for (String[] entry : theTableContainResult.getCpt()) {
            boolean theCorrectLine = false;
            String valueOfQuery = resultOf.get(0)[1];
            if(entry[entry.length-2].equals(valueOfQuery)) {
                for (String[] e : evidance) {
                    String name = e[0];
                    String value = e[1];
                    if (getIndex(theTableContainResult.firstline, name) != -1) {
                        if (entry[getIndex(theTableContainResult.firstline, name)].equals(value)) {
                            theCorrectLine = true;
                        }
                    } else {
                        theCorrectLine = false;
                    }
                }
            }
            if(theCorrectLine){
                answer = entry[entry.length-1];
              //  System.out.println("The answer is "+ answer);
                break;
            }

        }
        return answer;

    }
    private ArrayList<String> getRelevantNodes() {
        ArrayList<Node> whoToCheck = new ArrayList<>();
        for (String[] s : resultOf) {
            whoToCheck.add(getNodeByName(s[0]));
        }
        for (String[] s : evidance) {
            whoToCheck.add(getNodeByName(s[0]));
        }
        ArrayList<String> relevent = new ArrayList<>();

        Queue<Node> queue = new LinkedList<>();

        for (Node n : whoToCheck) {
            if (!relevent.contains(n.get_name())) {
                queue.add(n);
                relevent.add(n.get_name());
                while (!queue.isEmpty()) {
                    Node current = queue.poll();
                    for (Node p : current.getParents()) {
                        if(!relevent.contains(p.get_name())){
                            (queue).add(p);
                            relevent.add(p.get_name());
                        }
                    }
                }
            }
        }

        return relevent;
    }
    public cpt join(cpt cpt1 , cpt cpt2) {

        String[] newFirstLine = CreateNewFirstLine(cpt1.firstline,cpt2.getFirstline());
        cpt newCpt = joinCpt(newFirstLine,cpt1,cpt2);
        newCpt.setFirstline(newFirstLine);

        return newCpt;
    }
    private ArrayList<cpt> getCopyOfCpt(ArrayList<Node> cpts,ArrayList<String> relevent) {
        ArrayList<cpt> copyCpt = new ArrayList<>();

        for (Node n: this.get_nodes()) {
            if (relevent.contains(n.get_name())) {
                cpt newCpt = new cpt(n.getCpt().firstline.length);
                newCpt.setFirstline(n.getCpt().getFirstline());
                for (String[] s : n.getCpt().getCpt()) {
                    String[] newEntry = new String[s.length];
                    System.arraycopy(s, 0, newEntry, 0, s.length);
                    newCpt.getCpt().add(newEntry);
                }
                copyCpt.add(newCpt);
            }
        }
        return copyCpt;
    }
    public static String[] CreateNewFirstLine(String[] firstline1, String[] firstline2) {
        int sizeOfFirstLine1 = firstline1.length;
        int sizeOfFirstLine2 = firstline2.length;
        String[] newFirstLine = new String[1];

        if (sizeOfFirstLine1 > sizeOfFirstLine2) {

            newFirstLine = combineFirstLine(firstline1, firstline2, "X");

        } else if (sizeOfFirstLine2 > sizeOfFirstLine1) {

            newFirstLine = combineFirstLine(firstline1, firstline2, "Y");

        } else {
            newFirstLine = combineFirstLine(firstline1, firstline2, "Y");

        }
      //  System.out.println(Arrays.toString(newFirstLine)+" the new first line");
        return newFirstLine;
    }
    private static String[] combineFirstLine(String[] firstLine1, String[] firstLine2, String s) {

        ArrayList<String> common = new ArrayList<>();
        common.addAll(Arrays.asList(firstLine1).subList(0, firstLine1.length - 1));
        for (int i = 0; i < firstLine2.length; i++) {
            if (!common.contains(firstLine2[i]) && !firstLine2[i].equals("P=")) {
                common.add(firstLine2[i]);
            }
        }
        String[] newFirstLine = new String[common.size() + 1];
        newFirstLine[newFirstLine.length - 1] = "P=";
        int runner = 0;
        for (String cuurent : common) {
            newFirstLine[runner] = cuurent;
            runner++;
        }
        return newFirstLine;
    }
    //get 2 cpt table and join them
    public static cpt joinCpt(String[] newFirstLine,cpt cpt1,cpt cpt2) {

        cpt newCpt = new cpt(newFirstLine.length);
        ArrayList<String> commonNames = getCommonVaribles(cpt1.getFirstline(),cpt2.getFirstline());
        ArrayList<String[]> cptTable1 = cpt1.getCpt();
        ArrayList<String[]> cptTable2 = cpt2.getCpt();

        for (int i = 0; i <cptTable1.size() ; i++) {
            for (int j = 0; j <cptTable2.size() ; j++) {
                boolean sameValue = false;
                for (String common : commonNames) {
                    if(cptTable1.get(i)[getIndex(cpt1.firstline,common)].equals(cptTable2.get(j)[getIndex(cpt2.getFirstline(),common)])){
                        sameValue = true;
                    }
                    else sameValue = false;
                }
                if(sameValue == true){
                    String[] newLine = new String[newFirstLine.length];
                    for (int k = 0; k < newLine.length-1 ; k++) {
                        String current = newFirstLine[k];
                        int index = getIndex(cpt1.getFirstline(),current);
                        if(index != -1){
                            newLine[k] = cpt1.getCpt().get(i)[index];
                        }
                        else{
                            int index2 = getIndex(cpt2.getFirstline(),current);
                            newLine[k] = cpt2.getCpt().get(j)[index2];
                        }
                    }
                    BigDecimal cpt1P = new BigDecimal(cpt1.getCpt().get(i)[cpt1.getFirstline().length-1]);
                    BigDecimal cpt2P = new BigDecimal(cpt2.getCpt().get(j)[cpt2.getFirstline().length-1]);
                    BigDecimal result = cpt1P.multiply(cpt2P);
                    numOfmultply++;


                    newLine[newLine.length-1] = result.toString();
                    newCpt.getCpt().add(newLine);
                    newCpt.setFirstline(newFirstLine);



                }
            }
        }
        return newCpt;
    }
    //Check the common names in the first line
    private static ArrayList<String> getCommonVaribles(String[] firstline1, String[] firstline2) {
        ArrayList<String> commonNames = new ArrayList<>();
        for (int i = 0; i <firstline1.length-1 ; i++) {
            String currentFirstLine1 = firstline1[i];
            for (int j = 0; j <firstline2.length-1 ; j++) {
                if(firstline2[j].equals(currentFirstLine1)){
                    commonNames.add(currentFirstLine1);
                }
            }
        }

        return commonNames;
    }
    public static int getIndex(String[] firstline,String s ){
        int index = -1;
        for (int i = 0; i < firstline.length ; i++) {
            if(firstline[i].equals(s)) index = i;
        }
        return index;
    }
    public  cpt elimination(cpt cptBeforElimination,String variable) {
        //change the firstLine of the cpt
        String[] newFirstLine = new String[cptBeforElimination.getFirstline().length-1];
        int runner = 0;
        for (int i = 0; i < cptBeforElimination.getFirstline().length; i++) {
            if(!cptBeforElimination.getFirstline()[i].equals(variable)){
                newFirstLine[runner] = cptBeforElimination.getFirstline()[i];
                runner++;
            }
        }
        cpt cptAfterElimination = new cpt(newFirstLine.length);
        cptAfterElimination.setFirstline(newFirstLine);

        ArrayList<Integer> sumOf = getWhoToSum(cptBeforElimination.getFirstline(),variable);
        ArrayList<Integer> linesThatEdded = new ArrayList<>();

        for (int i  = 0 ; i <cptBeforElimination.getCpt().size() ; i++){
            ArrayList<String[]> toCheckIfEquals = new ArrayList<String[]>();
            boolean equalsLines = true;
            BigDecimal sum = new BigDecimal(cptBeforElimination.getCpt().get(i)[cptBeforElimination.getFirstline().length-1]);
            if(!linesThatEdded.contains(i)) {
                linesThatEdded.add(i);
                //add all the variables that we should equles in the other lines.
                for (int index : sumOf) {
                    String[] current = new String[2];
                    current[0] = Integer.toString(index);
                    current[1] = cptBeforElimination.getCpt().get(i)[index];
                    toCheckIfEquals.add(current);
                }

                for (int j = 1; j <cptBeforElimination.getCpt().size() ; j++) {
                    if(!linesThatEdded.contains(j) && i != j){
                        equalsLines = true;
                        for (String[] values : toCheckIfEquals) {
                            if(!cptBeforElimination.getCpt().get(j)[Integer.parseInt(values[0])].equals(values[1])) equalsLines = false;
                        }
                        if(equalsLines){
                            linesThatEdded.add(j);

                            BigDecimal propToAdd = new BigDecimal(cptBeforElimination.getCpt().get(j)[cptBeforElimination.getFirstline().length-1]);
                            sum = sum.add(propToAdd);
                            numOfSum++;


                        }

                    }
                }
                String[] newEntry = new String[newFirstLine.length];
                int runner2 = 0;
                for (String[] toAdd: toCheckIfEquals){
                    newEntry[runner2] = toAdd[1];
                    runner2++;
                }
                newEntry[newEntry.length-1] = sum.toString();
                cptAfterElimination.getCpt().add(newEntry);
            }
        }
        return cptAfterElimination;
    }
    private  ArrayList<Integer> getWhoToSum(String[] firstLine, String varible) {
        ArrayList<Integer> whoToSuom = new ArrayList<>();
        for (int i = 0; i < firstLine.length-1 ; i++) {
            if(!firstLine[i].equals(varible)){
                String needToBeAdded = firstLine[i];
                int index = getIndex(firstLine,needToBeAdded);
                whoToSuom.add(index);
            }
        }
        return whoToSuom;
    }
    public ArrayList<String[]> getCheckDependcies() { return checkDependcies; }
    public ArrayList<String> getCheckPropebilties() { return this.checkPropebilties; }
    public void setCheckDependcies(ArrayList<String[]> checkDependcies) { this.checkDependcies = checkDependcies; }
    public void setCheckPropebilties(ArrayList<String> checkPropebilties) {
        this.checkPropebilties.addAll(checkPropebilties);
    }
    public void print(){
        for(Node n : this.get_nodes()){
            System.out.println("NAME: " + n.get_name());
            System.out.println("THE CPT ARE :");
            System.out.println("THE FIRST LINE :");
            System.out.println(Arrays.toString(n.getCpt().getFirstline()));
            for(String[] arr : n.getCpt().getCpt()){
                System.out.println(Arrays.toString(arr));
            }
            System.out.println();
        }
    }
    public ArrayList<String[]> getResultOf() { return resultOf; }
    public ArrayList<String> getHidden() { return hidden; }
    public ArrayList<String[]> getEvidance() { return evidance; }
    public void setResultOf(ArrayList<String[]> resultOf) {
        this.resultOf.addAll(resultOf);
    }
    public void setHidden(ArrayList<String> hidden) {
        this.hidden.addAll(hidden);
    }
    public void setEvidance(ArrayList<String[]> evidance) {
        this.evidance.addAll(evidance);
    }

}
