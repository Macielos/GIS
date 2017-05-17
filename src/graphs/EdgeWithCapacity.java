package graphs;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * Created by Maciek on 14.05.2017.
 */
public class EdgeWithCapacity extends DefaultWeightedEdge {

    private int capacity;

    public EdgeWithCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Integer getEdgeSource() {
        return (Integer) this.getSource();
    }

    public Integer getEdgeTarget() {
        return (Integer) this.getTarget();
    }

    @Override
    public String toString() {
        return "[" + this.getSource() + " : " + this.getTarget() + "]";
    }
}
