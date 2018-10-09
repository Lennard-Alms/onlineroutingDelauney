import java.util.Hashtable;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;
import java.lang.Math;

/**
 * Die Geometry Class enthält alle Methoden, die zur Berechnung von geometrischen
 * Eigenschaften benötigt werden.
 *
 * Methoden:
 *  Distanzberechnungen,
 *  Winkelberechnungen zwischen 2 Vektoren
 *
 */
public class Geometry {

  /**
   * Gibt aus ob Punkt t unter oder über dem Vektor von l nach r liegt.
   * @method comparePointToLine
   * @param  Vertex             l
   * @param  Vertex             r
   * @param  Vertex             t
   * @return                    1 "darüber" / -1 "darunter"
   */
  public static int comparePointToLine(Vertex l, Vertex r, Vertex t) {
    Vertex lin = r.sub(l); //Vektor von r nach l
    Vertex orth = new Vertex(-lin.y, lin.x);
    double angle = calculateAngle(orth.add(l), t, l);
    if(orth.dot(t.sub(l)) == 0) {
        return 1;
    }
    if(angle <= 90) {
        return 1;
    } else{
        return -1;
    }
  }

  public static double getDistanceFromPointToIntersection(Vertex s,Vertex t,Vertex v,Vertex w) {
    Vertex s_t = t.sub(s);
    Vertex v_w = w.sub(v);
    double x1 = s.x;
    double y1 = s.y;
    double x2 = s_t.x;
    double y2 = s_t.y;
    double x3 = v.x;
    double y3 = v.y;
    double x4 = v_w.x;
    double y4 = v_w.y;
    return (x3 * y4 - x1 * y4 + y1 * x4 - y3 * x4) / (x2 * y4 - y2 * x4);
  }

  /**
   * Berechnet den Winkel zwischen 2 Linien.<br>
   * Es werden Vektoren vom Referenzpunkt zu den beiden übergebenen Vektoren x, y
   * berechnet, zwischen denen dann der Winkel berechnet wird.<br>
   * <br>
   * x, y werden übergeben<br>
   * Vektor referencePoint -> x wird berechnet<br>
   * Vektor referencePoint -> y wird berechnet<br>
   * Winkel zwischen |referencePoint->x| und |referencepoint->y| wird berechnet<br>
   * <br>
   * @method calculateAngle
   * @param  Vertex         x
   * @param  Vertex         y
   * @param  Vertex         referencePoint
   * @param  int            mode           rad = 1 | degree = 0
   * @return                Winkel zwischen |referencePoint->x| und |referencepoint->y|
   */
  public static double calculateAngle(Vertex x, Vertex y, Vertex referencePoint, int mode) {
    if(x.equals(y)){return 0.0;}
    x = x.sub(referencePoint);
    y = y.sub(referencePoint);

    double dot = x.dot(y);
    double length_x = x.distance(new Vertex(.0, .0));
    double length_y = y.distance(new Vertex(.0, .0));
    double angle = (dot / (length_x * length_y));

    if(mode == 0) return Math.toDegrees(Math.acos(angle));
    else return (Math.acos(angle));
  }

  /**
   * Ruft {@link Geometry#calculateAngle(Vertex, Vertex, Vertex, int)} mit mode = 0 auf. Gibt Winkel in degree zurück.
   * @see Geometry#calculateAngle(Vertex, Vertex, Vertex, int)
   */
  public static double calculateAngle(Vertex x, Vertex y, Vertex referencePoint) {
    return calculateAngle(x, y, referencePoint, 0);
  }

}
