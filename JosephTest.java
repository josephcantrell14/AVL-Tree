import java.util.ArrayList;
import java.util.List;

/**
 * Created by josephcantrell14 on 3/4/2016.
 */
public class JosephTest {
    public static void main(String[] args) {
        AVL<Integer> avl = new AVL<Integer>();
        avl.add(5);
        avl.add(6);
        avl.add(7);
        avl.add(8);

        avl.add(9);
        avl.add(10);
        avl.add(1);
        avl.add(2);
        avl.add(3);
        //avl.add(4);
        List<Integer> list2 = avl.preorder();
        System.out.println("Root:" + avl.getRoot());
    }
}
