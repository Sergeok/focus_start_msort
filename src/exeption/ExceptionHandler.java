package exeption;

import java.util.HashMap;
import java.util.Map;

public class ExceptionHandler {

    public static void PrintAllExceptions(HashMap<String, HashMap<String, Integer>> exceptionInfo) {
        if (exceptionInfo.size() > 0) {
            System.out.println("Sorting completed. Resolved exceptions raised during execution:");
            for (Map.Entry<String, HashMap<String, Integer>> entry : exceptionInfo.entrySet()) {
                System.out.println("\t" + entry.getKey() + ":");
                for (Map.Entry<String, Integer> data : entry.getValue().entrySet()) {
                    System.out.println("\t\t" + data.getKey() + " /" + data.getValue() + " times/");
                }
            }
        } else {
            System.out.println("Sorting completed successfully!");
        }
    }

    public static void SetException(HashMap<String,
            HashMap<String, Integer>> errorInfo, String excName, String excSource) {

        if (errorInfo.containsKey(excName)) {
            if (errorInfo.get(excName).containsKey(excSource)) {
                errorInfo.get(excName).put(excSource, errorInfo.get(excName).get(excSource) + 1);
            } else {
                errorInfo.get(excName).put(excSource, 1);
            }
        } else {
            errorInfo.put(excName, new HashMap<>());
            errorInfo.get(excName).put(excSource, 1);
        }
    }

}
