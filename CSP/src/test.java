import java.util.ArrayList;

/**
 * Created by ArmanGhoreshi on 1/25/2019 AD.
 */
public class test {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(1);
        list.remove(new Integer(1));

        System.out.println(list.toString());
    }
}
