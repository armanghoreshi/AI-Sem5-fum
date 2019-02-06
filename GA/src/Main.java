import sun.font.FontRunIterator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Created by ArmanGhoreshi on 12/6/2018 AD.
 */
public class Main {

    ///////// var dec ////////
//    static Scanner parameters;
    static Scanner input;
    static int [][] population ;                                    // 2d array of which fruit is for who
    static int [][] population_need;                                // each persons current need
    static int [] fruits;                                           // energy of fruits
    static int [] people;                                           // max need of a person
    static ArrayList<ArrayList<Integer>> population_info_sorted ;   //col1:chromosome number / col2:full / col3: waste
    static int population_size;
    static int people_size;
    static int fruit_size;
    static int iteration;
    static float lambda;
    static float mutation_rate;
    static Random rand = new Random();
    //////////////////////////


    public static void main(String[] args) {

        //--------------------------------      *    initial     *       --------------------------------------------//


            //parameters = new Scanner(new File("/Users/arman/Desktop/GA/parameters.txt"));
            input = new Scanner(System.in);

                                                // parameters file structure
        population_size = 100; // 1 ---> population size
        iteration = 100000;       // 2 ---> iterations
        lambda = 0.07f;        // 3 ---> lambda
        mutation_rate = 0.4f; // 4 ---> mutation rate

        people_size = input.nextInt();
        fruit_size = input.nextInt();

        population = new int[population_size][fruit_size];
        fruits = new int[fruit_size];
        people= new int[people_size];
        population_need = new int[population_size][people_size];
        population_info_sorted = new ArrayList<ArrayList<Integer>>();

        // reading values of fruits and humans need from input and filling up arrays.
        int people_total=0;
        int fruit_total=0;

        for (int i = 0; i < people_size; i++) {
            people[i]=input.nextInt();
            people_total+=people[i];
            for (int j = 0; j < population_size; j++) {
                population_need[j][i]=people[i];
            }
        }
        for (int i = 0; i < fruit_size; i++) {
            fruits[i]=input.nextInt();
            fruit_total+=fruits[i];
        }
        //can total fruits satisfy all people?
        boolean enough = false;
        if(people_total<fruit_total)
            enough=true;
        // create first state population
        int p_rand;
        int full_temp=0;
        int waste_temp=0;
        boolean before=false;

        // if fruits are more than needed energy then throwing away some fruits may decrease total waste.
        if(enough) {
            ArrayList<Integer> chromosome_temp_list = new ArrayList<Integer>();
            ArrayList<Integer> full_temp_list = new ArrayList<Integer>();
            ArrayList<Integer> waste_temp_list = new ArrayList<Integer>();
            for (int i = 0; i < population_size; i++) {
                full_temp=0;
                for (int j = 0; j < fruit_size; j++) {
                    p_rand = ThreadLocalRandom.current().nextInt(0, people_size); // select from people randomly
                    if (population_need[i][p_rand] > 0) {
                        population[i][j] = p_rand;
                        population_need[i][p_rand] -= fruits[j];
                        if(population_need[i][p_rand]<=0)
                            full_temp++;
                    } else {
                        population[i][j]=-1;
                    }

                }
                    // waste of every chromosome
                    waste_temp=0;
                    for (int l = 0; l < people_size; l++) {
                        if(population_need[i][l]<0)
                            waste_temp+= -(population_need[i][l]);
                    }
                    // insert and sort population by full people
                    int index = BinarySearch1(full_temp_list,full_temp);
                    full_temp_list.add(index,full_temp);
                    chromosome_temp_list.add(index,i);
                    waste_temp_list.add(index,waste_temp);
            }
                    population_info_sorted.add(chromosome_temp_list);
                    population_info_sorted.add(full_temp_list);
                    population_info_sorted.add(waste_temp_list);
        }else {
            ArrayList<Integer> chromosome_temp_list = new ArrayList<Integer>();
            ArrayList<Integer> full_temp_list = new ArrayList<Integer>();
            ArrayList<Integer> waste_temp_list = new ArrayList<Integer>();
            for (int i = 0; i < population_size; i++) {
                full_temp=0;
                for (int j = 0; j < fruit_size; j++) {
                    p_rand = ThreadLocalRandom.current().nextInt(0, people_size); // select from people randomly
                    population[i][j] = p_rand;
                    if (population[i][j]>0) before=true;
                    population_need[i][p_rand] -= fruits[j];
                    if (population_need[i][p_rand]<=0 && before==true) full_temp++;
                }
                // waste of every chromosome
                waste_temp=0;
                for (int l = 0; l < people_size; l++) {
                    if(population_need[i][l]<0)
                        waste_temp+= -(population_need[i][l]);
                }
                // insert and sort population by full people
                int index = BinarySearch1(full_temp_list,full_temp);
                full_temp_list.add(index,full_temp);
                chromosome_temp_list.add(index,i);
                waste_temp_list.add(index,waste_temp);

            }
                population_info_sorted.add(chromosome_temp_list);
                population_info_sorted.add(full_temp_list);
                population_info_sorted.add(waste_temp_list);
        }


        // testing initial
       // printLog();System.out.println("-------- initial END ---------");
        //--------------------------------------------------------------------------------------------------------------


        //--------------------------------      *    implementation     *       --------------------------------------//

        // iterations of algorithm
        int iteration_counter=0;
        int rnd=0;

        while (iteration_counter< iteration){
            rnd=expo();

            //crossove
            int ch1 = population_info_sorted.get(0).get(rnd);
            int ch2 =population_info_sorted.get(0).get(rnd+1);

            //1. choose two chromosomes
            int [] chromosome_temp1 = population[ch1];
            int [] chromosome_temp2 = population[ch2];


            //2. generate a random index to crossover genomes from that index to end
            int cross_index = ThreadLocalRandom.current().nextInt(1,fruit_size/2);
            //3. do the crossover
            crossover(chromosome_temp1,chromosome_temp2,cross_index , ch1 , ch2);

            //  --------------   mutation    -----------------  \\

            float probability =(float) Math.random();
            if (probability < mutation_rate)
                mutation(chromosome_temp1,ch1);

            //4. calculate new full and waste and insert in the population_info_sorted

            population_info_sorted.get(0).remove(rnd);
            population_info_sorted.get(1).remove(rnd);
            population_info_sorted.get(2).remove(rnd);
            population_info_sorted.get(0).remove(rnd);
            population_info_sorted.get(1).remove(rnd);
            population_info_sorted.get(2).remove(rnd);

            int waste_temp1=0;
            int full_temp1=0;
            int waste_temp2=0;
            int full_temp2=0;
            for (int l = 0; l < people_size; l++) {
                if (population_need[ch1][l]<=0) {
                    waste_temp1 += -(population_need[ch1][l]);
                    full_temp1++;
                }
                if (population_need[ch2][l]<=0) {
                    waste_temp2 += -(population_need[ch2][l]);
                    full_temp2++;
                }
            }
            int index1=0 , index2=0;

            index1=BinarySearch1(population_info_sorted.get(1),full_temp1);
            population_info_sorted.get(0).add(index1,ch1);
            population_info_sorted.get(1).add(index1,full_temp1);
            population_info_sorted.get(2).add(index1,waste_temp1);

            index2=BinarySearch1(population_info_sorted.get(1),full_temp2);
            population_info_sorted.get(0).add(index2,ch2);
            population_info_sorted.get(1).add(index2,full_temp2);
            population_info_sorted.get(2).add(index2,waste_temp2);

            iteration_counter++;
        }// while


        //--------------------------------------------------------------------------------------------------------------
    printLog();
    }// Main


