import java.util.HashSet;
import java.util.ArrayList;


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

    public double distance(Vertex v) {
        double dist = sub(v).mag();
        if(this.isHighway && v.isHighway && dist!= 0.0){
            return 0.000000001;
        }
        return dist;
    }

    public Vertex sub(Vertex vertex) {
        return new Vertex(this.x - vertex.x, this.y - vertex.y);
    }

    public Vertex add(Vertex vertex) {
        return new Vertex(this.x + vertex.x, this.y + vertex.y);
    }

    public Vertex mult(double scalar) {
        return new Vertex(this.x * scalar, this.y * scalar);
    }

    public double mag() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public double dot(Vertex vertex) {
        return this.x * vertex.x + this.y * vertex.y;
    }

    public double cross(Vertex vertex) {
        return this.y * vertex.x - this.x * vertex.y;
    }

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
