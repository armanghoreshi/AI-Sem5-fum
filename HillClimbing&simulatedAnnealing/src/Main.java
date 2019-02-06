import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ArmanGhoreshi on 11/25/2018 AD.
 */
public class Main {

    //////// global variables //////////

    private static int [] people_need;
    private static int [] people_temp;
    private static int [] fruits_energy;
    private static int [] fruits_people;

    private static Scanner input = new Scanner(System.in);


    public static void main(String[] args) {

        //////////////+++++ variables ++++++////////////////

        int people_size = 0;
        int fruit_szie = 0;
        int totalWaste = 0;
        int people_full = 0;
        int count = 0;
        boolean STOP = false;

        int[] best_fruit_people;
        int best_totalWaste = 0;
        int best_people_full = 0;
        int best_oldNeed = 0;
        int best_newNeed = 0;
        int best_fruit_rand = 0;
        int best_people_rand = 0;

        int[] temp_not ;
        int temp_not_counter=-1;
        //-----------------------------------------------//

        //////////////+++++ input ++++++////////////////

        people_size = input.nextInt();
        fruit_szie = input.nextInt();

        people_need = new int[people_size];
        people_temp = new int[people_size];
        fruits_energy = new int[fruit_szie];
        fruits_people = new int[fruit_szie];
        temp_not = new int[fruit_szie];

        best_fruit_people = fruits_people;

        for (int i = 0; i < people_size; i++) {
            people_need[i] = input.nextInt();
            people_temp[i] = people_need[i];
        }

        for (int i = 0; i < fruit_szie; i++) {
            fruits_energy[i] = input.nextInt();
        }

        //-----------------------------------------------//


        //////////////+++++ implementation ++++++////////////////
//        int p_rand=0;
//        for (int i = 0; i < fruit_szie ; i++) {
//            p_rand =ThreadLocalRandom.current().nextInt(-1, people_size);
//            if(people_temp[p_rand]>0){ //hungry
//
//            }
//        }
        int while_count=0;
        int p_rand=0;
        for (int i = 0; i < fruit_szie; i++) {
            p_rand=ThreadLocalRandom.current().nextInt(0, people_size);
            if(people_temp[p_rand]>0) {
                fruits_people[i] = p_rand;
                people_temp[fruits_people[i]] = people_temp[fruits_people[i]] - fruits_energy[i];
            }else{
                fruits_people[i]=-1;
                temp_not[++temp_not_counter]=i;
            }
        }



        int k =0;
        for (int i=0; i < people_size ; i++) {

            while (people_temp[i]>0 && k<=temp_not_counter){
                fruits_people[temp_not[k]]=i;
                people_temp[fruits_people[temp_not[k]]] = people_temp[fruits_people[temp_not[k]]] - fruits_energy[temp_not[k]];
                k++;
            }

            if (people_temp[i] < 0) {
                totalWaste += Math.abs(people_temp[i]);
                people_full++;
            } else if (people_temp[i] == 0) {
                people_full++;
            }
        }


        while (!STOP) {


            int people_full_copy = people_full;
            int totalWaste_copy = totalWaste;
            int fruit_rand;
            int people_rand;
            for (int i = 0; i < fruit_szie; i++) {
                for (int j = -1; j < people_size; j++) {
                    people_full_copy = people_full;
                    totalWaste_copy = totalWaste;
                    fruit_rand = i;
                    people_rand = j;
                    if (people_rand == fruits_people[fruit_rand]) continue;
//                int fruit_rand = ThreadLocalRandom.current().nextInt(0, fruit_szie);
//                int people_rand = ThreadLocalRandom.current().nextInt(-1, people_size); //improvement test

                    int oldNeed = 0;
                    if (fruits_people[fruit_rand] > -1)
                        oldNeed = people_temp[fruits_people[fruit_rand]] + fruits_energy[fruit_rand];

                    int newNeed = 0;
                    if (people_rand > -1) {
                        newNeed = people_temp[people_rand] - fruits_energy[fruit_rand];
                    }

                    if (fruits_people[fruit_rand] > -1) {
                        if (people_temp[fruits_people[fruit_rand]] <= 0 && oldNeed > 0) {
                            people_full_copy--;
                            totalWaste_copy = totalWaste_copy - Math.abs(people_temp[fruits_people[fruit_rand]]);
                        } else if (oldNeed <= 0) {
                            totalWaste_copy = totalWaste_copy - (fruits_energy[fruit_rand]);
                        }
                    }

                    if (people_rand > -1) {
                        if (people_temp[people_rand] > 0 && newNeed <= 0) {
                            people_full_copy++;
                            totalWaste_copy = totalWaste_copy + Math.abs(newNeed);
                        } else if (people_temp[people_rand] <= 0) {
                            totalWaste_copy = totalWaste_copy + (fruits_energy[fruit_rand]);
                        }
                    }


                    int difference = best_people_full - people_full_copy;

                    if (difference < 0) {
                        best_oldNeed = oldNeed;
                        best_newNeed = newNeed;
                        best_people_rand = people_rand;
                        best_fruit_rand = fruit_rand;
                        best_people_full = people_full_copy;
                        best_totalWaste = totalWaste_copy;
                    } else if (difference == 0) {
                        if (best_totalWaste - totalWaste_copy > 0) {//vaziyat behtar shode
                            best_oldNeed = oldNeed;
                            best_newNeed = newNeed;
                            best_people_rand = people_rand;
                            best_fruit_rand = fruit_rand;
                            best_people_full = people_full_copy;
                            best_totalWaste = totalWaste_copy;
                        }
                    }
                } // for j
            }//for i


            if( /*while_count>1000*/ totalWaste==best_totalWaste && people_full==best_people_full ){
                STOP=true;
            }else {
                if (fruits_people[best_fruit_rand] > -1)
                    people_temp[fruits_people[best_fruit_rand]] = best_oldNeed;
                if (best_people_rand > -1)
                    people_temp[best_people_rand] =best_newNeed;
                fruits_people[best_fruit_rand] = best_people_rand;
                people_full = best_people_full;
                totalWaste=best_totalWaste;
            }
            while_count++;
        }//WHILE

        //-----------------------------------------------//

        //////////////+++++ test/print ++++++////////////////
        System.out.print(people_full+" ");
        System.out.println(totalWaste);
        int [] people_fruit_count = new int[people_size];
        Arrays.fill(people_fruit_count,0);
        for (int i = 0; i < fruit_szie; i++) {
            if(fruits_people[i]!=-1)
            people_fruit_count[fruits_people[i]]++;
        }
        for (int i = 0; i < people_size; i++) {
            System.out.print(people_fruit_count[i]+" ");
            for (int j = 0; j <fruit_szie ; j++) {
                if (fruits_people[j] == i)
                    System.out.print(j+" ");
            }
            System.out.println();
        }


        //-----------------------------------------------//

    }
}



/*     ---- print ----

System.out.println(totalWaste);
            System.out.println(people_full);
            for (int i = 0; i < fruit_szie; i++) {
                System.out.print(" / "+fruits_people[i]);
            }
            System.out.println();
            System.out.println("-----------");

*/



