import java.util.Comparator;

public class VertexDistComperator implements Comparator<Vertex>{

  public Vertex target;

  public VertexDistComperator(Vertex target) {
    this.target = target;
  }

  public int compare(Vertex v, Vertex u) {
    return (int) Math.signum(this.target.distance(u) - this.target.distance(v));
  }
}
