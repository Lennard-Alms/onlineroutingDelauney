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
    Vertex orth = new Vertex(-lin.y, lin.x);
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
