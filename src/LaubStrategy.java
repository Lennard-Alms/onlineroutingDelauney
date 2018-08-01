import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

class LaubStrategy implements IAlgorithm {
  protected Animator animator;
  protected Vertex start = null;
  protected Vertex target = null;

  public LaubStrategy() {}

  public LaubStrategy(Vertex s, Vertex t, Animator animator) {
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
      //if(v.equals(target)) { return v; }
      Vertex v_t = v.sub(target);
      if(v_t.mag() < current.distance(target)) {
        double score = calculateScore(current, v);
        if(score < bestScore) {
          bestScore = score;
          bestVertex = v;
        }
      }
    }
    return bestVertex;
  }

  protected double calculateScore(Vertex current, Vertex v) {
    double angle = Geometry.calculateAngle(v, target, current) / 90;
    Vertex v_t = v.sub(target);
    return Math.pow(angle,3) * v_t.mag() * current.distance(v);
  }
}
