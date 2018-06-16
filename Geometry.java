import java.util.Hashtable;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;
import java.lang.Math;

public class Geometry {

  public static int comparePointToLine(Vertex l, Vertex r, Vertex t) {
    Vertex lin = r.sub(l);
    Vertex orth = new Vertex(lin.y, -lin.x);
    double angle = calculateAngle(orth, t, l);
    if(orth.dot(t.sub(l)) == 0) {
        return 1;
    }
    if(angle <= 90) {
        return 1;
    } else{
        return -1;
    }
  }

  public static double getDistanceFromPointToIntersection(Vertex p,Vertex x,Vertex q,Vertex y) {
      Vertex s = y.sub(q);
      Vertex r = x.sub(p);
      return s.cross(q.sub(p)) / s.cross(r);
  }

  public static double calculateAngle(Vertex x, Vertex y, Vertex referencePoint, int mode) {
    x = x.sub(referencePoint);
    y = y.sub(referencePoint);

    double dot = x.dot(y);
    double length_x = x.distance(new Vertex(.0, .0));
    double length_y = y.distance(new Vertex(.0, .0));
    double angle = (dot / (length_x * length_y));

    if(mode == 0) return Math.toDegrees(Math.acos(angle));
    else return (Math.acos(angle));
  }

  public static double calculateAngle(Vertex x, Vertex y, Vertex referencePoint) {
    return calculateAngle(x, y, referencePoint, 0);
  }

}
