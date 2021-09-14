import entity.DataType;
import kwaymergesort.CustomMergeSort;
import entity.AppConfiguration;

public class MSortApp {

    public static void main(String[] args) {
        AppConfiguration appConfiguration = new AppConfiguration(args);
        if (appConfiguration.isSettingsIncorrect()) {
            return;
        }

        CustomMergeSort<?> customMergeSort;
        if (appConfiguration.getDataType() == DataType.STRING) {
            customMergeSort = new CustomMergeSort<>(String.class, appConfiguration);
        } else {
            customMergeSort = new CustomMergeSort<>(Integer.class, appConfiguration);
        }
        customMergeSort.KWayMergeSort();

        appConfiguration.PrintAllExceptions();
    }

}