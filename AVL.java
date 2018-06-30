import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Your implementation of an AVL Tree.
 *
 * @author Joseph Cantrell
 * @version 1.0
 */
public class AVL<T extends Comparable<? super T>> implements AVLInterface<T> {

    // Do not make any new instance variables.
    private AVLNode<T> root;
    private int size;

    /**
     * A no argument constructor that should initialize an empty AVL tree.
     * DO NOT IMPLEMENT THIS CONSTRUCTOR!
     */
    public AVL() {
    }

    /**
     * Initializes the AVL tree with the data in the Collection. The data
     * should be added in the same order it is in the Collection.
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot add null data to"
                    + " the AVL tree.");
        }
        for (T t : data) {
            if (t == null) {
                throw new IllegalArgumentException("Cannot add null data"
                        + " to the AVL tree.");
            }
            add(t);
        }
    }

    @Override
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot add null data"
                    + " to the AVL tree.");
        }
        root = add(data, root);
    }

    /**
     * A helper method to recursively add the node to the tree.
     * @param data The data to add to the tree
     * @param current The node to use for recursion
     * @return The current node
     */
    private AVLNode<T> add(T data, AVLNode<T> current) {
        if (current == null) {
            size++;
            return new AVLNode<T>(data);
        } else if (data.compareTo(current.getData()) == -1) {
            current.setLeft(add(data, current.getLeft()));
        } else if (data.compareTo(current.getData()) == 1) {
            current.setRight(add(data, current.getRight()));
        }
        current.setHeight(updateHeight(current));
        current.setBalanceFactor(updateBalance(current));
        //rebalancing
        if (current.getBalanceFactor() < -1) {
            if (current.getRight() != null
                    && current.getRight().getBalanceFactor() >= 1) {
                current.setRight(rightRotation(current.getRight()));
                return leftRotation(current);
            } else {
                return leftRotation(current);
            }
        } else if (current.getBalanceFactor() > 1) {
            if (current.getLeft() != null
                    && current.getLeft().getBalanceFactor() <= -1) {
                current.setLeft(leftRotation(current.getLeft()));
                return rightRotation(current);
            } else {
                return rightRotation(current);
            }
        }
        return current;
    }

