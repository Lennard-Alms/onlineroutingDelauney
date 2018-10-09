import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Implementierung von Dijkstra auf der Delaunay Triangulation
 */

class OptimalStrategy implements IAlgorithm {
  protected Animator animator;
  protected Vertex start = null;
  protected Vertex target = null;
  protected PriorityQueue<Vertex> q = new PriorityQueue<>(11, new VertexComparator());
  protected Hashtable<Vertex, Vertex> p = new Hashtable<>();
  protected Graph G = null;
  protected HashSet<Vertex> R = null;

  public OptimalStrategy() {}

  public OptimalStrategy(Vertex s, Vertex t, Animator animator, Graph G) {
    setStart(s);
    setTarget(t);
    setAnimator(animator);
    setGraph(G);
  }

  public void setStart(Vertex s) {
    start = s;
  }

  public void setGraph(Graph G) {
    this.G = G;
    R = new HashSet<>(G.V);
  }

  public void setTarget(Vertex t) {
    target = t;
  }

  public void setAnimator(Animator animator) {
    this.animator = animator;
  }

  public Animator getAnimator() {
    return animator;
  }

  public List<Vertex> run() {
    for(Vertex v : G.V) {
        v.l = Double.POSITIVE_INFINITY;
    }
    start.l = 0;
    q.add(start);

    while(R.size() > 0) {
      Vertex v = 	q.poll();
      while(!R.contains(v)) {
        v = q.poll();
      }
      R.remove(v);

      for(Vertex w : v.neighbours) {
        if(R.contains(w)) {
          if(w.l > v.l + v.distance(w)) {
            w.l = v.l + v.distance(w);
            q.add(w);
            p.remove(w);
            p.put(w, v);
          }
        }
      }
      
    }

    List<Vertex> path = new ArrayList<>();
    Vertex current = target;
    path.add(0, current);

    while(!current.equals(start)) {
      Vertex next = step(current);
      path.add(next);
      current = next;
    }

    return path;
  }

  public Vertex step(Vertex current) {
    return p.get(current);
  }

}
