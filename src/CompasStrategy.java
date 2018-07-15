import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

class CompasStrategy implements IAlgorithm {
  protected Animator animator;
  protected Vertex start = null;
  protected Vertex target = null;

  public CompasStrategy() {}

  public CompasStrategy(Vertex s, Vertex t, Animator animator) {
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
    Vertex bestVertex = null;
    double bestScore = Double.POSITIVE_INFINITY;
    for(Vertex v : current.neighbours) {
      double score = Geometry.calculateAngle(v, target, current) / 90;
      if(score < bestScore) {
        bestScore = score;
        bestVertex = v;
      }
    }
    return bestVertex;
  }

}