    //--------------------------------      *    BinarySearch     *       --------------------------------------------//
    private static <T>
    int BinarySearch1(List<? extends Comparable<? super T>> list, T key) {
        int low = 0;
        int high = list.size()-1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            Comparable<? super T> midVal = list.get(mid);
            int cmp = midVal.compareTo(key);

            if (cmp > 0)
                low = mid + 1;
            else if (cmp < 0)
                high = mid - 1;
            else {
                return mid; // key found
            }
        }
        return (low );  // key not found
    }
    //------------------------------------------------------------------------------------------------------------------

    public static void printLog(){

        System.out.print(population_info_sorted.get(1).get(0)); //full
        System.out.print(" "+population_info_sorted.get(2).get(0)); //waste
        System.out.println();
        int [] people_fruit_count = new int[people_size];
        Arrays.fill(people_fruit_count,0);
        for (int i = 0; i < fruit_size; i++) {
            if(population[population_info_sorted.get(0).get(0)][i]!=-1)
                people_fruit_count[population[population_info_sorted.get(0).get(0)][i]]++;
        }
        for (int i = 0; i < people_size; i++) {
            System.out.print(people_fruit_count[i]+" ");
            for (int j = 0; j <fruit_size ; j++) {
                if (population[population_info_sorted.get(0).get(0)][j] == i)
                    System.out.print((j+1)+" ");
            }
            System.out.println();
        }System.out.println();
    }



    public static int expo(){
        int result=0;
        result = (int)(((float)Math.log(1-rand.nextFloat()))/(-lambda));
        if(result > (population_size-2)) {
            result=expo();
        }
        return result;
    }

    public static void crossover(int[] chrom1 , int[] chrom2 , int index , int chrom_num1 ,int chrom_num2){
        int swap=0;
        for (int i = 0; i <index ; i++) {
            // take the fruit from first person
            swap = chrom1[i];
            if (swap != -1)
                population_need[chrom_num1][swap] += fruits[i];

            // take the fruit from second person
            chrom1[i]=chrom2[i];
            if (chrom1[i] !=-1)
                population_need[chrom_num2][chrom1[i]] += fruits[i];

            // give fruit to first person
            if (chrom1[i]!=-1)
                population_need[chrom_num1][chrom1[i]]-= fruits[i];
            chrom2[i]=swap;
            //give fruit to second person
            if (chrom2[i] !=-1)
                population_need[chrom_num2][chrom2[i]] -= fruits[i];
        }
    }

    public static void mutation(int [] chromosome_temp , int chrom_num){

        int fruit_rand = ThreadLocalRandom.current().nextInt(0,fruit_size);
        int people_rand = ThreadLocalRandom.current().nextInt(-1,people_size);
        if(chromosome_temp[fruit_rand]!= -1)
            population_need[chrom_num][chromosome_temp[fruit_rand]]+=fruits[fruit_rand];
        if(people_rand!=-1)
            population_need[chrom_num][people_rand]-=fruits[fruit_rand];
        chromosome_temp[fruit_rand]=people_rand;
    }
}
