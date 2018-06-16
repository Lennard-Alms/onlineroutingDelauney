import java.util.Hashtable;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;
import java.lang.Math;

public class Graph {

    public List<Vertex> vList = new ArrayList<>();
    public HashSet<Vertex> V = new HashSet<>();

    public Graph() {

    }

    public void addVertex(Vertex v) {
        V.add(v);
        vList.add(v);
        calculateTriangulation();
    }

    public void calculateTriangulation() {
        for(Vertex v : V) {
            v.neighbours.clear();
            v.nList.clear();
        }
        if(V.size() > 2) {
            DelaunayTriangulator triangulator = new DelaunayTriangulator(vList);
            triangulator.triangulate();
            List<Triangle2D> triangles = triangulator.getTriangles();
            for(Triangle2D tri : triangles) {
                addEdge(tri.a, tri.b);
                addEdge(tri.b, tri.c);
                addEdge(tri.a, tri.c);
            }
        } else if(V.size() == 2) {
            addEdge(vList.get(0), vList.get(1));
        }
    }

    public void addEdge(Vertex v, Vertex w) {
        if(V.contains(v) && V.contains(w)) {
            v.addNeighbour(w);
            w.addNeighbour(v);
        }
    }

    public void clear() {
        vList.clear();
        V.clear();
    }



    public List<Vertex> laubenthalschesRouting() {
        List<Vertex> path = new ArrayList<>();
        Vertex current = vList.get(0);
        path.add(current);

        while(!current.equals(vList.get(1))) {
            HashSet<Vertex> candidates = new HashSet<>();
            for(Vertex v : current.neighbours) {
                if(current.distance(vList.get(1)) > v.distance(vList.get(1))) {
                    candidates.add(v);
                }
            }
            Vertex bestDist = current;
            Vertex longestEdge = current;
            for(Vertex v : candidates) {
                if(v.distance(vList.get(1)) < bestDist.distance(vList.get(1))) {
                    bestDist = v;
                }
                if(current.distance(v) > current.distance(longestEdge)) {
                    longestEdge = v;
                }
            }
            Vertex best = current;
            if(bestDist.distance(vList.get(1)) != 0) {
                double bestRatio = -1;
                double distNorm;
                double edgeNorm;
                double routingRatio;

                for(Vertex v : candidates) {
                    distNorm = bestDist.distance(vList.get(1)) / v.distance(vList.get(1));
                    edgeNorm = current.distance(v) / current.distance(longestEdge);

                    Vertex v_ = v.sub(current);
                    Vertex t_ = vList.get(1).sub(current);

                    double dot = v_.dot(t_);
                    double v_length = v_.distance(new Vertex(.0,.0));
                    double t_length = t_.distance(new Vertex(.0,.0));
                    double angle = (dot / (v_length * t_length));
                    angle = Math.toDegrees(Math.acos(angle)) / 90;

                    routingRatio = ((distNorm) / (edgeNorm)) / (Math.pow(angle, 3));
                    if(routingRatio > bestRatio && bestRatio != 0) {
                        best = v;
                        bestRatio = routingRatio;
                    }
                }
            } else {
              best = bestDist;
            }
            current = best;
            path.add(current);
        }
        return path;
    }

    public List<Vertex> chewsNew() {
        System.out.println("____________");
        List<Vertex> path = new ArrayList<>();
        Vertex s = vList.get(0);
        Vertex t = vList.get(1);
        Vertex current = s;
        path.add(current);
        int i = 0;
        while(!current.equals(t) && i < 1000) {
            i++;
            Vertex x = null;
            Vertex y = null;
            for(Vertex v : current.nList) {

                if(v.equals(t)) {
                    path.add(t);
                    return path;
                }

                for(Vertex w : current.nList) {
                    if(v.neighbours.contains(w)) {
                        if(intersects(s,t,v,w)) {
                            if(x == null) {
                                x = v;
                                y = w;
                                System.out.println("first set");
                            } else if(Geometry.getDistanceFromPointToIntersection(s,t,x,y) < Geometry.getDistanceFromPointToIntersection(s,t,v,w)) {
                                x = v;
                                y = w;
                                System.out.println("change");
                            }
                        }
                    }
                }
            }
            System.out.println("next iter");

            Vertex cc = GetCircumcenter(current, x, y);
            Vertex leftmost = cc.add(s.sub(t).mult(1/s.sub(t).mag()).mult(cc.distance(current)));
            Vertex rightmostInter = findRightIntersect(s,t,cc,current);

            if(Geometry.comparePointToLine(leftmost, rightmostInter, current) == Geometry.comparePointToLine(leftmost, rightmostInter, x)) {
                current = x;
            } else {
                current = y;
            }
            path.add(current);
        }
        return path;
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


    public List<Vertex> greedyRoutingPath() {
        Vertex current = vList.get(0);
        List<Vertex> path = new ArrayList<>();
        path.add(current);
        Vertex best = current;
        while(!current.equals(vList.get(1))) {
            for(Vertex v : current.neighbours) {
                if(best.distance(vList.get(1)) > v.distance(vList.get(1))) {
                    best = v;
                }
            }
            path.add(best);
            current = best;
        }
        return path;
    }

    public List<Vertex> optimalRoutingPath() {
        for(Vertex v : V) {
            v.l = Double.POSITIVE_INFINITY;
        }
        vList.get(0).l = 0;
        PriorityQueue<Vertex> q = new PriorityQueue<>(11, new VertexComperator());
        q.add(vList.get(0));
        HashSet<Vertex> R = new HashSet<>(V);
        Hashtable<Vertex, Vertex> p = new Hashtable<>();
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
        Vertex v = vList.get(1);
        path.add(0, v);
        while(!v.equals(vList.get(0))) {
            Vertex next = p.get(v);
            path.add(next);
            v = next;
        }
        return path;
    }

    @Override
    public String toString() {
        return "Vertices[" + V + "]";
    }

}
