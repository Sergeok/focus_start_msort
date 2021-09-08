package kwaymergesort;

import exeption.ExceptionHandler;
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
            new PrintWriter(appConfiguration.outFilename).close();
        } catch (FileNotFoundException e) {
            ExceptionHandler.SetException(appConfiguration.errorInfo,
                    "The output file could not be created", appConfiguration.outFilename);
        }
    }

    private void generateNamedScannerList(List<NamedScanner> namedScannerList) {
        for (String filename : appConfiguration.srcList) {
            try {
                namedScannerList.add(new NamedScanner(filename, new Scanner(new File(filename))));
            } catch (FileNotFoundException e) {
                ExceptionHandler.SetException(appConfiguration.errorInfo,
                        "Source file was not found", filename);
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
            BufferedWriter out = new BufferedWriter(new FileWriter(appConfiguration.outFilename, true));
            out.write(str );
            out.close();
        } catch (IOException e) {
            ExceptionHandler.SetException(appConfiguration.errorInfo,
                    "Could not be added to the output file", appConfiguration.outFilename);
        }
    }

    public T getNextItemFromScanner(NamedScanner namedScanner) {
        T result = null;

        if (tClass == String.class) {
            if (namedScanner.getScanner().hasNext()) {
                result = tClass.cast(namedScanner.getScanner().next());
            }
        } else if (tClass == Integer.class) {
            while (namedScanner.getScanner().hasNext()) {
                try {
                    result = tClass.cast(Integer.parseInt(namedScanner.getScanner().next()));
                    break;
                } catch (NumberFormatException e) {
                    ExceptionHandler.SetException(appConfiguration.errorInfo,
                            "Wrong number format", namedScanner.getFilename());
                }
            }
        }

        return result;
    }

    private boolean isOrderIncorrect(T currItem, T nextItem, String filename) {
        if (nextItem == null) {
            return false;
        }

        if (appConfiguration.isSortDirectionAsc) {
            if (currItem.compareTo(nextItem) > 0) {
                ExceptionHandler.SetException(appConfiguration.errorInfo,
                        "Incorrect presorting", filename);
                return true;
            }
        } else {
            if (currItem.compareTo(nextItem) < 0) {
                ExceptionHandler.SetException(appConfiguration.errorInfo,
                        "Incorrect presorting", filename);
                return true;
            }
        }

        return false;
    }

    public void appendRemainderToOutputFile(T currItem, NamedScanner scanner) {
        T nextItem = getNextItemFromScanner(scanner);
        while (currItem != null) {
            if (nextItem == null) {
                appendStringToOutputFile(currItem.toString());
            } else {
                appendStringToOutputFile(currItem.toString() + "\n");
            }

            currItem = nextItem;
            do {
                nextItem = getNextItemFromScanner(scanner);
            } while (isOrderIncorrect(currItem, nextItem, scanner.getFilename()));
        }
    }

    public void KWayMergeSort() {
        List<NamedScanner> namedScannerList = new ArrayList<>();
        List<T> primaryListToMerging = new ArrayList<>();

        clearOrCreateOutputFile();
        generateNamedScannerList(namedScannerList);
        generatePrimaryListToMerging(namedScannerList, primaryListToMerging);

        LoserTree<T> loserTree = new LoserTree<>(primaryListToMerging, appConfiguration.isSortDirectionAsc);

        int currInd;
        T currItem, nextItem;
        while (loserTree.getTreeSize() > 1) {
            currInd = loserTree.getWinner();
            currItem = loserTree.getLeaf(currInd);

            appendStringToOutputFile(currItem.toString() + "\n");

            do {
                nextItem = getNextItemFromScanner(namedScannerList.get(currInd));
            } while (isOrderIncorrect(currItem, nextItem, namedScannerList.get(currInd).getFilename()));

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
