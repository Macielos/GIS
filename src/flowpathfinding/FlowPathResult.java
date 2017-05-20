package flowpathfinding;

import java.util.List;

/**
 * Created by Maciek on 14.05.2017.
 * Klasa reprezentujaca wynik dzialania algorytmu SPE/LPE
 */
public class FlowPathResult {
    /* jeśli > 0, nie udało się znaleźć ścieżek bez przekroczenia przepustowości wezlow */

    private final int unitsLeft;
    private final List<FlowPath> flowPaths;
    private final long timeRequired;

    public FlowPathResult(int unitsLeft, List<FlowPath> flowPaths, long timeRequired) {
        this.unitsLeft = unitsLeft;
        this.flowPaths = flowPaths;
        this.timeRequired = timeRequired;
    }

    public int getUnitsLeft() {
        return unitsLeft;
    }

    public long getTime() {
        return timeRequired;
    }


    public List<FlowPath> getFlowPaths() {
        return flowPaths;
    }

    @Override
    public String toString() {
        return "FlowPathResult{" +
                "unitsLeft=" + unitsLeft +
                ", flowPaths=" + flowPaths +
                '}';
    }
}
