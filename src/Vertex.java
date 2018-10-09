import java.util.HashSet;
import java.util.ArrayList;

/**
 * Ein zweidimensionaler Punkt mit zusätzlichen Eigenschaften
 */

public class Vertex {
    public ArrayList<Vertex> nList = new ArrayList<>();
    public HashSet<Vertex> neighbours = new HashSet<>();
    public double x;
    public double y;
    public double l;
    public int count;
    public int totalCount;
    public Boolean isHighway = false;

    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void addNeighbour(Vertex v) {
        neighbours.add(v);
        nList.add(v);
    }

    /**
     * Berechnet die Distanz zwischen 2 Vertices. Ein Endpunkt des Highway hat die Länge "ungefähr 0"
     * @method distance
     */
    public double distance(Vertex v) {
        double dist = sub(v).mag();
        if(this.isHighway && v.isHighway && dist!= 0.0){
            return 0.000000001;
        }
        return dist;
    }

    /**
     * Subtrahiert den übergebenen Vertex von sich selbst.
     * @method sub
     */
    public Vertex sub(Vertex vertex) {
        return new Vertex(this.x - vertex.x, this.y - vertex.y);
    }

    /**
     * Addiert den übergebenen Vertex auf sich selbst.
     * @method add
     */
    public Vertex add(Vertex vertex) {
        return new Vertex(this.x + vertex.x, this.y + vertex.y);
    }

    /**
     * Multipliziert den Vertex mit einem Skalar
     * @method mult
     */
    public Vertex mult(double scalar) {
        return new Vertex(this.x * scalar, this.y * scalar);
    }

    /**
     * Berechnet die Länge des Vertex
     * @method mag
     */
    public double mag() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**
     * Berechnet das Skalarprodukt von 2 Vektoren
     * @method dot
     */
    public double dot(Vertex vertex) {
        return this.x * vertex.x + this.y * vertex.y;
    }

    /**
     * Berechnet das Kreuzprodukt von 2 Vektoren
     * @method cross
     */
    public double cross(Vertex vertex) {
        return this.y * vertex.x - this.x * vertex.y;
    }

    /**
     * Gibt zurück, ob Vertex Endpunkt des Highway ist
     * @method isHighway
     */
    public Boolean isHighway() {
      return this.isHighway;
    }

    public String toString() {
        String neig = "";
        for(Vertex v : neighbours){
            neig += "(" + v.x + "," + v.y + ")-";
        }
        return "vertex[ x=" + x + ", y=" + y + " -> " + neig + "]";
    }

}
