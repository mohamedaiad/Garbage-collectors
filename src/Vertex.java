import java.util.ArrayList;
import java.util.List;

// class to make define vertices and make graph
public class Vertex {
    private String name;
    private boolean visited;
    private List<Vertex> adjacent;

    // constructor
    public Vertex(String name){
        this.name = name;
        this.visited = false;
        this.adjacent = new ArrayList<>();
    }

    // adjacent nodes
    public void addNeighbour(Vertex vertex){
        this.adjacent.add(vertex);
    }

    public List<Vertex> getAdjacent(){
        return this.adjacent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isVisited() {
        return visited;
    }

    // mark as visited
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
