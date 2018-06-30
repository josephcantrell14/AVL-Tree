import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


/**
 * SSTests
 *
 **************
 * HOW TO USE *
 **************
 * If you want to add your own test cases or you want to understand what the
 * tests are doing, please refer below
 *
 * assertTreeEquals(int[], Integer[], int[], int[])
 *  - this method takes, the positions, values, heights, and balances of all
 *  - the nodes in the AVL tree and verifies the correctness of avlTree.
 *
 *  - The positions are 1 index and level order (exactly like a heap)
 *  - For more explanation, look at the comments in the instance variable
 *  - declarations below
 *
 * @author Shashank Singh
 * @version 1.2
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SSTests {

    //region #VARIABLES
    private static final int TIMEOUT = 200;
    private AVL<Integer> avlTree;

    /*
     * pos : locations of values in tree (1-indexed level order) (heap style)
     * Example:            1
     *                    / \
     *                   /   \
     *                  2     3
     *                 / \   / \
     *                4   5 6   7
     */
    private int[] pos;

    /*
     * vals : expected values at the corresponding positions in pos (above)
     */
    private Integer[] vals;

    /*
     * heights : expected heights of nodes at the corresponding positions
     */
    private int[] heights;

    /*
     * balance : expected balances of nodes at the corresponding positions
     */
    private int[] balances;
    //endregion

    @Test(timeout = TIMEOUT)
    public void t01EmptyConstructor() {
        // empty constructor should do nothing
        avlTree = new AVL<>();
        assertNull(avlTree.getRoot());
        assertEquals(0, avlTree.size());
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void t02ConstructorNullArg() {
        avlTree = new AVL<>(null);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void t03ConstructorNullElement() {
        Integer[] args = {1, 2, null};
        avlTree = new AVL<>(Arrays.asList(args));
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void t04AddNull() {
        (new AVL<>()).add(null);
    }

    @Test(timeout = TIMEOUT)
    public void t05ConstructorSizeClear() {
        Integer[] args = {7, 2};
        avlTree = new AVL<>(Arrays.asList(args));

        assertNotNull(avlTree.getRoot());
        assertEquals(2, avlTree.size());

        avlTree.clear();
        assertNull(avlTree.getRoot());
        assertEquals(0, avlTree.size());
    }

    @Test(timeout = TIMEOUT)
    public void t06AddDuplicate() {
        // duplicate adds should be ignored
        avlTree = new AVL<>();
        avlTree.add(5);
        avlTree.add(5);
        assertEquals(1, avlTree.size());
    }

    @Test(timeout = TIMEOUT)
    public void t07AddNoBalancing() {
        // multiple adds none of which should cause your tree to need
        // rebalancing
        Integer[] args = {3, 2, 7};
        avlTree  = new AVL<>(Arrays.asList(args));
        pos      = new     int[] {1, 2, 3};
        vals     = new Integer[] {3, 2, 7};
        heights  = new     int[] {1, 0, 0};
        balances = new     int[] {0, 0, 0};
        /*
         *              3
         *             / \
         *            2   7
         */
        assertTreeEquals();

        avlTree.add(1);
        pos      = new     int[] {1, 2, 3, 4};
        vals     = new Integer[] {3, 2, 7, 1};
        heights  = new     int[] {2, 1, 0, 0};
        balances = new     int[] {1, 1, 0, 0};
        /*
         *              3
         *             / \
         *            2   7
         *           /
         *          1
         */
        assertTreeEquals();

        avlTree.add(9);
        avlTree.add(5);
        pos      = new     int[] {1, 2, 3, 4, 6, 7};
        vals     = new Integer[] {3, 2, 7, 1, 5, 9};
        heights  = new     int[] {2, 1, 1, 0, 0, 0};
        balances = new     int[] {0, 1, 0, 0, 0, 0};
        /*
         *              3
         *             / \
         *            2   7
         *           /   / \
         *          1   5   9
         */
        assertTreeEquals();

        avlTree.add(4);
        avlTree.add(6);
        pos      = new     int[]  {1, 2, 3, 4, 6, 7, 12, 13};
        vals     = new Integer[]  {3, 2, 7, 1, 5, 9,  4,  6};
        heights  = new     int[]  {3, 1, 2, 0, 1, 0,  0,  0};
        balances = new     int[] {-1, 1, 1, 0, 0, 0,  0,  0};
        /*
         *              3
         *             / \
         *            2   7
         *           /   / \
         *          1   5   9
         *             / \
         *            4   6
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT)
    public void t08AddSingleRightRotation() {
        // this addition should cause a single right rotation rebalancing
        // when the last element (2) is added
        Integer[] args = {4, 3, 2};
        avlTree  = new AVL<>(Arrays.asList(args));
        pos      = new     int[] {1, 2, 3};
        vals     = new Integer[] {3, 2, 4};
        heights  = new     int[] {1, 0, 0};
        balances = new     int[] {0, 0, 0};
        /*
         *   BEFORE REBALANCING                 AFTER REBALANCING
         *
         *          4                                   3
         *         /                                   / \
         *        3                                   2   4
         *       /
         *      2
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT)
    public void t09AddSingleRightRotationManyUnbalanced() {
        // this addition should cause a single right rotation rebalancing
        // when the last element (1) is added
        // this checks that you're attempting rebalancing at the lowest
        // unbalanced node since there will be 2 unbalanced nodes after
        // the 1 is inserted
        Integer[] args = {4, 5, 3, 2, 1};
        avlTree  = new AVL<>(Arrays.asList(args));
        pos      = new     int[] {1, 2, 3, 4, 5};
        vals     = new Integer[] {4, 2, 5, 1, 3};
        heights  = new     int[] {2, 1, 0, 0, 0};
        balances = new     int[] {1, 0, 0, 0, 0};
        /*
         *    BEFORE REBALANCING                  AFTER REBALANCING
         *
         *            4                                   4
         *           / \                                 / \
         *          3   5                               2   5
         *         /                                   / \
         *        2                                   1   3
         *       /
         *      1
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT)
    public void t10AddSingleLeftRotation() {
        // this addition should cause a single left rotation rebalancing
        // when the last element (4) is added
        // this is the mirror case for t08AddSingleRightRotation
        Integer[] args = {2, 3, 4};
        avlTree  = new AVL<>(Arrays.asList(args));
        pos      = new     int[] {1, 2, 3};
        vals     = new Integer[] {3, 2, 4};
        heights  = new     int[] {1, 0, 0};
        balances = new     int[] {0, 0, 0};
        /*
         *   BEFORE REBALANCING                 AFTER REBALANCING
         *
         *          2                                   3
         *           \                                 / \
         *            3                               2   4
         *             \
         *              4
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT)
    public void t11AddSingleLeftRotationManyUnbalanced() {
        // this addition should cause a single left rotation rebalancing
        // when the last element (5) is added
        // this checks that you're attempting rebalancing at the lowest
        // unbalanced node since there will be 2 unbalanced nodes after
        // the 5 is inserted
        // this is the mirror case for t09AddSingleRightRotationManyUnbalanced
        Integer[] args = {2, 1, 3, 4, 5};
        avlTree  = new AVL<>(Arrays.asList(args));
        pos      = new     int[]  {1, 2, 3, 6, 7};
        vals     = new Integer[]  {2, 1, 4, 3, 5};
        heights  = new     int[]  {2, 0, 1, 0, 0};
        balances = new     int[] {-1, 0, 0, 0, 0};
        /*
         *    BEFORE REBALANCING                  AFTER REBALANCING
         *
         *            2                                   2
         *           / \                                 / \
         *          1   3                               1   4
         *               \                                 / \
         *                4                               3   5
         *                 \
         *                  5
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT)
    public void t12AddDoubleLeftRightRotation() {
        // this addition should cause a double rotation rebalancing
        // (left rotation then right rotation)
        // when 1 is added
        Integer[] args = {3, 2, 4, 0};
        avlTree  = new AVL<>(Arrays.asList(args));
        pos      = new     int[] {1, 2, 3, 4};
        vals     = new Integer[] {3, 2, 4, 0};
        heights  = new     int[] {2, 1, 0, 0};
        balances = new     int[] {1, 1, 0, 0};
        /*
         *              3
         *             / \
         *            2   4
         *           /
         *          0
         */
        assertTreeEquals();

        avlTree.add(1);
        pos      = new     int[] {1, 2, 3, 4, 5};
        vals     = new Integer[] {3, 1, 4, 0, 2};
        heights  = new     int[] {2, 1, 0, 0, 0};
        balances = new     int[] {1, 0, 0, 0, 0};
        /*
         *  BEFORE REBALANCING        FIRST ROTATION      AFTER REBALANCING
         *
         *              3                  3                      3
         *             / \                / \                    / \
         *            2   4              2   4                  1   4
         *           /                  /                      / \
         *          0                  1                      0   2
         *           \                /
         *            1              0
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT)
    public void t13AddDoubleRightLeftRotation() {
        // this addition should cause a double rotation rebalancing
        // (right rotation then left rotation)
        // when 5 is added
        // this is the mirror case for t12AddDoubleLeftRightRotation
        Integer[] args = {3, 2, 4, 6};
        avlTree  = new AVL<>(Arrays.asList(args));
        pos      = new     int[]  {1, 2,  3, 7};
        vals     = new Integer[]  {3, 2,  4, 6};
        heights  = new     int[]  {2, 0,  1, 0};
        balances = new     int[] {-1, 0, -1, 0};
        /*
         *              3
         *             / \
         *            2   4
         *                 \
         *                  6
         */
        assertTreeEquals();

        avlTree.add(5);
        pos      = new     int[]  {1, 2, 3, 6, 7};
        vals     = new Integer[]  {3, 2, 5, 4, 6};
        heights  = new     int[]  {2, 0, 1, 0, 0};
        balances = new     int[] {-1, 0, 0, 0, 0};
        /*
         *  BEFORE REBALANCING       FIRST ROTATION      AFTER REBALANCING
         *
         *        3                      3                     3
         *       / \                    / \                   / \
         *      2   4                  2   4                 2   5
         *           \                      \                   / \
         *            6                      5                 4   6
         *           /                        \
         *          5                          6
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void t14RemoveNull() {
        avlTree = new AVL<>();
        avlTree.add(1);
        avlTree.remove(null);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void t15RemoveDoesNotExist() {
        avlTree = new AVL<>();
        avlTree.add(1);
        avlTree.remove(2);
    }

    @Test(timeout = TIMEOUT)
    public void t16RemoveSimple() {
        // removing only leaf nodes
        avlTree = new AVL<>();
        avlTree.add(1);
        assertEquals(1, (int) avlTree.remove(1));
        assertNull(avlTree.getRoot());
        assertEquals(0, avlTree.size());

        avlTree.add(2);
        avlTree.add(3);
        avlTree.add(1);
        pos      = new     int[] {1, 2, 3};
        vals     = new Integer[] {2, 1, 3};
        heights  = new     int[] {1, 0, 0};
        balances = new     int[] {0, 0, 0};
        /*
         *        2
         *       / \
         *      1   3
         */
        assertTreeEquals();

        assertEquals(1, (int) avlTree.remove(1));
        pos      = new     int[]  {1, 3};
        vals     = new Integer[]  {2, 3};
        heights  = new     int[]  {1, 0};
        balances = new     int[] {-1, 0};
        /*
         *        2
         *         \
         *          3
         */
        assertTreeEquals();

        assertEquals(3, (int) avlTree.remove(3));
        pos      = new     int[] {1};
        vals     = new Integer[] {2};
        heights  = new     int[] {0};
        balances = new     int[] {0};
        /*
         *        2
         */
        assertTreeEquals();

        assertEquals(2, (int) avlTree.remove(2));
        pos      = new     int[] {};
        vals     = new Integer[] {};
        heights  = new     int[] {};
        balances = new     int[] {};
        /*
         *     [EMPTY TREE]
         *         null
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT)
    public void t17RemoveOnlyLeftChild() {
        // removing nodes with only a left child
        Integer[] args = new Integer[] {2, 3, 1, 0};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *         2
         *        / \
         *       1   3
         *      /
         *     0
         */

        assertEquals(1, (int) avlTree.remove(1));
        pos      = new     int[] {1, 2, 3};
        vals     = new Integer[] {2, 0, 3};
        heights  = new     int[] {1, 0, 0};
        balances = new     int[] {0, 0, 0};
        /*
         *         2
         *        / \
         *       0   3
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT)
    public void t18RemoveOnlyRightChild() {
        // removing nodes with only a right child
        Integer[] args = new Integer[] {1, 0, 2, 3};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *        1
         *       / \
         *      0   2
         *           \
         *            3
         */

        assertEquals(2, (int) avlTree.remove(2));
        pos      = new     int[] {1, 2, 3};
        vals     = new Integer[] {1, 0, 3};
        heights  = new     int[] {1, 0, 0};
        balances = new     int[] {0, 0, 0};
        /*
         *       1
         *      / \
         *     0   3
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void t19getNull() {
        avlTree = new AVL<>();
        avlTree.add(5);
        avlTree.get(null);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void t20getNonExistent() {
        avlTree = new AVL<>();
        avlTree.add(5);
        avlTree.get(4);
    }

    @Test(timeout = TIMEOUT)
    public void t21get() {
        Integer[] args = new Integer[] {4, 2, 6, 1, 3, 5, 7};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *                  4
         *                 / \
         *                /   \
         *               2     6
         *              / \   / \
         *             1   3 5   7
         */
        assertEquals(5, (int) avlTree.get(5));
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void t22containsNull() {
        avlTree = new AVL<>();
        avlTree.add(5);
        avlTree.contains(null);
    }

    @Test(timeout = TIMEOUT)
    public void t23contains() {
        Integer[] args = new Integer[] {4, 2, 6, 1, 3, 5, 7};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *                  4
         *                 / \
         *                /   \
         *               2     6
         *              / \   / \
         *             1   3 5   7
         */
        assertTrue(avlTree.contains(5));
        assertFalse(avlTree.contains(100));
    }

    @Test(timeout = TIMEOUT)
    public void t24preorder() {
        Integer[] args = new Integer[] {5, 2, 8, 1, 4, 6, 9, 0, 3, 7};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *                       5
         *                      / \
         *                     /   \
         *                    /     \
         *                   2       8
         *                  / \     / \
         *                 1   4   6   9
         *                /   /     \
         *               0   3       7
         */
        Integer[] expected = new Integer[] {5, 2, 1, 0, 4, 3, 8, 6, 7, 9};
        List<Integer> list = avlTree.preorder();
        assertCorrectOrder(expected, list);
    }

    @Test(timeout = TIMEOUT)
    public void t25postorder() {
        Integer[] args = new Integer[] {5, 2, 8, 1, 4, 6, 9, 0, 3, 7};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *                       5
         *                      / \
         *                     /   \
         *                    /     \
         *                   2       8
         *                  / \     / \
         *                 1   4   6   9
         *                /   /     \
         *               0   3       7
         */
        Integer[] expected = new Integer[] {0, 1, 3, 4, 2, 7, 6, 9, 8, 5};
        List<Integer> list = avlTree.postorder();
        assertCorrectOrder(expected, list);
    }

    @Test(timeout = TIMEOUT)
    public void t26inorder() {
        Integer[] args = new Integer[] {5, 2, 8, 1, 4, 6, 9, 0, 3, 7};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *                       5
         *                      / \
         *                     /   \
         *                    /     \
         *                   2       8
         *                  / \     / \
         *                 1   4   6   9
         *                /   /     \
         *               0   3       7
         */
        Integer[] expected = new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        List<Integer> list = avlTree.inorder();
        assertCorrectOrder(expected, list);
    }

    @Test(timeout = TIMEOUT)
    public void t27levelorder() {
        Integer[] args = new Integer[] {5, 2, 8, 1, 4, 6, 9, 0, 3, 7};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *                       5
         *                      / \
         *                     /   \
         *                    /     \
         *                   2       8
         *                  / \     / \
         *                 1   4   6   9
         *                /   /     \
         *               0   3       7
         */
        List<Integer> list = avlTree.levelorder();
        assertCorrectOrder(args, list);
    }

    @Test(timeout = TIMEOUT)
    public void t28height() {
        avlTree = new AVL<>();
        // height of empty tree is -1
        assertEquals(-1, avlTree.height());

        Integer[] args = new Integer[] {5, 2, 8, 1, 4, 6, 9, 0, 3, 7};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *                       5
         *                      / \
         *                     /   \
         *                    /     \
         *                   2       8
         *                  / \     / \
         *                 1   4   6   9
         *                /   /     \
         *               0   3       7
         */
        assertEquals(3, avlTree.height());
    }

    @Test(timeout = TIMEOUT)
    public void t29equalsTrivial() {
        avlTree = new AVL<>();
        assertNotEquals(avlTree, null);
        assertNotEquals(avlTree, 5);
        assertEquals(avlTree, avlTree);
    }

    @Test(timeout = TIMEOUT)
    public void t30equals() {
        Integer[] args = new Integer[] {5, 2, 8, 1, 4, 6, 9, 0, 3, 7};
        avlTree = new AVL<>(Arrays.asList(args));
        AVL<Integer> otherTree = new AVL<>(Arrays.asList(args));

        assertEquals(avlTree, otherTree);

        otherTree.remove(7);
        assertNotEquals(avlTree, otherTree);
    }

    @Test(timeout = TIMEOUT)
    public void t31RemoveTwoChildrenSuccessorLeftTraversal() {
        // removing node with 2 children requiring left traversal
        // for finding the successor in the right subtree
        Integer[] args = new Integer[] {2, 1, 5, 0, 4, 6, 3};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *                  2
         *                 / \
         *                1   5
         *               /   / \
         *              0   4   6
         *                 /
         *                3
         */

        assertEquals(2, (int) avlTree.remove(2));
        pos      = new     int[] {1, 2, 3, 4, 6, 7};
        vals     = new Integer[] {3, 1, 5, 0, 4, 6};
        heights  = new     int[] {2, 1, 1, 0, 0, 0};
        balances = new     int[] {0, 1, 0, 0, 0, 0};
        /*
         *                  3
         *                 / \
         *                1   5
         *               /   / \
         *              0   4   6
         */
        assertTreeEquals();

        assertEquals(3, (int) avlTree.remove(3));
        pos      = new     int[] {1, 2,  3, 4, 7};
        vals     = new Integer[] {4, 1,  5, 0, 6};
        heights  = new     int[] {2, 1,  1, 0, 0};
        balances = new     int[] {0, 1, -1, 0, 0};
        /*
         *                  4
         *                 / \
         *                1   5
         *               /     \
         *              0       6
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT)
    public void t32RemoveTwoChildrenSuccessorNoLeftTraversal() {
        // removing node with 2 children requiring no left traversal
        // for finding the successor in the right subtree
        Integer[] args = new Integer[] {4, 1, 5, 0, 6};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *                  4
         *                 / \
         *                1   5
         *               /     \
         *              0       6
         */

        assertEquals(4, (int) avlTree.remove(4));
        pos      = new     int[] {1, 2, 3, 4};
        vals     = new Integer[] {5, 1, 6, 0};
        heights  = new     int[] {2, 1, 0, 0};
        balances = new     int[] {1, 1, 0, 0};
        /*
         *                  5
         *                 / \
         *                1   6
         *               /
         *              0
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT)
    public void t33RemoveOneChildRebalanceSingleRightRotation() {
        // removing node with 1 child requiring single rebalancing
        // right rotation
        Integer[] args = new Integer[] {5, 1, 6, 0};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *                  5
         *                 / \
         *                1   6
         *               /
         *              0
         */

        assertEquals(6, (int) avlTree.remove(6));
        pos      = new     int[] {1, 2, 3};
        vals     = new Integer[] {1, 0, 5};
        heights  = new     int[] {1, 0, 0};
        balances = new     int[] {0, 0, 0};
        /*
         *     BEFORE REBALANCING                AFTER REBALANCING
         *
         *            5                                 1
         *           /                                 / \
         *          1                                 0   5
         *         /
         *        0
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT)
    public void t34RemoveOneChildRebalanceSingleLeftRotation() {
        // removing node with 1 child requiring single rebalancing
        // left rotation
        // this is the mirror case for test 33
        Integer[] args = new Integer[] {1, 0, 5, 7};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *                  1
         *                 / \
         *                0   5
         *                     \
         *                      7
         */

        assertEquals(0, (int) avlTree.remove(0));
        pos      = new     int[] {1, 2, 3};
        vals     = new Integer[] {5, 1, 7};
        heights  = new     int[] {1, 0, 0};
        balances = new     int[] {0, 0, 0};
        /*
         *     BEFORE REBALANCING                AFTER REBALANCING
         *
         *            1                                 5
         *             \                               / \
         *              5                             1   7
         *               \
         *                7
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT)
    public void t35RemoveOneChildRebalanceDoubleRightLeftRotation() {
        // removing node with 1 child requiring double rebalancing
        // right rotation then left rotation
        Integer[] args = new Integer[] {5, 1, 7, 6};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *             5
         *            / \
         *           1   7
         *              /
         *             6
         */

        assertEquals(1, (int) avlTree.remove(1));
        pos      = new     int[] {1, 2, 3};
        vals     = new Integer[] {6, 5, 7};
        heights  = new     int[] {1, 0, 0};
        balances = new     int[] {0, 0, 0};
        /*
         *   BEFORE REBALANCING     SINGLE ROTATION     AFTER REBALANCING
         *
         *             5                 5
         *              \                 \                     6
         *               7                 6                   / \
         *              /                   \                 5   7
         *             6                     7
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT)
    public void t36RemoveOneChildRebalanceDoubleLeftRightRotation() {
        // removing node with 1 child requiring double rebalancing
        // left rotation then right rotation
        // this is the mirror case for test 35
        Integer[] args = new Integer[] {6, 4, 7, 5};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *             6
         *            / \
         *           4   7
         *            \
         *             5
         */

        avlTree = new AVL<>(Arrays.asList(args));

        assertEquals(7, (int) avlTree.remove(7));
        pos      = new     int[] {1, 2, 3};
        vals     = new Integer[] {5, 4, 6};
        heights  = new     int[] {1, 0, 0};
        balances = new     int[] {0, 0, 0};
        /*
         *   BEFORE REBALANCING     SINGLE ROTATION     AFTER REBALANCING
         *
         *             6                    6
         *            /                    /                   5
         *           4                    5                   / \
         *            \                  /                   4   6
         *             5                4
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT)
    public void t37RemoveTwoChildrenRebalanceSingleLeftRotation() {
        // removing node with 2 children requiring single rebalancing
        // left rotation
        Integer[] args = new Integer[] {6, 4, 9, 2, 7, 11, 13};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *             6
         *            / \
         *           4   9
         *          /   / \
         *         2   7   11
         *                  \
         *                   13
         */

        avlTree = new AVL<>(Arrays.asList(args));

        assertEquals(6, (int) avlTree.remove(6));
        pos      = new     int[] {1, 2,  3, 4, 6,  7};
        vals     = new Integer[] {7, 4, 11, 2, 9, 13};
        heights  = new     int[] {2, 1,  1, 0, 0,  0};
        balances = new     int[] {0, 1,  0, 0, 0,  0};
        /*
         *       BEFORE REBALANCING           AFTER REBALANCING
         *
         *            7                              7
         *           / \                            / \
         *          4   9                          4   11
         *         /     \                        /   / \
         *        2       11                     2   9   13
         *                 \
         *                  13
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT)
    public void t38RemoveTwoChildrenRebalanceDoubleRightLeftRotation() {
        // removing node with 2 children requiring double rebalancing
        // right rotation then left rotation
        Integer[] args = new Integer[] {6, 4, 9, 2, 7, 11, 10};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *             6
         *            / \
         *           4   9
         *          /   / \
         *         2   7   11
         *                /
         *               10
         */

        avlTree = new AVL<>(Arrays.asList(args));

        assertEquals(6, (int) avlTree.remove(6));
        pos      = new     int[] {1, 2,  3, 4, 6,  7};
        vals     = new Integer[] {7, 4, 10, 2, 9, 11};
        heights  = new     int[] {2, 1,  1, 0, 0,  0};
        balances = new     int[] {0, 1,  0, 0, 0,  0};
        /*
         *   BEFORE REBALANCING    SINGLE ROTATION      AFTER REBALANCING
         *
         *           7                   7                      7
         *          / \                 / \                    / \
         *         4   9               4   9                  4   10
         *        /     \             /     \                /   / \
         *       2       11          2       10             2   9   11
         *              /                     \
         *             10                      11
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT)
    public void t39RemoveTwoChildrenRebalanceSingleRightRotation() {
        // removing node with 2 children requiring single rebalancing
        // right rotation
        // this is the mirror case for test 37
        Integer[] args = new Integer[] {6, 4, 9, 2, 5, 10, 1};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *             6
         *            / \
         *           4   9
         *          / \   \
         *         2   5   10
         *        /
         *       1
         */

        avlTree = new AVL<>(Arrays.asList(args));

        assertEquals(6, (int) avlTree.remove(6));
        pos      = new     int[] {1, 2, 3, 4, 6,  7};
        vals     = new Integer[] {4, 2, 9, 1, 5, 10};
        heights  = new     int[] {2, 1, 1, 0, 0,  0};
        balances = new     int[] {0, 1, 0, 0, 0,  0};
        /*
         *     BEFORE REBALANCING             AFTER REBALANCING
         *
         *             9                             4
         *            / \                           / \
         *           4   10                        2   9
         *          / \                           /   / \
         *         2   5                         1   5   10
         *        /
         *       1
         */
        assertTreeEquals();
    }

    @Test(timeout = TIMEOUT)
    public void t40RemoveTwoChildrenRebalanceDoubleLeftRightRotation() {
        // removing node with 2 children requiring double rebalancing
        // left rotation then right rotation
        // this is the mirror case for test 38
        Integer[] args = new Integer[] {9, 4, 10, 2, 6, 11, 5};
        avlTree = new AVL<>(Arrays.asList(args));
        /*
         *             9
         *            / \
         *           4   10
         *          / \   \
         *         2   6   11
         *            /
         *           5
         */

        avlTree = new AVL<>(Arrays.asList(args));

        assertEquals(9, (int) avlTree.remove(9));
        pos      = new     int[] {1, 2,  3, 4, 5,  7};
        vals     = new Integer[] {6, 4, 10, 2, 5, 11};
        heights  = new     int[] {2, 1,  1, 0, 0,  0};
        balances = new     int[] {0, 0, -1, 0, 0,  0};
        /*
         *
         *   BEFORE REBALANCING     SINGLE ROTATION       AFTER REBALANCING
         *
         *           10                   10                     6
         *          / \                  / \                    / \
         *         4   11               6   11                 4   10
         *        / \                  /                      / \   \
         *       2   6                4                      2   5   11
         *          /                / \
         *         5                2   5
         */
        assertTreeEquals();
    }

    /**
     * Helper method for asserting that an AVL tree has the correct structure
     */
    private void assertTreeEquals() {
        // check if size is as expected
        assertEquals(pos.length, avlTree.size());

        if (pos.length == 0) {
            assertNull(avlTree.getRoot());
        }

        // check contents
        for (int i = 0; i < pos.length; ++i) {
            int position = pos[i];
            String bin = Integer.toBinaryString(position).substring(1);
            AVLNode<Integer> iter = avlTree.getRoot();
            for (char c : bin.toCharArray()) {
                if (iter != null) {
                    iter = c == '1' ? iter.getRight() : iter.getLeft();
                }
            }
            if (vals[i] == null) {
                assertNull(iter);
            } else {
                assertNotNull(iter);
                assertEquals(vals[i], iter.getData());

                // if you're getting an error on the lines below, you're
                // most probably not updating the heights and balances of the
                // node when they are added to the tree or when stuff gets moved
                // around during remove
                assertEquals(heights[i], iter.getHeight());
                assertEquals(balances[i], iter.getBalanceFactor());

            }
        }
    }

    /**
     * Helper method for verifying results from order traversals
     *
     * @param expected expected order
     * @param values   values to check
     */
    private void assertCorrectOrder(Integer[] expected, List<Integer> values) {
        assertEquals(expected.length, values.size());
        for (Integer i : expected) {
            assertEquals(i, values.remove(0));
        }
    }
}
