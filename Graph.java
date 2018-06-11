import java.util.Hashtable;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Graph {

    public Hashtable<Integer, Vertex> V = new Hashtable<>();

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

    public List<Vector2D> optimalRoutingPath(){
        V.get(0).l = 0;
        PriorityQueue<Vertex> q = new PriorityQueue<>(11, new VertexComperator());
        q.add(V.get(0));
        HashSet<Vertex> R = new HashSet<>(V.values());
        Hashtable<Vertex, Vertex> p = new Hashtable<>();
        while(R.size() > 0){
            Vertex v = 	q.poll();
            while(!R.contains(v)){
                v = q.poll();
            }
            R.remove(v);
            for(Vertex w : v.neighbours.values()){
                if(R.contains(w)){
                    if(w.l > v.l + v.getDistance(w)){
                        w.l = v.l + v.getDistance(w);
                        q.add(w);
                        p.remove(w);
                        p.put(w, v);
                    }
                }
            }
        }
        List<Vector2D> path = new ArrayList<>();
        Vertex v = V.get(1);
        path.add(v.vector);
        while(!v.equals(V.get(0))){
            Vertex next = p.get(v);
            path.add(next.vector);
            v = next;
        }
        return path;

    }

    @Override
    public String toString() {
        return "Vertices[" + V + "]";
    }

}
