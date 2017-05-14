package flowpathfinding;

import java.util.List;

/**
 * Created by Maciek on 14.05.2017.
 */
public class FlowPathResult {
    //Jeśli > 0, nie udało się znaleźć ścieżek bez przekroczenia przepustowości wezlow
    private final int unitsLeft;
    private final List<FlowPath> flowPaths;

    public FlowPathResult(int unitsLeft, List<FlowPath> flowPaths) {
        this.unitsLeft = unitsLeft;
        this.flowPaths = flowPaths;
    }

    public int getUnitsLeft() {
        return unitsLeft;
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
