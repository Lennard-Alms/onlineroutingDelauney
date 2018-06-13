import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Hashtable;



public class Test {

  public static void main(String[] args) {
    Graph G = new Graph();
    G.addVertex(new Vertex(1.0,1.0));
    G.addVertex(new Vertex(3.0,4.0));
    G.addVertex(new Vertex(6.0,5.0));
    G.addVertex(new Vertex(2.0,4.0));
    G.addVertex(new Vertex(9.0,10.0));
    G.addVertex(new Vertex(17.0,12.0));
    G.addVertex(new Vertex(5.0,8.0));
    G.addVertex(new Vertex(14.0,2.0));
    G.calculateTriangulation();

  }

}
