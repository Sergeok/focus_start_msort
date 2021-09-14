package kwaymergesort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoserTree<T extends Comparable<T>> {

    private List<Integer> treeOfInd;
    private final List<T> leaves;
    private final boolean isSortDirectionAsc;

    private boolean compare(T p1, T p2) {
        if (isSortDirectionAsc) {
            return p1.compareTo(p2) > 0;
        } else {
            return p1.compareTo(p2) < 0;
        }
    }

    private void adjustTree(int s) {
        for (int t = (s + getTreeSize()) / 2; t > 0 && s >= 0; t /= 2) {
            if (treeOfInd.get(t) == -1 || compare(leaves.get(s), (leaves.get(treeOfInd.get(t))))) {
                int tmp = s;
                s = treeOfInd.get(t);
                treeOfInd.set(t, tmp);
            }
        }

        treeOfInd.set(0, s);
    }

    public LoserTree(List<T> primaryListToMerging, boolean isSortDirectionAsc) {
        this.leaves = primaryListToMerging;
        this.treeOfInd = new ArrayList<>(Collections.nCopies(leaves.size(), -1));
        this.isSortDirectionAsc = isSortDirectionAsc;

        for (int i = getTreeSize()-1; i >= 0; i--) {
            adjustTree(i);
        }
    }

    public int getTreeSize() {
        return treeOfInd.size();
    }

    public T getLeaf(int i) {
        return leaves.get(i);
    }

    public void setLeaf(T leaf, int s) {
        leaves.set(s, leaf);
        adjustTree(s);
    }

    public int getWinner() {
        return treeOfInd.get(0);
    }

    public void removeWinner() {
        int s = treeOfInd.get(0);

        leaves.remove(s);
        treeOfInd = new ArrayList<>(Collections.nCopies(leaves.size(), -1));

        for (int i = getTreeSize()-1; i >= 0; i--) {
            adjustTree(i);
        }
    }

}