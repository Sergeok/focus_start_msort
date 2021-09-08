package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AppConfiguration {

    public Boolean isSortDirectionAsc = true;
    public Boolean isDataTypeString = null;
    public String outFilename = null;
    public List<String> srcList = new ArrayList<>();
    public HashMap<String, HashMap<String, Integer>> errorInfo = new HashMap<>();

    public AppConfiguration(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-s":
                    isDataTypeString = true;
                    break;
                case "-i":
                    isDataTypeString = false;
                    break;
                case "-a":
                    isSortDirectionAsc = true;
                    break;
                case "-d":
                    isSortDirectionAsc = false;
                    break;
                default:
                    outFilename = args[i];
                    srcList = Arrays.asList(Arrays.copyOfRange(args, i + 1, args.length));

                    return;
            }
        }
    }

    public boolean isSettingsIncorrect() {
        boolean condition = false;

        if (outFilename == null) {
            System.out.println("Output file name not specified!");
            condition = true;
        }
        if (isDataTypeString == null) {
            System.out.println("Wrong datatype!");
            condition = true;
        }
        if (srcList.size() < 1) {
            System.out.println("No source data file specified!");
            condition = true;
        }

        return condition;
    }

//    public boolean isSortDirectionAsc() {
//        return isSortDirectionAsc;
//    }
//
//    public void setSortDirectionAsc(boolean sortDirectionAsc) {
//        isSortDirectionAsc = sortDirectionAsc;
//    }
//
//    public Boolean getDataTypeString() {
//        return isDataTypeString;
//    }
//
//    public void setDataTypeString(Boolean dataTypeString) {
//        isDataTypeString = dataTypeString;
//    }
//
//    public HashMap<String, HashMap<String, Integer>> getErrorInfo() {
//        return errorInfo;
//    }
//
//    public void setErrorInfo(HashMap<String, HashMap<String, Integer>> errorInfo) {
//        this.errorInfo = errorInfo;
//    }
//
//    public String getOutFilename() {
//        return outFilename;
//    }
//
//    public void setOutFilename(String outFilename) {
//        this.outFilename = outFilename;
//    }
//
//    public List<String> getSrcList() {
//        return srcList;
//    }
//
//    public void setSrcList(List<String> srcList) {
//        this.srcList = srcList;
//    }

}
