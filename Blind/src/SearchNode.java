/**
 * Created by ArmanGhoreshi on 1/24/2019 AD.
 */
public class SearchNode implements Comparable<SearchNode>
{

    private State curState;
    private SearchNode parent;
    private int gcost;
    private double hCost;
    private double fCost;


    public SearchNode(State s)
    {
        curState = s;
        parent = null;
        gcost = s.getCost();
        hCost = 0;
        fCost = 0;
    }

    public SearchNode(SearchNode prev, State s, int c, double h)
    {
        parent = prev;
        curState = s;
        gcost = c;
        hCost = h;
        fCost = gcost + hCost;
    }


    public State getCurState()
    {
        return curState;
    }


    public SearchNode getParent()
    {
        return parent;
    }


    public int getCost()
    {
        return gcost;
    }

    public double getHCost()
    {
        return hCost;
    }

    public double getFCost()
    {
        return fCost;
    }


    @Override
    public int compareTo(SearchNode s) {
        if (this.curState.getCost() > s.curState.getCost())
            return 1;
        else if (this.curState.getCost() == s.curState.getCost())
            return 0;
        return -1;
    }
}
