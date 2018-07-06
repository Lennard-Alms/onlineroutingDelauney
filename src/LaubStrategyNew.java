import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

class LaubStrategyNew implements IAlgorithm {
  private Animator animator;
  private Vertex start = null;
  private Vertex target = null;

  public LaubStrategyNew(Vertex s, Vertex t, Animator animator) {
    setStart(s);
    setTarget(t);
    this.animator = animator;
  }

  public LaubStrategyNew() {}

  public void setStart(Vertex s) {
    start = s;
  }

  public void setTarget(Vertex t) {
    target = t;
  }

  public void setAnimator() {

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
      if(v.equals(target)) { return v; }
      if(v.distance(target) < current.distance(target)){
        double score = Math.pow(getAngle(current,v),3) * v.distance(target) * current.distance(v);
        /*
          System.out.print(Math.pow(getAngle(current,v),3));
          System.out.print(" * ");
          System.out.print(v.distance(target));
          System.out.print(" * ");
          System.out.print(current.distance(v));
          System.out.print(" = ");
          System.out.println(score);
        */
        if(score < bestScore){
          bestScore = score;
          bestVertex = v;
        }
      }
    }
    return bestVertex;
  }

  public double getAngle(Vertex current, Vertex v) {
    Vertex v_ = v.sub(current);
    Vertex t_ = target.sub(current);

    double dot = v_.dot(t_);
    double v_length = v_.distance(new Vertex(.0,.0));
    double t_length = t_.distance(new Vertex(.0,.0));
    double angle = (dot / (v_length * t_length));
    return Math.toDegrees(Math.acos(angle)) / 90;
  }
}
