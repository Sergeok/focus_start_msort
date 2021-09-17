package kwaymergesort;

import entity.AppConfiguration;
import entity.NamedScanner;

import java.io.*;
import java.util.*;

public class CustomMergeSort<T extends Comparable<T>> {

    private final Class<T> tClass;
    private final AppConfiguration appConfiguration;

    public CustomMergeSort(Class<T> c, AppConfiguration appConfiguration) {
        tClass = c;
        this.appConfiguration = appConfiguration;
    }

    private void clearOrCreateOutputFile() {
        try {
            new PrintWriter(appConfiguration.getOutFilename()).close();
        } catch (FileNotFoundException e) {
            appConfiguration.SetException(
                    "The output file could not be created", appConfiguration.getOutFilename());
        }
    }

    private void generateNamedScannerList(List<NamedScanner> namedScannerList) {
        for (String filename : appConfiguration.getSrcList()) {
            try {
                namedScannerList.add(new NamedScanner(filename, new Scanner(new File(filename))));
            } catch (FileNotFoundException e) {
                appConfiguration.SetException("Source file was not found", filename);
            }
        }
    }

    public void generatePrimaryListToMerging(List<NamedScanner> namedScannerList, List<T> primaryListToMerging) {
        for (int i = 0; i < namedScannerList.size();) {
            T item = getNextItemFromScanner(namedScannerList.get(i));

            if (item != null) {
                primaryListToMerging.add(item);
                i++;
            } else {
                namedScannerList.remove(i);
            }
        }
    }

    public void appendStringToOutputFile(String str) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(appConfiguration.getOutFilename(), true));
            out.write(str);
            out.close();
        } catch (IOException e) {
            appConfiguration.SetException(
                    "Could not be added to the output file", appConfiguration.getOutFilename());
        }
    }

    public T getNextItemFromScanner(NamedScanner namedScanner) {
        T result = null;

        if (tClass == Integer.class) {
            while (namedScanner.getScanner().hasNext()) {
                try {
                    result = tClass.cast(Integer.parseInt(namedScanner.getScanner().next()));
                    break;
                } catch (NumberFormatException e) {
                    appConfiguration.SetException(
                            "Wrong number format", namedScanner.getFilename());
                }
            }
        } else {
            if (namedScanner.getScanner().hasNext()) {
                result = tClass.cast(namedScanner.getScanner().next());
            }
        }

        return result;
    }

    private boolean isOrderIncorrect(T currItem, T nextItem, String filename) {
        if (nextItem == null) {
            return false;
        }

        if (appConfiguration.isASC()) {
            if (currItem.compareTo(nextItem) > 0) {
                appConfiguration.SetException("Incorrect presorting", filename);
                return true;
            }
        } else {
            if (currItem.compareTo(nextItem) < 0) {
                appConfiguration.SetException("Incorrect presorting", filename);
                return true;
            }
        }

        return false;
    }

    public void appendRemainderToOutputFile(T currItem, NamedScanner scanner) {
        T nextItem = getNextItemFromScanner(scanner);
        while (currItem != null) {
            if (nextItem == null) {
                appendStringToOutputFile(String.format("%s", currItem));
            } else {
                appendStringToOutputFile(String.format("%s\n", currItem));
            }

            currItem = nextItem;
            do {
                nextItem = getNextItemFromScanner(scanner);
            } while (isOrderIncorrect(currItem, nextItem, scanner.getFilename()) &&
                    appConfiguration.isSkipUnsorted());
        }
    }

    public void KWayMergeSort() {
        List<NamedScanner> namedScannerList = new ArrayList<>();
        List<T> primaryListToMerging = new ArrayList<>();

        clearOrCreateOutputFile();
        generateNamedScannerList(namedScannerList);
        generatePrimaryListToMerging(namedScannerList, primaryListToMerging);
        if (primaryListToMerging.isEmpty()) {
            return;
        }

        LoserTree<T> loserTree = new LoserTree<>(primaryListToMerging, appConfiguration.isASC());

        int currInd;
        T currItem, nextItem;
        while (loserTree.getTreeSize() > 1) {
            currInd = loserTree.getWinner();
            currItem = loserTree.getLeaf(currInd);

            appendStringToOutputFile(String.format("%s\n", currItem));

            do {
                nextItem = getNextItemFromScanner(namedScannerList.get(currInd));
            } while (isOrderIncorrect(currItem, nextItem, namedScannerList.get(currInd).getFilename()) &&
                    appConfiguration.isSkipUnsorted());

            if (nextItem != null) {
                loserTree.setLeaf(nextItem, currInd);
            } else {
                loserTree.removeWinner();
                namedScannerList.remove(currInd);
            }
        }

        appendRemainderToOutputFile(loserTree.getLeaf(loserTree.getWinner()),
                namedScannerList.get(loserTree.getWinner()));
    }

}
