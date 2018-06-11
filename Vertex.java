import java.util.Hashtable;

public class Vertex {

    public Hashtable<Integer, Vertex> neighbours = new Hashtable<>();
    public Vector2D vector = null;
    public double l = Double.POSITIVE_INFINITY;

    public Vertex(Vector2D v) {

        this.vector = v;

    }

    public void addNeighbour(int key, Vertex v){
        neighbours.put(key, v);
    }

    public double getDistance(Vertex v){
        Vector2D d = this.vector.sub(v.vector);
        return Math.sqrt(d.dot(d));
    }
    public double getX(){
        return vector.x;
    }

    public double getY(){
        return vector.y;
    }

    public String toString() {
        return "vertex[ x=" + getX() + ", y=" + getY() + " -> " + neighbours.keySet() + "]";
    }

}
