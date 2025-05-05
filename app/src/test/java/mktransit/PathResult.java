package mktransit;

import java.util.List;

public class PathResult {
    private List<String> fullPath; //ทุก ID ที่ผ่าน
    private List<String> importantSteps; //จุดสำคัญ คือ Interchange
    private int totalTime; //เวลารวม

    //Constructore
    public PathResult(List<String> fullPath, List<String> importantSteps, int totalTime) {
        this.fullPath = fullPath;
        this.importantSteps = importantSteps;
        this.totalTime = totalTime;
    }

    //Method
    public List<String> getFullPath() {
        return fullPath;
    }

    public List<String> getImportantSteps() {
        return importantSteps;
    }

    public int getTotalTime() {
        return totalTime;
    }
}
