import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ArmanGhoreshi on 1/24/2019 AD.
 */
public class Main {

    static int fruit_count;
    static int person_count;
    static int pair_count;

    static int [] fruits_energy ;
    static int [] person_need;
    static int [][] nextto;

    public static void main(String[] args) {

        ////////// var dec /////////
        Stack<myNode> stack = new Stack<>();
        Stack<Integer> not_available_vars = new Stack<>();
        boolean dead_end=false;
        //////////input///////////
        Scanner scanner = new Scanner(System.in);
        person_count = scanner.nextInt();
        person_need = new int[person_count];

        fruit_count = scanner.nextInt();
        fruits_energy = new int[fruit_count];

        for (int i = 0; i < person_count; i++) {
            person_need[i] = scanner.nextInt();
        }

        for (int i = 0; i < fruit_count; i++) {
            fruits_energy[i] = scanner.nextInt();
        }



        pair_count = scanner.nextInt();
        nextto = new int[pair_count][2];

        for (int i = 0; i < pair_count; i++) {
            nextto[i][0] = (scanner.nextInt()-1);
            nextto[i][1] = (scanner.nextInt()-1);
        }
        //////////////////////////

        /** create root */

        ArrayList<Integer> [] root_domain = new ArrayList[person_count];
        ArrayList<Integer> [] root_eaten = new ArrayList[person_count];
        for (int i = 0; i < root_domain.length;i++) {
            root_domain[i]=new ArrayList<Integer>();
            root_eaten[i]=new ArrayList<Integer>();
        }
        ArrayList<Integer> available_vars = new ArrayList<>();
        int [] need = new int[person_count];
        for (int i = 0; i <person_count ; i++) {
            for (int j = 0; j <fruit_count ; j++) {
                root_domain[i].add(j);
            }

        }
        for (int i = 0; i < person_count; i++) {
            available_vars.add(i);
        }
        for (int i = 0; i < person_count; i++) {
            need[i]=person_need[i];
        }
        myNode root = new myNode(null,root_domain,root_eaten,available_vars,need);
        stack.push(root);

        /** go to child
         * 1 -> MRV
         * 2 -> LCV
         * 3 -> Forward
         */


        // main
        int count_while=0;
        while (!stack.isEmpty()) {
          count_while++;
            goal_check(stack,count_while);
            dead_end = dead_end(stack);

            if (dead_end && stack.peek().parent!=null){
                stack.peek().parent.domain[stack.peek().last].remove(stack.peek().parent.domain[stack.peek().last].size()-1);
                stack.pop();
                continue;
            }

            myNode curr_node = stack.peek();
            // choose Variable(student)
            int mrv=0;
            int min=Integer.MAX_VALUE;

            for (int i = 0; i <person_count ; i++) {
                myNode temp_node = stack.peek();
                ArrayList<Integer> [] temp_list = temp_node.domain ;
                if(temp_list[i].size()<min && temp_node.available_vars.contains(i)){
                    min = temp_list[i].size();
                    mrv=i;
                }
            }

            // choose value
//            int value = LCV(curr_node,mrv);
//            System.out.println("value= "+value);
            int value = ThreadLocalRandom.current().nextInt(0,curr_node.domain[mrv].size());
            int energy = fruits_energy[curr_node.domain[mrv].get(value)];
            ArrayList<Integer> index_of_repeat = new ArrayList<>();
            int count_energy=0;
            for (int i = 0; i < fruit_count; i++) {
                if(fruits_energy[i]==energy){
                    count_energy++;
                    index_of_repeat.add(i);
                }
            }

            boolean forward_res = forward_check(index_of_repeat,count_energy,mrv,curr_node,curr_node.domain[mrv].get(value));

            if (forward_res){ // no neighbour domain is empty by choosing this value
                myNode next = new myNode();
                next.setNode(curr_node.domain,curr_node.eaten,curr_node.full,curr_node.available_vars);

                next.eaten[mrv].add(curr_node.domain[mrv].get(value)); // add to eaten
                //next.domain[mrv].remove(value); // remove from domain
                domain_unification(next,curr_node.domain[mrv].get(value));
                if (dead_end && stack.peek().parent!=null){
                    curr_node.domain[mrv].remove(value);
                    if(curr_node.domain[mrv].size()==0){
                        stack.peek().parent.domain[stack.peek().last].remove(stack.peek().parent.domain[stack.peek().last].size()-1);
                        stack.pop();
                    }
                    continue;
                }
                next.full[mrv] -= energy;
                next.last=mrv;
                if (next.full[mrv]<=0){
                    next.available_vars.remove(new Integer(mrv));
                    next.last_available_removed = mrv;
                }
                next.parent=stack.peek();
                forward_delete(index_of_repeat,count_energy,mrv,next);
                stack.push(next);
            }
            else {
                curr_node.domain[mrv].remove((int)value);
                if(curr_node.domain[mrv].size()==0){
                    stack.peek().parent.domain[stack.peek().last].remove(stack.peek().parent.domain[stack.peek().last].size()-1);
                    stack.pop();
                }
            }

        }
    }

