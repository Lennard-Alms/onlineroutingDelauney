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


/**
 * Die Graphenklasse enthält alle Eigenschaften über den zu routenden Graphen.<br>
 * Hier werden alle Knoten, Kanten und der Highway gespeichert, die Triangulierung erstellt
 * und Methoden zum Routing bereitgestellt.<br>
 * <br>
 * Durch das Strategy Pattern der Routing Algorithmen können dem Graphen verschiedene
 * Algorithmen zur Berechnung des Routings übergeben und ausgeführt werden.<br>
 */

public class Graph {

    public List<Vertex> vList = new ArrayList<>();
    public HashSet<Vertex> V = new HashSet<>();
    public Vertex[] highway = null;
    private IAlgorithm onlineStrategy = null;

    public Graph() {

    }

    /**
     * Übernimmt den Routing Algorithmus, welcher zur Pfadberechnung verwendet werden soll
     * @method setOnlineStrategy
     * @param  IAlgorithm        strategy
     */
    public void setOnlineStrategy(IAlgorithm strategy) {
      onlineStrategy = strategy;
    }

    /**
     * Fügt einen neuen Punkt zum Graphen hinzu und berechnet eine neue Triangulation
     * @method addVertex
     * @param  Vertex    v
     */
    public void addVertex(Vertex v) {
        if(!V.contains(v)){
            V.add(v);
            vList.add(v);
            calculateTriangulation();
        }
    }

    /**
     * Fügt einen neuen Punkt zum Graphen hinzu und berechnet eventuell eine neue Triangulation
     * @method addVertex
     * @param  Vertex    v
     * @param  Boolean   calcTriang Flag, ob Triangulation neu berechnet werden muss
     */
    public void addVertex(Vertex v, Boolean calcTriang) {
        if(!V.contains(v)){
            V.add(v);
            vList.add(v);
            if(calcTriang) {
                calculateTriangulation();
            }
        }
    }

    /**
     * Fügt eine neue Kante (v,u) hinzu und markiert diese als Highway
     * @method setHighway
     * @param  Vertex     v
     * @param  Vertex     u
     */
    public void setHighway(Vertex v, Vertex u) {
        if(V.contains(v) && V.contains(v)) {
            v.isHighway = true;
            u.isHighway = true;
            u.addNeighbour(v);
            v.addNeighbour(u);
            this.highway = new Vertex[]{v,u};
        }
    }

    /**
     * Gibt die 2 Vertices zurück, die den Highway beschreiben
     * @method getHighway
     */
    public Vertex[] getHighway() {
        return this.highway;
    }

    /**
     * Berechnet 2 Vertices, deren Kante den Highway beschreiben
     * @method calculateHighway
     */
    public void calculateHighway() {
        LaubStrategy laubStrategy = new LaubStrategy();
        for(Vertex v : vList) {
            v.totalCount = 0;
        }
        for(Vertex v : vList) {
            laubStrategy.setTarget(v);
            ArrayList<Vertex> sweepStructure = new ArrayList<>(vList);
            sweepStructure.sort(new VertexDistComparator(v));
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

    /**
     * Berechnet die Delaunay Triangulation auf der Punktmenge des Graphen
     * @method calculateTriangulation
     */
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

    /**
     * Fügt die Kante (v,w) zum Graphen hinzu
     * @method addEdge
     * @param  Vertex  v
     * @param  Vertex  w
     */
    public void addEdge(Vertex v, Vertex w) {
        if(V.contains(v) && V.contains(w)) {
            v.addNeighbour(w);
            w.addNeighbour(v);
        }
    }

    /**
     * Löscht alle Kanten und Punkte, sowie den Highway. Der Graph ist danach leer.
     * @method clear
     */
    public void clear() {
        vList.clear();
        V.clear();
        highway = null;
    }

    /**
     * Führt die aktuelle Routing Strategy auf die bestehende Triangulation der Punktemenge aus
     * @method route
     * @return Pfad von s nach t
     */
    public List<Vertex> route() {
      return onlineStrategy.run();
    }

    @Override
    public String toString() {
        return "Vertices[" + V + "]";
    }

}
