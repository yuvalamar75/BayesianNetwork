import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public class cpt implements Comparable<cpt> {

    String[] firstline;
    ArrayList<String[]> cpt;

    public static int getNumOfsum() {
        return numOfsum;
    }

    private static int numOfsum = 0;


    public cpt(int size) {
        firstline = new String[size + 2];
        this.cpt = new ArrayList<>();
    }

    /*public static String[] CreateNewFirstLine(String[] firstline1, String[] firstline2) {
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
    public static cpt joinCpt(String[] newFirstLine,cpt cpt1,cpt cpt2,int numOfmultply) {

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

       *//* System.out.println("----------------");
        System.out.println(Arrays.toString(newCpt.firstline));
        for (String[] s: newCpt.getCpt()) {
            System.out.println(Arrays.toString(s));
        }
        System.out.println("--------------");*//*

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

    public static cpt elimination(cpt cptBeforElimination,String variable) {

        numOfsum = 0;
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
                            numOfsum++;
                            sum = sum.add(propToAdd);


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

    private static ArrayList<Integer> getWhoToSum(String[] firstLine, String varible) {
        ArrayList<Integer> whoToSuom = new ArrayList<>();
        for (int i = 0; i < firstLine.length-1 ; i++) {
            if(!firstLine[i].equals(varible)){
                String needToBeAdded = firstLine[i];
                int index = getIndex(firstLine,needToBeAdded);
                whoToSuom.add(index);
            }
        }
        return whoToSuom;
    }*/

    public ArrayList<String[]> getCpt() { return cpt; }
    public void setCpt(ArrayList<String[]> cpt) {
        this.cpt.addAll(cpt); }
    public String[] getFirstline() { return firstline; }
    public void setFirstline(String[] firstline) { this.firstline = firstline; }


    @Override
    public int compareTo(cpt o) {
        return ((Integer)this.getCpt().size()).compareTo(o.getCpt().size());
    }
}
