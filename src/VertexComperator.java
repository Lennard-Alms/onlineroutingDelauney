import java.util.Comparator;

public class VertexComperator implements Comparator<Vertex>{
  public int compare(Vertex v, Vertex u)
    {
      return (int) Math.signum(v.l - u.l);
    }
}
