import java.util.Hashtable;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Graph {

    public List<Vertex> vList = new ArrayList<>();
    public HashSet<Vertex> V = new HashSet<>();

    public Graph() {

    }

    public void addVertex(Vertex v){
        V.add(v);
        vList.add(v);
        calculateTriangulation();
    }

    public void calculateTriangulation(){
        for(Vertex v : V){
            v.neighbours.clear();
        }
        if(V.size() > 2){
            DelaunayTriangulator triangulator = new DelaunayTriangulator(vList);
            triangulator.triangulate();
            List<Triangle2D> triangles = triangulator.getTriangles();
            for(Triangle2D tri : triangles){
                addEdge(tri.a, tri.b);
                addEdge(tri.b, tri.c);
                addEdge(tri.a, tri.c);
            }
        } else if(V.size() == 2){
            addEdge(vList.get(0), vList.get(1));
        }
    }

    public void addEdge(Vertex v, Vertex w){
        if(V.contains(v) && V.contains(w)){
            v.addNeighbour(w);
            w.addNeighbour(v);
        }
    }

    public void clear(){
        vList.clear();
        V.clear();
    }



    public List<Vertex> laubentahlschesRouting(){
        List<Vertex> path = new ArrayList<>();
        Vertex current = vList.get(0);
        path.add(current);
        System.out.println();
        System.out.println();
        System.out.println();
        while(!current.equals(vList.get(1))) {
            System.out.println("(" + current.x + "," + current.y + ")");
            HashSet<Vertex> candidates = new HashSet<>();
            for(Vertex v : current.neighbours) {
                if(current.distance(vList.get(1)) > v.distance(vList.get(1))) {
                    candidates.add(v);
                }
            }
            Vertex bestDist = current;
            Vertex longestEdge = current;
            for(Vertex v : candidates) {
                if(v.distance(vList.get(1)) < bestDist.distance(vList.get(1))) {
                    bestDist = v;
                    System.out.println("Distance: " + v.distance(vList.get(1)));
                }
                if(current.distance(v) > current.distance(longestEdge)) {
                    System.out.println("Longest Edge: " + current.distance(v));
                    longestEdge = v;
                }
            }
            Vertex best = current;
            if(bestDist.distance(vList.get(1)) != 0) {
                double bestRatio = -1;
                double distNorm;
                double edgeNorm;
                double routingRatio;
                for(Vertex v : candidates) {
                    
                    distNorm = bestDist.distance(vList.get(1)) / v.distance(vList.get(1));
                    System.out.println("Distnorm");
                    System.out.println(bestDist.distance(vList.get(1)) + " / " + v.distance(vList.get(1)) + " = " + distNorm);
                    edgeNorm = current.distance(v) / current.distance(longestEdge);
                    routingRatio = distNorm / (edgeNorm);
                    System.out.println(distNorm + " / " + edgeNorm + " = " + routingRatio);
                    if(routingRatio > bestRatio && bestRatio != 0) {
                        best = v;
                        bestRatio = routingRatio;
                    }
                }
            } else {
              best = bestDist;
            }
            current = best;
            path.add(current);
        }
        return path;
    }

    public List<Vertex> greedyRoutingPath(){
        Vertex current = vList.get(0);
        List<Vertex> path = new ArrayList<>();
        path.add(current);
        Vertex best = current;
        while(!current.equals(vList.get(1))){
            for(Vertex v : current.neighbours){
                if(best.distance(vList.get(1)) > v.distance(vList.get(1))){
                    best = v;
                }
            }
            path.add(best);
            current = best;
        }
        return path;
    }

    public List<Vertex> optimalRoutingPath(){
        for(Vertex v : V){
            v.l = Double.POSITIVE_INFINITY;
        }
        vList.get(0).l = 0;
        PriorityQueue<Vertex> q = new PriorityQueue<>(11, new VertexComperator());
        q.add(vList.get(0));
        HashSet<Vertex> R = new HashSet<>(V);
        Hashtable<Vertex, Vertex> p = new Hashtable<>();
        while(R.size() > 0){
            Vertex v = 	q.poll();
            while(!R.contains(v)){
                v = q.poll();
            }
            R.remove(v);
            for(Vertex w : v.neighbours){
                if(R.contains(w)){
                    if(w.l > v.l + v.distance(w)){
                        w.l = v.l + v.distance(w);
                        q.add(w);
                        p.remove(w);
                        p.put(w, v);
                    }
                }
            }
        }
        List<Vertex> path = new ArrayList<>();
        Vertex v = vList.get(1);
        path.add(0, v);
        while(!v.equals(vList.get(0))){
            Vertex next = p.get(v);
            path.add(next);
            v = next;
        }
        return path;
    }

    @Override
    public String toString() {
        return "Vertices[" + V + "]";
    }

}
