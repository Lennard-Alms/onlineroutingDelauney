import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

/**
 * Routing Strategie für Chew's Algorithmus
 */

class ChewStrategy implements IAlgorithm {
  protected Animator animator;
  protected Vertex start = null;
  protected Vertex target = null;

  /**
   * Konstruktor
   * @method ChewStrategy
   */
  public ChewStrategy() {}

  /**
   * Konstruktor
   * @method ChewStrategy
   * @param  Vertex       s             Start Knoten
   * @param  Vertex       t             Target Knoten
   * @param  Animator     animator      Animator Objekt um Animationen zu speichern
   */
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

  /**
   * Führt eine komplette Pfadberechnung von start zu target aus.
   * @method run
   * @return [description]
   */
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

  /**
   * Führt die Berechnung des nächsten Pfadsegments mit current als Startknoten durch.
   * @method step
   * @param  Vertex current       Knoten, der die aktuelle Position beschreibt.
   * @return                      Nächster Knoten auf dem Pfad
   */
  public Vertex step(Vertex current) {
    Vertex x = null;
    Vertex y = null;

    /**
     * Berechnet für alle Nachbarknoten, ob diese sich schneiden und prüft,
     * ob es sich bei den Knoten um das rechteste Dreieck nach Chew's Algorithmus handelt,
     * welches mit der Geraden von start nach target einen Schnittpunkt hat.
     */
    for(Vertex v : current.nList) {
      if(v.equals(target)) {
        return target;
      }
      for(Vertex w : current.nList) {
        if(v.neighbours.contains(w)) {
          if(intersects(start, target, v, w)) {
            /**
             * Prüfe ob es sich um das rechteste Dreieck handelt.
             */
            if(x == null) {
              x = v;
              y = w;
            } else if (Geometry.getDistanceFromPointToIntersection(start, target, x, y) <
                      Geometry.getDistanceFromPointToIntersection(start, target, v, w)) {
              x = v;
              y = w;
            }
          }
        }
      }
    }

    /**
     * Wenn 2 Knoten x, y gefunden wurden, für die obige Eigenschaft gilt:
     *  Berechne Kreis um die 3 Punkte current, x, y
     *  Prüfe, ob x oder y das nächste Pfadsegment auf der Route ist.
     */
    if(x != null && y != null) {
      Vertex cc = GetCircumcenter(current, x, y);
      Vertex leftmost = cc.add(start.sub(target).mult(1/start.sub(target).mag()).mult(cc.distance(current)));
      Vertex rightmostInter = findRightIntersect(start,target,cc,current);
      if(Geometry.comparePointToLine(leftmost, rightmostInter, current) == Geometry.comparePointToLine(leftmost, rightmostInter, x) && Geometry.comparePointToLine(leftmost, rightmostInter, current) != Geometry.comparePointToLine(leftmost, rightmostInter, y)) {
          current = x;
      } else if(Geometry.comparePointToLine(leftmost, rightmostInter, current) != Geometry.comparePointToLine(leftmost, rightmostInter, x) && Geometry.comparePointToLine(leftmost, rightmostInter, current) == Geometry.comparePointToLine(leftmost, rightmostInter, y)) {
          current = y;
      } else {
        if(x.distance(target) < y.distance(target)) {
          current = x;
        } else {
          current = y;
        }
      }
      return current;
    } else {
      current = target;
      return current;
    }
  }

    /**
     * Berechnet den rechten Schnittpunkt zwischen einem Kreis und der Geraden von s nach t
     * "Rechter Schnittpunkt" heißt, dass der Schnittpunkt, der projiziert auf die Gerade von s nach t
     * näher an t liegt, gewählt wird.
     * @method findRightIntersect
     * @param  Vertex             s             Start Knoten
     * @param  Vertex             t             Target Knoten
     * @param  Vertex             cc            Circumcenter
     * @param  Vertex             current       Aktuelle Position
     * @return
     */
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
