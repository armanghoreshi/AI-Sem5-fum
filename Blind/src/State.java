/**
 * Created by ArmanGhoreshi on 1/24/2019 AD.
 */

import java.util.ArrayList;
import java.util.Arrays;


public class State
{

    private int outOfPlace = 0;

    private int manDist = 0;

    private final int[] GOAL = new int[]
            { 1, 2, 3, 4, 5, 6, 7, 8, 0 };
    private int[] curBoard;

    private int cost=0;


    public State(int[] board)
    {
        curBoard = board;
        setOutOfPlace();
    }



    public double findCost()
    {
        return 1;
    }


    private void setOutOfPlace()
    {
        for (int i = 0; i < curBoard.length; i++)
        {
            if (curBoard[i] != GOAL[i])
            {
                outOfPlace++;
            }
        }
    }


//


    private int getHole()
    {
        int holeIndex = -1;

        for (int i = 0; i < 9; i++)
        {
            if (curBoard[i] == 0)
                holeIndex = i;
        }
        return holeIndex;
    }

    public int getOutOfPlace()
    {
        return outOfPlace;
    }


    public int getManDist()
    {
        return manDist;
    }


    private int[] copyBoard(int[] state)
    {
        int[] ret = new int[9];
        for (int i = 0; i < 9; i++)
        {
            ret[i] = state[i];
        }
        return ret;
    }


    public ArrayList<State> genSuccessors()
    {
        ArrayList<State> successors = new ArrayList<State>();
        int ZeroIndex = getHole();

        if (ZeroIndex != 0 && ZeroIndex != 3 && ZeroIndex != 6)
        {
            swap(ZeroIndex - 1, ZeroIndex, successors);
        }
        if (ZeroIndex != 6 && ZeroIndex != 7 && ZeroIndex != 8)
        {
            swap(ZeroIndex + 3, ZeroIndex, successors);
        }
        if (ZeroIndex != 0 && ZeroIndex != 1 && ZeroIndex != 2)
        {
            swap(ZeroIndex - 3, ZeroIndex, successors);
        }
        if (ZeroIndex != 2 && ZeroIndex != 5 && ZeroIndex != 8)
        {
            swap(ZeroIndex + 1, ZeroIndex, successors);
        }

        return successors;
    }


    private void swap(int d1, int d2, ArrayList<State> s)
    {

        int[] cpy = new int[9];
        for (int i = 0; i < 9; i++)
        {
            cpy[i] = curBoard[i];
        }

        int temp = cpy[d1];
        cpy[d1] = curBoard[d2];
        cpy[d2] = temp;
        int cost = Math.abs(d2-temp);
        State temp_state = new State(cpy);
        temp_state.setCost(cost);
        s.add(temp_state);
    }



    public boolean isGoal()
    {
        if (Arrays.equals(curBoard, GOAL))
        {
            return true;
        }
        return false;
    }



    public void printState()
    {
        System.out.println(curBoard[0] + " | " + curBoard[1] + " | "
                + curBoard[2]);
        System.out.println("---------");
        System.out.println(curBoard[3] + " | " + curBoard[4] + " | "
                + curBoard[5]);
        System.out.println("---------");
        System.out.println(curBoard[6] + " | " + curBoard[7] + " | "
                + curBoard[8]);
    }



    public boolean equals(State s)
    {
        if (Arrays.equals(curBoard, ((State) s).getCurBoard()))
        {
            return true;
        }
        else
            return false;

    }


    public int[] getCurBoard()
    {
        return curBoard;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }


}
