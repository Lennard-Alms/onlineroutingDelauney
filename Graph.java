import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;

/**
 * 2D vector class implementation.
 *
 * @author Johannes Diemke
 */
public class Graph {

    public Hashtable<Integer, Vertex> V = new Hashtable<>();

    /**
     * Constructor of the Graph class used to create new Graph instances.
     *
     * @param vertices
     *            List of vertices that contains Objects of class Vertex
     */
    public Graph() {

    }

    public void addVertex(Vector2D v){
        Vertex vertex = new Vertex(v);
        V.put(V.size(), vertex);
    }

    public void addEdge(int v, int w){
        if(V.containsKey(v) && V.containsKey(w)){
            V.get(v).addNeighbour(w, V.get(w));
            V.get(w).addNeighbour(v, V.get(v));
        }
    }

    public List<Vector2D> greedyRoutingPath(){
        Vertex current = V.get(0);
        List<Vector2D> path = new ArrayList<>();
        path.add(current.vector);
        Vertex best = current;
        while(!current.equals(V.get(1))){
            for(Vertex v : current.neighbours.values()){
                if(best.getDistance(V.get(1)) > v.getDistance(V.get(1))){
                    best = v;
                }
            }
            path.add(best.vector);
            current = best;
        }
        return path;
    }


    @Override
    public String toString() {
        return "Vertices[" + V + "]";
    }

}