    //public static myNode

    public static int LCV(myNode curr, int variable){
        ArrayList<Integer> neighbours = new ArrayList<>();
        for (int i = 0; i <pair_count ; i++) {
            if (nextto[i][0] == variable)
                neighbours.add(nextto[i][1]);
            if (nextto[i][1] == variable)
                neighbours.add(nextto[i][0]);
        }
        int min =Integer.MAX_VALUE;
        int index=0;
        int intersect=0;
        for (int i = 0; i <curr.domain[variable].size() ; i++) { // which value from chosen variable
            int temp_val = curr.domain[variable].get(i);
            intersect=0;
            for (int j = 0; j < neighbours.size(); j++) { // which neighbour
                ArrayList<Integer> temp_list = curr.domain[neighbours.get(j)];
                for (int k = 0; k < temp_list.size(); k++) { // which value of neighbour
                    if (fruits_energy[temp_val] == fruits_energy[temp_list.get(k)]){
                        intersect++;
                    }

                }
            }
            if(intersect < min){
                min=intersect;
                index=i;
            }
        }
        return index;
    }

    public static void forward_delete(ArrayList<Integer> index, int count,int variable,myNode curr){
        for (int i = 0; i < pair_count; i++) {
            if (nextto[i][0] == variable){
                if (!curr.available_vars.contains(nextto[i][1]))
                    continue;
                for (int j = 0; j < count; j++) {
                    if(curr.domain[nextto[i][1]].contains(index.get(j))){
                        curr.domain[nextto[i][1]].remove(new Integer(index.get(j)));
                    }
                }
            }
        }

        for (int i = 0; i < pair_count; i++) {
            if (nextto[i][1] == variable){
                if (!curr.available_vars.contains(nextto[i][0]))
                    continue;
                for (int j = 0; j < count; j++) {
                    if(curr.domain[nextto[i][0]].contains(index.get(j))){
                        curr.domain[nextto[i][0]].remove(new Integer(index.get(j)));
                    }
                }
            }
        }
    }

    public static boolean forward_check(ArrayList<Integer> index, int count,int variable,myNode curr, int value_number){ // if ok return true

        for (int i = 0; i < pair_count; i++) {
            if (nextto[i][0] == variable){
                if (!curr.available_vars.contains(nextto[i][1]))
                    continue;
                int intersect =0;
                for (int j = 0; j < count; j++) {
                    //System.out.println("i:"+i+"  j:"+j);
                    if(curr.domain[nextto[i][1]].contains(index.get(j))){
                        intersect++;
                    }
                }
                if (curr.domain[nextto[i][1]].size()-intersect ==0 && curr.full[nextto[i][1]]>0){
                    return false;
                }
            }
        }
        for (int i = 0; i < pair_count; i++) {
            if (nextto[i][1] == variable){
                if (!curr.available_vars.contains(nextto[i][0]))
                    continue;
                int intersect =0;
                for (int j = 0; j < count; j++) {
                    if(curr.domain[nextto[i][0]].contains(index.get(j))){
                        intersect++;
                    }
                }
                if (curr.domain[nextto[i][0]].size()-intersect ==0 && curr.full[nextto[i][0]]>0){
                    return false;
                }
            }
        }
//        for (int i = 0; i < curr.domain.length; i++) {
//            if (curr.domain[i].contains(value_number)){
//                if (curr.domain[i].size()==1)
//                    return false;
//            }
//        }
        return true;
    }

    public static void domain_unification(myNode node,int value){
        for (int i = 0; i < node.domain.length; i++) {
            node.domain[i].remove(new Integer(value));
        }
    }

    public static void goal_check(Stack<myNode> s,int count){
        myNode temp_node = s.peek();
        ArrayList<Integer> [] temp_list = temp_node.eaten ;

        boolean goal = true;
        for (int i = 0; i < person_count && goal; i++) {
            int sum =0;
            for (int j = 0; j < temp_list[i].size(); j++) {
                sum += fruits_energy[temp_list[i].get(j)];
            }
            if (sum<person_need[i])
                goal=false;
        }

        if(goal){
            for (int i = 0; i < person_count; i++) {
                System.out.println("person "+i+" : ");
                for (int j = 0; j < temp_list[i].size(); j++) {
                    System.out.print(" | "+temp_list[i].get(j));
                }
                System.out.println();
            }
            System.out.println("--"+count+"--");
            System.exit(1);
        }// goal
    }//goal check

    public static boolean dead_end(Stack<myNode> s){
        myNode temp_node = s.peek();
        ArrayList<Integer> [] temp_list = temp_node.domain ;

        boolean dead = false;
        for (int i = 0; i < person_count && !dead; i++) {
            if (temp_list[i].size()==0 && temp_node.full[i]>0)
                dead=true;
        }
        return dead;
    }
}//main
