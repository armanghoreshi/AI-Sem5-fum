import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ArmanGhoreshi on 1/24/2019 AD.
 */
public class myNode {

    public myNode parent;

    public ArrayList<Integer> [] domain =new ArrayList[Main.person_count] ;
    public ArrayList<Integer> [] eaten =new ArrayList[Main.person_count] ;
    public int [] full=new int[Main.person_count];
    public ArrayList<Integer> available_vars = new ArrayList<>();
    public int last_available_removed;
    public int last;

    public myNode(myNode parent , ArrayList<Integer> [] domain, ArrayList<Integer> [] eaten, ArrayList<Integer> available_vars, int[] full){
        this.parent = parent;
        this.domain = domain;
        this.eaten = eaten ;
        this.available_vars = available_vars;
        this.full=full;
    }
    public myNode(){

    }

    public void setNode(ArrayList<Integer> [] input1,ArrayList<Integer> [] input2,int[] input3,ArrayList<Integer>  input4){
        setDomain(input1);
        setEaten(input2);
        setFull(input3);
        setAvailable_vars(input4);
    }


    public void setDomain(ArrayList<Integer> [] input){
        for (int i = 0; i < domain.length; i++) {
            domain[i]=new ArrayList<Integer>();
        }
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j <input[i].size() ; j++) {
                this.domain[i].add(input[i].get(j));
            }
        }
    }

    public void setEaten(ArrayList<Integer> [] input){
        for (int i = 0; i < eaten.length; i++) {
            eaten[i]=new ArrayList<Integer>();
        }

        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j <input[i].size() ; j++) {
                this.eaten[i].add(input[i].get(j));
            }
        }
    }
    public void setFull(int[] input){
        for (int i = 0; i < input.length; i++) {
            this.full[i]=input[i];
        }
    }
    public void setAvailable_vars(ArrayList<Integer>  input){
        for (int i = 0; i < input.size(); i++) {
            this.available_vars.add(input.get(i));
        }
    }
}
