import java.util.ArrayList;

public class entries {


    /*private ArrayList<Node> parents ;
    private ArrayList<String> propebilties;*/
    int sizeParents = 0 ;
    ArrayList<String[]> entry;


    public entries(ArrayList<String[]> entry, int sizeParents){
     /*   this.parents = parents;
        this.propebilties = propebilties;*/
     this.entry = entry;

    }
    public entries(){
      /* parents = new ArrayList<>();
       propebilties = new ArrayList<>();*/
      this.entry = new ArrayList<>();
    }


}
