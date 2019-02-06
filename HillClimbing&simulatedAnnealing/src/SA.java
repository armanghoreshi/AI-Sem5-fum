import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ArmanGhoreshi on 11/25/2018 AD.
 */
public class SA {

    //////// global variables //////////

    private static int [] people_need;
    private static int [] people_temp;
    private static int [] fruits_energy;
    private static int [] fruits_people;

    private static Scanner input;

    static {
        //try {
            //input = new Scanner(new File("src/input1.txt"));
            input = new Scanner(System.in);
       // } catch (FileNotFoundException e) {
        //    e.printStackTrace();
        //}
    }

    public static void main(String[] args) {

        //////////////+++++ variables ++++++////////////////

        int people_size =0;
        int fruit_szie =0;
        int totalWaste=0;
        int people_full=0;
        int count=0;
        boolean STOP =false;

        int [] best_fruit_people;
        int best_totalWaste;
        int best_people_full;

        float tempreture = 1.0f;
        float prob=0;
        //-----------------------------------------------//

        //////////////+++++ input ++++++////////////////

        people_size = input.nextInt();
        fruit_szie = input.nextInt();

        people_need = new int[people_size];
        people_temp = new int [people_size];
        fruits_energy = new int[fruit_szie];
        fruits_people = new int[fruit_szie];

        best_fruit_people = fruits_people;

        for (int i = 0; i < people_size; i++) {
            people_need[i]= input.nextInt();
            people_temp[i] = people_need[i];
        }

        for (int i = 0; i < fruit_szie; i++) {
            fruits_energy[i]= input.nextInt();
        }

        //-----------------------------------------------//


        //////////////+++++ implementation ++++++////////////////
//        int restart_counter=0;
//        best_totalWaste=10000000;
//        best_people_full=0;
//
//        while (restart_counter<5) {
//
//            totalWaste=0;
//            people_full=0;
//            count=0;
//            for (int i = 0; i < people_size; i++) {
//
//            people_temp[i]=people_need[i];
//            }

        for (int i = 0; i < fruit_szie; i++) {
            fruits_people[i] = ThreadLocalRandom.current().nextInt(-1, people_size);
            if(fruits_people[i]>-1)
                people_temp[fruits_people[i]] =people_temp[fruits_people[i]] - fruits_energy[i];
        }
        for (int i = 0; i < people_size; i++) {
            if(people_temp[i]<0){
                totalWaste+=Math.abs(people_temp[i]);
                people_full++;
            }else if(people_temp[i]==0) {
                people_full++;
            }
        }

            while (!STOP) {

                prob = (float)Math.random();
                int people_full_copy = people_full;
                int totalWaste_copy = totalWaste;
                int fruit_rand = ThreadLocalRandom.current().nextInt(0, fruit_szie);
                int people_rand = ThreadLocalRandom.current().nextInt(-1, people_size); //improvement test
                if (people_rand == fruits_people[fruit_rand]) continue;
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
                        totalWaste_copy = totalWaste_copy - (oldNeed - people_temp[fruits_people[fruit_rand]]);
                    }
                }

                if (people_rand > -1) {
                    if (people_temp[people_rand] > 0 && newNeed <= 0) {
                        people_full_copy++;
                        totalWaste_copy = totalWaste_copy + Math.abs(newNeed);
                    } else if (people_temp[people_rand] <= 0) {
                        totalWaste_copy = totalWaste_copy + (people_temp[people_rand] - newNeed);
                    }
                }

                int difference = people_full - people_full_copy;
                if (difference < 0  || prob< tempreture ) {
                    if (fruits_people[fruit_rand] > -1)
                        people_temp[fruits_people[fruit_rand]] = oldNeed;
                    if(people_rand >-1)
                        people_temp[people_rand] = newNeed;
                    fruits_people[fruit_rand] = people_rand;
                    people_full = people_full_copy;
                    totalWaste=totalWaste_copy;
                    count = 0;
                } else if (difference > 0) {
                    count++;
                } else {
                    if (totalWaste - totalWaste_copy > 0) {//vaziyat behtar shode
                        people_temp[fruits_people[fruit_rand]] = oldNeed;
                        if (people_rand > -1)
                            people_temp[people_rand] = newNeed;
                        fruits_people[fruit_rand] = people_rand;
                        people_full = people_full_copy;
                        totalWaste = totalWaste_copy;
                        count = 0;
                    } else count++;
                }

                tempreture-=0.005f;
                if (tempreture<0.01) STOP = true;
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
                    System.out.print((j+1)+" ");
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



