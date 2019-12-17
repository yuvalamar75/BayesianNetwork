//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.util.ArrayList;

public class Node
{
    private static int _counter = 0;
    private int _id;
    private String color = "white";
    private ArrayList<Node> _ni;
    private ArrayList<Node> parents;
    private ArrayList<Node> childrens;
    private String _name;
    private boolean colored;
    private boolean discovered;
    private cpt cpt;
    private ArrayList<String> values;
    private String cameFrom;


    public Node(String s)
    {

        this.set_id(_counter++);
        this.set_ni(new ArrayList());
        this.set_name(s);
        parents = new ArrayList<>();
        childrens = new ArrayList<>();
        colored = false;
        discovered = false;
        color = "white";
        cameFrom ="";
        values = new ArrayList<>();
        this.cpt = new cpt(parents.size());


    }


   /* public Node(Node n){
   *//*     this.id(n.get_id());
        this.set_ni(n.get_ni());
        this.set_name(n.get_name());
        this.parents = n.getParents();
        this.childrens = n.getChildrens();
        this.colored = n.isColored();
        this.discovered = n.discovered;
        this.color = n.getColor();*//*
        this._id = n.get_id();
    }*/

    public static int getCounter()
    {
        return _counter;
    }

    public char get_id()
    {
        return (char)this._id;
    }

    public void set_id(int _id)
    {
        this._id = _id;
    }




    public ArrayList<Node> get_ni()
    {
        return this._ni;
    }

    public void set_ni(ArrayList<Node> _ni)
    {
        this._ni = _ni;
    }


    public void add(Node n)
    {
        boolean did = false;
        //char nid = n.get_id();
            did = true;
            // Character e = nid;*/
            this._ni.add(n);

        //}
    }
    public boolean hasEdge(String des){
        boolean ans = false;
        for (int i = 0; i < this._ni.size(); ++i) {
            if (des.equals(" "+this._ni.get(i)._name));{
                ans = true;
            }
        }
        return ans;
    }


    public int degree() {
        return this._ni.size();
    }

    public String toString() {
        String ans = "Node: " + this._id + ", name, " + this._name + " ,|ni|, " + this._ni.size() ;
        return ans;
    }

    public String get_name() {
        return this._name;
    }

    private void set_name(String name) {
        this._name = name;
    }

    public static void resetCounter(){
        _counter = 0;
    }

    public static void decreaseCounter() {
        --_counter;
    }

    public ArrayList<Node> getParents() { return parents; }
    public ArrayList<Node> getChildrens() { return childrens; }
    public void setColored(boolean colored) { this.colored = colored; }
    public boolean isColored() { return colored; }
    public void setDiscovered(boolean discovered) { this.discovered = discovered; }
    public boolean isDiscovered() { return discovered; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getCameFrom() { return cameFrom; }
    public void setCameFrom(String cameFrom) { this.cameFrom = cameFrom; }
    public ArrayList<String> getValues() { return values; }
    public void setValues(ArrayList<String> values) { this.values = values; }
    public cpt getCpt() { return cpt; }
    public void setCpt(cpt cpt) { this.cpt = cpt; }
}