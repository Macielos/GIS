package flowpathfinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciek on 14.05.2017.
 * Klasa reprezentujaca pojedyncza sciezke od s do t i liczbe przesylanych nia jednostek w wyniku dzialania algorytmu
 * SPE/LPE
 */
public class FlowPath {

    private final List<Integer> path;
    private final int unitCount;

    public FlowPath(List<Integer> path, int unitCount) {
        this.path = path;
        this.unitCount = unitCount;
    }

    public List<Integer> getPath() {
        return path;
    }

    public int getUnitCount() {
        return unitCount;
    }

    @Override
    public String toString() {
        return "FlowPath{" +
                "path=" + path +
                ", unitCount=" + unitCount +
                '}';
    }
}
