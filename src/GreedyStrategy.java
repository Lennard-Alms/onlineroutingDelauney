import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

class GreedyStrategy implements IAlgorithm {
  protected Animator animator;
  protected Vertex start = null;
  protected Vertex target = null;

  public GreedyStrategy() {}

  public GreedyStrategy(Vertex s, Vertex t, Animator animator) {
    setStart(s);
    setTarget(t);
    setAnimator(animator);
  }

  public void setStart(Vertex s) {
    start = s;
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
    Vertex current = start;
    List<Vertex> path = new ArrayList<>();
    path.add(current);
    while(!current.equals(target)) {
      current = step(current);
      path.add(current);
    }
    return path;
  }

  public Vertex step(Vertex current) {
    Vertex bestVertex = current;
    for(Vertex v : current.neighbours) {
      Vertex v_t = v.sub(target);
      if(bestVertex.distance(target) > v_t.mag()) {
        bestVertex = v;
      }
    }
    return bestVertex;
  }

}
