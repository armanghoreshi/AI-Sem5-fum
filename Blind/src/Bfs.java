/**
 * Created by ArmanGhoreshi on 1/24/2019 AD.
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


public class Bfs
{

    public static void search(int[] board)
    {
        SearchNode root = new SearchNode(new State(board));
        Queue<SearchNode> queue = new LinkedList<SearchNode>();

        queue.add(root);

        Start(queue);
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


    public static void Start(Queue<SearchNode> q)
    {
        int searchCount = 1;

        while (!q.isEmpty())
        {
//            for (int i = 0; i <9 ; i++) {
//
//                System.out.print(" "+q.peek().getCurState().getCurBoard()[i]);
//            }
//            System.out.println();

            SearchNode tempNode = (SearchNode) q.poll();

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
                        q.add(newNode);
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
                    System.out.println("solution :"+solutionPath.size());
                }

                System.exit(0);
            }
        }

        // This should never happen with our current puzzles.
        System.out.println("no answer");
    }
}
