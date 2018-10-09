import java.util.Comparator;

/**
 * Implementierung einer Vergleichsfunktion für Vertices bezüglich Ihrer Dijkstra Sortierung in der Priority Queue
 */

public class VertexComparator implements Comparator<Vertex>{
  public int compare(Vertex v, Vertex u)
    {
      return (int) Math.signum(v.l - u.l);
    }
}
