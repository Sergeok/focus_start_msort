package entity;

import java.util.*;

public class AppConfiguration {

    private Boolean isSortDirectionAsc = true;
    private DataType dataType = null;
    private String outFilename = null;
    private List<String> srcList = new ArrayList<>();
    private final HashMap<String, HashMap<String, Integer>> exceptionInfo = new HashMap<>();
    private boolean skipUnsorted = true;

    public AppConfiguration(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-s":
                    dataType = DataType.STRING;
                    break;
                case "-i":
                    dataType = DataType.INTEGER;
                    break;
                case "-a":
                    isSortDirectionAsc = true;
                    break;
                case "-d":
                    isSortDirectionAsc = false;
                    break;
                case "-p":
                    skipUnsorted = false;
                    break;
                default:
                    outFilename = args[i];
                    srcList = Arrays.asList(Arrays.copyOfRange(args, i + 1, args.length));
                    return;
            }
        }
    }

    public boolean isSettingsIncorrect() {
        List<String> errorList = new ArrayList<>();

        if (outFilename == null) {
            errorList.add("Output file name not specified!");
        }
        if (dataType == null) {
            errorList.add("Wrong datatype!");
        }
        if (srcList.size() < 1) {
            errorList.add("No source data file specified!");
        }

        if (errorList.isEmpty()) {
            return false;
        } else {
            System.out.println("Sorting failed. Critical errors:");
            for (String error : errorList) {
                System.out.printf("\t%s%n", error);
            }
            return true;
        }
    }

    public boolean isASC() {
        return isSortDirectionAsc;
    }

    public DataType getDataType() {
        return dataType;
    }

    public String getOutFilename() {
        return outFilename;
    }

    public List<String> getSrcList() {
        return srcList;
    }

    public boolean isSkipUnsorted() {
        return skipUnsorted;
    }

    public void SetException(String excName, String excSource) {
        if (exceptionInfo.containsKey(excName)) {
            if (exceptionInfo.get(excName).containsKey(excSource)) {
                exceptionInfo.get(excName).put(excSource, exceptionInfo.get(excName).get(excSource) + 1);
            } else {
                exceptionInfo.get(excName).put(excSource, 1);
            }
        } else {
            exceptionInfo.put(excName, new HashMap<>());
            exceptionInfo.get(excName).put(excSource, 1);
        }
    }

    public void PrintAllExceptions() {
        if (exceptionInfo.size() > 0) {
            System.out.println("Sorting completed. Resolved exceptions raised during execution:");
            for (Map.Entry<String, HashMap<String, Integer>> entry : exceptionInfo.entrySet()) {
                System.out.printf("\t%s:%n", entry.getKey());
                for (Map.Entry<String, Integer> data : entry.getValue().entrySet()) {
                    System.out.printf("\t\t%s /%d times/%n", data.getKey(), data.getValue());
                }
            }
        } else {
            System.out.println("Sorting completed successfully!");
        }
    }

}
