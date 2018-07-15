import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

class ChewStrategy implements IAlgorithm {
  protected Animator animator;
  protected Vertex start = null;
  protected Vertex target = null;

  public ChewStrategy() {}

  public ChewStrategy(Vertex s, Vertex t, Animator animator) {
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
    List<Vertex> path = new ArrayList<>();
    Vertex current = start;
    path.add(current);
    int i = 0;
    while(!current.equals(target) && i < 1000) {
      i++;
      current = step(current);
      path.add(current);
    }
    return path;
  }

  public Vertex step(Vertex current) {
    Vertex x = null;
    Vertex y = null;
    for(Vertex v : current.nList) {
      if(v.equals(target)) {
        return target;
      }
      for(Vertex w : current.nList) {
        if(v.neighbours.contains(w)) {
          if(intersects(start,target,v,w)) {
            if(x == null) {
              x = v;
              y = w;
            } else if(Geometry.getDistanceFromPointToIntersection(start,target,x,y) < Geometry.getDistanceFromPointToIntersection(start,target,v,w)) {
              x = v;
              y = w;
            }
          }
        }
      }
    }
    if(x != null && y != null) {
      Vertex cc = GetCircumcenter(current, x, y);
      Vertex leftmost = cc.add(start.sub(target).mult(1/start.sub(target).mag()).mult(cc.distance(current)));
      Vertex rightmostInter = findRightIntersect(start,target,cc,current);
      if(Geometry.comparePointToLine(leftmost, rightmostInter, current) == Geometry.comparePointToLine(leftmost, rightmostInter, x)) {
          current = x;
      } else {
          current = y;
      }
      return current;
    } else {
      current = target;
      return current;
    }
  }

  public Vertex findRightIntersect(Vertex s, Vertex t, Vertex cc, Vertex current) {
        Vertex sToT = t.sub(s);
        Vertex sToCC = cc.sub(s);
        double cp = sToCC.dot(sToT)/sToT.dot(sToT);
        Vertex proj = sToT.mult(cp);
        double d = proj.distance(sToCC);
        double r = cc.distance(current);
        double plus = Math.sqrt(r*r - d*d);
        Vertex dest = proj.add(sToT.mult(1/sToT.mag()).mult(plus));
        return s.add(dest);
    }

    public double Slope(Vertex from, Vertex to) {
  		return (to.y - from.y) / (to.x - from.x);
  	}

    public Vertex Midpoint(Vertex a, Vertex b) {
  		// midpoint is the average of x & y coordinates
  		return new Vertex(
  			(a.x + b.x) / 2,
  			(a.y + b.y) / 2
  		);
  	}

    public Vertex GetCircumcenter(Vertex a, Vertex b, Vertex c) {
  		// determine midpoints (average of x & y coordinates)
  		Vertex midAB = Midpoint(a, b);
  		Vertex midBC = Midpoint(b, c);

  		// determine slope
  		// we need the negative reciprocal of the slope to get the slope of the perpendicular bisector
  		double slopeAB = -1 / Slope(a, b);
  		double slopeBC = -1 / Slope(b, c);

  		// y = mx + b
  		// solve for b
  		double bAB = midAB.y - slopeAB * midAB.x;
  		double bBC = midBC.y - slopeBC * midBC.x;

  		// solve for x & y
  		// x = (b1 - b2) / (m2 - m1)
  		double x = (bAB - bBC) / (slopeBC - slopeAB);
  		Vertex circumcenter = new Vertex(
  			x,
  			(slopeAB * x) + bAB
  		);

  		return circumcenter;
  	}

    boolean intersects(Vertex p1, Vertex p2, Vertex p3, Vertex p4) {
      double x1 = p1.x;
      double x2 = p2.x;
      double x3 = p3.x;
      double x4 = p4.x;
      double y1 = p1.y;
      double y2 = p2.y;
      double y3 = p3.y;
      double y4 = p4.y;

      double bx = x2 - x1;
      double by = y2 - y1;
      double dx = x4 - x3;
      double dy = y4 - y3;
      double b_dot_d_perp = bx * dy - by * dx;

      if (b_dot_d_perp == 0) {
        return false;
      }
      double cx = x3 - x1;
      double cy = y3 - y1;
      double t = (cx * dy - cy * dx) / b_dot_d_perp;
      if (t < 0 || t > 1) {
        return false;
      }
      double u = (cx * by - cy * bx) / b_dot_d_perp;
      if (u < 0 || u > 1) {
        return false;
      }
      return true;
    }

}
