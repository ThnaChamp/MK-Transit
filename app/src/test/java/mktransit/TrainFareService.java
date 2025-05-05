package mktransit;

import java.util.Map;

public class TrainFareService {
    private final Map<String, Map<String, Integer>> fareTable;

    public TrainFareService(Map<String, Map<String, Integer>> fareTable) {
        this.fareTable = fareTable;
    }

    public Integer findFare(String from, String to) {
        if (!fareTable.containsKey(from)) return null;
        Map<String, Integer> destMap = fareTable.get(from);
        return destMap.getOrDefault(to, null);
    }
}