    @Override
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot remove null data"
                    + " because null data cannot exist in the tree.");
        }
        if (size == 0) {
            throw new NoSuchElementException("Could not find element"
                    + " in the tree.");
        }
        return remove(data, root);
    }

    /**
     * A helper method to recursively remove a node from the tree.
     * @param data data to get in the AVL tree
     * @param current The current node in the recursion
     * @return The current node
     */
    private T remove(T data, AVLNode<T> current) {
        AVLNode<T> currentCopy = current;
        if (current == null) {
            throw new NoSuchElementException("The data does not exist in the"
                    + " tree and thus cannot be removed.");
        } else if (data.compareTo(current.getData()) == -1) {
            return remove(data, current.getLeft());
        } else if (data.compareTo(current.getData()) == 1) {
            return remove(data, current.getRight());
        } else {
            size--;
            //remove leaf
            if (current.getHeight() == 0) {
                if (current.getData().compareTo(root.getData()) == 0) {
                    root = null;
                }
                current = null;
                //remove node with one child
            } else if (current.getBalanceFactor() >= 0
                    && current.getHeight() == 1) {
                if (current.getLeft() != null) {
                    current = current.getLeft();
                } else {
                    current = current.getRight();
                }
                current.setHeight(updateHeight(current));
                current.setBalanceFactor(updateBalance(current));
                //remove node with 2 children
            } else {
                current = removeTwoChildren(data, current, current.getRight());
                current.setHeight(updateHeight(current));
                current.setBalanceFactor(updateBalance(current));
            }
            //rebalancing
            if (current != null) {
                if (current.getBalanceFactor() < -1) {
                    if (current.getRight() != null
                            && current.getRight().getBalanceFactor() >= 1) {
                        current.setRight(rightRotation(current.getRight()));
                        return leftRotation(current).getData();
                    } else {
                        return leftRotation(current).getData();
                    }
                } else if (current.getBalanceFactor() > 1) {
                    if (current.getLeft() != null
                            && current.getLeft().getBalanceFactor() <= -1) {
                        current.setLeft(leftRotation(current.getLeft()));
                        return rightRotation(current).getData();
                    } else {
                        return rightRotation(current).getData();
                    }
                }
            }
        }
        return currentCopy.getData();
    }

    /**
     *
     * @param data The data to be removed
     * @param removed The node to be removed
     * @param current The current node
     * @return The current node
     */
    private AVLNode<T> removeTwoChildren(T data,
                                         AVLNode<T> removed,
                                         AVLNode<T> current) {
        if (current.getLeft() != null) {
            removed = current.getLeft();
            return current.getLeft();
        } else if (current.getRight() == null) {
            removed = current;
            return current;
        }
        current.setHeight(updateHeight(current));
        current.setBalanceFactor(updateBalance(current));
        return removeTwoChildren(data, removed, current.getRight());
    }

    @Override
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot search for null data"
                    + " because null data cannot exist in the tree.");
        }
        if (size == 0) {
            throw new NoSuchElementException("Could not find element in"
                    + " the tree.");
        }
        return get(data, root);
    }

    /**
     * A helper method to recursively get data from the tree.
     * @param data The data to get from the tree.
     * @param current The node to be used for recursion.
     * @return The data in the tree.
     */
    private T get(T data, AVLNode<T> current) {
        if (current == null) {
            throw new NoSuchElementException("The data was not "
                    + "found in the tree.");
        } else if (data.compareTo(current.getData()) == 0) {
            return current.getData();
        } else if (data.compareTo(current.getData()) == -1) {
            return get(data, current.getLeft());
        } else {
            return get(data, current.getRight());
        }
    }

    @Override
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot search for null data"
                    + " because null data cannot exist in the tree.");
        }
        return contains(data, root);
    }

    /**
     * A helper method that recursively finds if an element is in the tree.
     * @param data The data to search for in the tree.
     * @param current The node to be used in recursion.
     * @return Whether the tree contains the given data
     */
    private boolean contains(T data, AVLNode<T> current) {
        if (current == null) {
            return false;
        } else if (data.compareTo(current.getData()) == 0) {
            return true;
        } else if (data.compareTo(current.getData()) == -1) {
            return contains(data, current.getLeft());
        } else {
            return contains(data, current.getRight());
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public List<T> preorder() {
        ArrayList<T> list = new ArrayList<T>();
        if (size == 0) {
            return list;
        }
        preorder(list, root);
        return list;
    }

    /**
     * A helper method to traverse the tree using the preorder method.
     * @param list The list to store the nodes' data
     * @param node THe node to be used for recursion.
     */
    private void preorder(ArrayList<T> list, AVLNode<T> node) {
        list.add(node.getData());
        if (node.getLeft() != null) {
            preorder(list, node.getLeft());
        }
        if (node.getRight() != null) {
            preorder(list, node.getRight());
        }
    }

    @Override
    public List<T> postorder() {
        ArrayList<T> list = new ArrayList<T>();
        if (size == 0) {
            return list;
        }
        postorder(list, root);
        return list;
    }

    /**
     * A helper method to traverse the tree using the postorder method.
     * @param list The list to store the nodes' data
     * @param node THe node to be used for recursion.
     */
    private void postorder(ArrayList<T> list, AVLNode<T> node) {
        if (node.getLeft() != null) {
            postorder(list, node.getLeft());
        }
        if (node.getRight() != null) {
            postorder(list, node.getRight());
        }
        list.add(node.getData());
    }

    @Override
    public List<T> inorder() {
        ArrayList<T> list = new ArrayList<T>();
        if (size == 0) {
            return list;
        }
        inorder(list, root);
        return list;
    }

    /**
     * A helper method to traverse the tree using the inorder method.
     * @param list The list to store the nodes' data
     * @param node THe node to be used for recursion.
     */
    private void inorder(ArrayList<T> list, AVLNode<T> node) {
        if (node.getLeft() != null) {
            inorder(list, node.getLeft());
        }
        list.add(node.getData());
        if (node.getRight() != null) {
            inorder(list, node.getRight());
        }
    }

    @Override
    public List<T> levelorder() {
        LinkedList<AVLNode<T>> queue = new LinkedList<AVLNode<T>>();
        LinkedList<T> list = new LinkedList<T>();
        if (size != 0) {
            queue.add(root);
        }
        while (queue.size() != 0) {
            AVLNode<T> ref = queue.getFirst();
            queue.remove(ref);
            list.add(ref.getData());
            AVLNode<T> left = ref.getLeft();
            AVLNode<T> right = ref.getRight();
            if (left != null) {
                queue.add(left);
            }
            if (right != null) {
                queue.add(right);
            }
        }
        return list;
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public int height() {
        if (size == 0) {
            return -1;
        }
        return root.getHeight();
    }
    
    /**
     * Compares two AVLs and checks to see if the trees are the same.  If
     * the trees have the same data in a different arrangement, this method
     * should return false.  This will only return true if the tree is in the
     * exact same arrangement as the other tree.
     *
     * You may assume that you won't get an AVL with a different generic type.
     * For example, if this AVL holds Strings, then you will not get as an input
     * an AVL that holds Integers.
     * 
     * Be sure to also implement the other general checks that .equals() should
     * check as well.
     * 
     * @param other the Object we are comparing this AVL to
     * @return true if other is equal to this AVL, false otherwise.
     */
    public boolean equals(Object other) {
        AVL<T> tree = null;
        if (other instanceof AVL) {
            tree = (AVL<T>) other;
        } else {
            return false;
        }
        boolean equal = true;
        if (other == null) {
            return (this == null);
        } else if (size == 0) {
            return (size == ((AVL<T>) tree).size());
        }
        return equals(root, tree.getRoot());
    }

    /**
     * A helper method to recursively determine if two trees are equal.
     * @param n1 The first node to compare and recurse
     * @param o1 The second node to compare and recurse
     * @return Whether the two trees are equal
     */
    private boolean equals(AVLNode<T> n1, AVLNode<T> o1) {
        if (n1 == null && o1 == null) {
            return true;
        } else if (n1.getData().compareTo(o1.getData()) != 0) {
            return false;
        } else {
            return equals(n1.getLeft(), o1.getLeft())
                    && equals(n1.getRight(), o1.getRight());
        }
    }

    /**
     * A method to update the height of a node.
     * @param current The node to update
     * @return The updated height
     */
    private int updateHeight(AVLNode<T> current) {
        int left = 0;
        int right = 0;
        if (current.getLeft() != null) {
            left = current.getLeft().getHeight();
        } else if (current.getRight() != null) {
            right = current.getRight().getHeight();
        } else {
            return 0;
        }
        return max(left, right) + 1;
    }

    /**
     *
     * @param current The node to update
     * @return The updated balance
     */
    private int updateBalance(AVLNode<T> current) {
        int left = 0;
        int right = 0;
        if (current.getLeft() != null) {
            left = current.getLeft().getHeight();
        } else {
            left = -1;
        }
        if (current.getRight() != null) {
            right = current.getRight().getHeight();
        } else {
            right = -1;
        }
        return left - right;
    }

    /**
     *
     * @param a The first integer to compare
     * @param b The second integer to compare
     * @return The larger integer
     */
    private int max(int a, int b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }

    /**
     *
     * @param current The node needing a right rotation
     * @return The new top node
     */
    private AVLNode<T> rightRotation(AVLNode<T> current) {
        AVLNode<T> top = current.getLeft();
        if (top.getRight() != null) {
            AVLNode<T> newLeft = top.getRight();
            current.setLeft(newLeft);
        } else {
            current.setLeft(null);
        }
        top.setRight(current);
        current.setHeight(updateHeight(current));
        current.setBalanceFactor(updateBalance(current));
        top.setHeight(updateHeight(top));
        top.setBalanceFactor(updateBalance(top));
        return top;
    }

    /**
     *
     * @param current The node needing a left rotation
     * @return The new top node
     */
    private AVLNode<T> leftRotation(AVLNode<T> current) {
        AVLNode<T> top = current.getRight();
        if (top.getLeft() != null) {
            AVLNode<T> newRight = top.getLeft();
            current.setRight(newRight);
        } else {
            current.setRight(null);
        }
        top.setLeft(current);
        current.setHeight(updateHeight(current));
        current.setBalanceFactor(updateBalance(current));
        top.setHeight(updateHeight(top));
        top.setBalanceFactor(updateBalance(top));
        return top;
    }

    @Override
    public AVLNode<T> getRoot() {
        // DO NOT EDIT THIS METHOD!
        return root;
    }
}
