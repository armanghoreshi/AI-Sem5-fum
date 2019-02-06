/**
 * Created by ArmanGhoreshi on 1/24/2019 AD.
 */
import java.util.ArrayList;
import java.util.Stack;

public class Dfs
{

    public static void search(int[] board)
    {
        SearchNode root = new SearchNode(new State(board));
        Stack<SearchNode> stack = new Stack<SearchNode>();

        stack.add(root);

        Start(stack);
    }


    private static boolean checkRepeats(SearchNode n)
    {
        boolean retValue = false;
        SearchNode checkNode = n;


        while (n.getParent() != null && !retValue)
        {
            if (n.getParent().getCurState().equals(checkNode.getCurState()))
            {
                retValue = true;
            }
            n = n.getParent();
        }

        return retValue;
    }

    public static void Start(Stack<SearchNode> s)
    {
        int searchCount = 1;

        while (!s.isEmpty())
        {
            SearchNode tempNode = (SearchNode) s.pop();

            if (!tempNode.getCurState().isGoal())
            {
                ArrayList<State> tempSuccessors = tempNode.getCurState()
                        .genSuccessors();

                for (int i = 0; i < tempSuccessors.size(); i++)
                {
                    SearchNode newNode = new SearchNode(tempNode,
                            tempSuccessors.get(i), tempNode.getCost()
                            + (int)tempSuccessors.get(i).findCost(), 0);

                    if (!checkRepeats(newNode))
                    {
                        s.add(newNode);
                    }
                }
                searchCount++;
            }
            else
            {
                Stack<SearchNode> solutionPath = new Stack<SearchNode>();
                solutionPath.push(tempNode);
                tempNode = tempNode.getParent();

                while (tempNode.getParent() != null)
                {
                    solutionPath.push(tempNode);
                    tempNode = tempNode.getParent();
                }
                solutionPath.push(tempNode);

                int loopSize = solutionPath.size();

                for (int i = 0; i < loopSize; i++)
                {
                    tempNode = solutionPath.pop();
                    tempNode.getCurState().printState();
                    System.out.println();
                    System.out.println();

                System.out.println("solution:"+solutionPath.size());
                }

                System.exit(0);
            }
        }
        System.out.println("no answer");
    }
}

