import java.util.Hashtable;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;
import java.lang.Math;



import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;




public class Graph {

    public List<Vertex> vList = new ArrayList<>();
    public HashSet<Vertex> V = new HashSet<>();
    public Vertex[] highway = null;
    private IAlgorithm onlineStrategy = null;

    public Graph() {

    }

    public void setOnlineStrategy(IAlgorithm strategy) {
      onlineStrategy = strategy;
    }

    public void addVertex(Vertex v) {
        if(!V.contains(v)){
            V.add(v);
            vList.add(v);
            calculateTriangulation();
        }
    }

    public void addVertex(Vertex v, Boolean calcTriang) {
        if(!V.contains(v)){
            V.add(v);
            vList.add(v);
            if(calcTriang) {
                calculateTriangulation();
            }
        }
    }

    public void setHighway(Vertex v, Vertex u) {
        if(V.contains(v) && V.contains(v)) {
            v.isHighway = true;
            u.isHighway = true;
            u.addNeighbour(v);
            v.addNeighbour(u);
            this.highway = new Vertex[]{v,u};
        }
    }

    public Vertex[] getHighway() {
        return this.highway;
    }

    public void calculateHighway() {
        LaubStrategy laubStrategy = new LaubStrategy();
        for(Vertex v : vList) {
            v.totalCount = 0;
        }
        for(Vertex v : vList) {
            laubStrategy.setTarget(v);
            ArrayList<Vertex> sweepStructure = new ArrayList<>(vList);
            sweepStructure.sort(new VertexDistComperator(v));
            for(Vertex u : sweepStructure) {
                u.count = 0;
            }
            for(Vertex u : sweepStructure) {
                if(u.equals(v)) break;
                Vertex next = laubStrategy.step(u);
                next.count += u.count + 1;
                next.totalCount += u.count + 1;
            }
        }
        Vertex highwayStart = vList.get(0);
        Vertex highwayEnd = vList.get(1);
        for(Vertex v : vList) {
            if(v.totalCount > highwayStart.totalCount) {
                highwayEnd = highwayStart;
                highwayStart = v;
            } else if(v.totalCount > highwayEnd.totalCount) {
                highwayEnd = v;
            }
        }
        setHighway(highwayStart, highwayEnd);
    }

    public void calculateTriangulation() {
        for(Vertex v : V) {
            v.neighbours.clear();
            v.nList.clear();
            v.isHighway = false;
        }
        if(V.size() > 2) {
            DelaunayTriangulator triangulator = new DelaunayTriangulator(vList);
            triangulator.triangulate();
            List<Triangle2D> triangles = triangulator.getTriangles();
            for(Triangle2D tri : triangles) {
                addEdge(tri.a, tri.b);
                addEdge(tri.b, tri.c);
                addEdge(tri.a, tri.c);
            }
        } else if(V.size() == 2) {
            addEdge(vList.get(0), vList.get(1));
        }
    }

    public void addEdge(Vertex v, Vertex w) {
        if(V.contains(v) && V.contains(w)) {
            v.addNeighbour(w);
            w.addNeighbour(v);
        }
    }

    public void clear() {
        vList.clear();
        V.clear();
        highway = null;
    }

    public List<Vertex> route() {
      return onlineStrategy.run();
    }

    @Override
    public String toString() {
        return "Vertices[" + V + "]";
    }

}
