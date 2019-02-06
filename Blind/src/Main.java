/**
 * Created by ArmanGhoreshi on 1/24/2019 AD.
 */
public class Main {
    public static void main(String[] args) {

        /////////////// input here /////////////////
//3,4,5,6,7,8,0,1,2
        int[] startingStateBoard = {3,4,5,6,7,8,0,1,2};

        ///////////////////////////////////////////

//      Dfs.search(startingStateBoard);

//      Bfs.search(startingStateBoard);

//      Astar.search(startingStateBoard);

        Ucs.search(startingStateBoard);
    }

}
