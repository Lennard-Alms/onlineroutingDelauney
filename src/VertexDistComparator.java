import java.util.Comparator;

/**
 * Implementierung einer Vergleichsfunktion für Vertices bezüglich Ihrer Distanz zu einem beliebigen Punkt
 */

public class VertexDistComparator implements Comparator<Vertex>{

  public Vertex target;

  public VertexDistComparator(Vertex target) {
    this.target = target;
  }

  public int compare(Vertex v, Vertex u) {
    return (int) Math.signum(this.target.distance(u) - this.target.distance(v));
  }
}
