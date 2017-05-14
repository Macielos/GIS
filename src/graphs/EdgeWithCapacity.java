package graphs;

import org.jgrapht.graph.DefaultEdge;

/**
 * Created by Maciek on 14.05.2017.
 */
public class EdgeWithCapacity extends DefaultEdge {

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

    @Override
    public String toString() {
        return "EdgeWithCapacity{" +
                "capacity=" + capacity +
                '}';
    }
}
