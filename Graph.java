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

    public void addVertex(Vertex v){
        V.add(v);
        vList.add(v);
        calculateTriangulation();
    }

    public void calculateTriangulation(){
        for(Vertex v : V){
            v.neighbours.clear();
        }
        if(V.size() > 2){
            DelaunayTriangulator triangulator = new DelaunayTriangulator(vList);
            triangulator.triangulate();
            List<Triangle2D> triangles = triangulator.getTriangles();
            for(Triangle2D tri : triangles){
                addEdge(tri.a, tri.b);
                addEdge(tri.b, tri.c);
                addEdge(tri.a, tri.c);
            }
        } else if(V.size() == 2){
            addEdge(vList.get(0), vList.get(1));
        }
    }

    public void addEdge(Vertex v, Vertex w){
        if(V.contains(v) && V.contains(w)){
            v.addNeighbour(w);
            w.addNeighbour(v);
        }
    }

    public void clear(){
        vList.clear();
        V.clear();
    }



    public List<Vertex> laubentahlschesRouting(){
        List<Vertex> path = new ArrayList<>();
        Vertex current = vList.get(0);
        path.add(current);
        
        System.out.println("-----------------------");
        
        int epoche = 0;
        while(!current.equals(vList.get(1))) {
          System.out.println("Iteration: " + epoche);
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
                    // routingRatio = ( angle);
                    // System.out.println(angle);
                    System.out.println(distNorm + " / " + edgeNorm + " = " + routingRatio);
                    if(routingRatio > bestRatio && bestRatio != 0) {
                        best = v;
                        bestRatio = routingRatio;
                    }
                    
                    // if(Math.abs(1 - routingRatio) < bestRatio && bestRatio != 0) {
                    // 
                    // }
                    
                }
            } else {
              best = bestDist;
            }
            current = best;
            path.add(current);
            System.out.println("");
            epoche++;
        }
        return path;
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
    
    
    public List<Vertex> chewsRouting() {
        List<Vertex> path = new ArrayList<>();
        Vertex current = vList.get(0);
        path.add(current);
        
        System.out.println("-----------------------");
        
        while(!current.equals(vList.get(1))) {
          Vertex zero_y_axis = new Vertex(.1, .0);
          Vertex current_t = vList.get(1).sub(current);
          
          double dot = zero_y_axis.dot(current_t);
          double zero_y_axis_length = zero_y_axis.distance(new Vertex(.0, .0));
          double current_t_length = current_t.distance(new Vertex(.0, .0));
          
          double angle_current_t = (dot / (zero_y_axis_length * current_t_length)); // = Cos( alpha )
          double alpha_current_t = Math.toDegrees(Math.acos(angle_current_t));
          
          
          List<Vertex> neighbours_rot = new ArrayList<>();
          List<Vertex> neighbours = new ArrayList<>();
          
          
          System.out.println("-------------");
          System.out.println(current);
          
          for(Vertex v : current.neighbours) {
            Vertex z = v.sub(current);
            double rad = Math.toRadians(alpha_current_t);;
            if(current.sub(vList.get(1)).y < 0) {
              rad = -rad;
            }
            Vertex v_new = new Vertex(z.x * Math.cos(rad) - z.y * Math.sin(rad),
              z.x * Math.sin(rad) + z.y * Math.cos(rad)).add(current);
            neighbours_rot.add(v_new);
            neighbours.add(v);
          }
          
          List<Vertex> neighbours_rot_archive = new ArrayList<>(neighbours_rot);          
          
          Collections.sort(neighbours_rot ,new Comparator<Vertex>() {
              @Override
              public int compare(Vertex v1, Vertex v2) {
                return (v1.x < v2.x) ? -1 : 0;
              }
          });
          Vertex x_rot = neighbours_rot.get(neighbours_rot.size() - 2);
          Vertex y_rot = neighbours_rot.get(neighbours_rot.size() - 1);
          Vertex x = null;
          Vertex y = null;
          int index = 0;
          
          for(Vertex v : neighbours_rot_archive) {
            if(v.equals(x_rot)) x = neighbours.get(index);
            if(v.equals(y_rot)) y = neighbours.get(index);
            index++;
          }
          
          for(Vertex v : current.neighbours) {
            if(v.equals(x)) {
              x = v;              
            }
            if(v.equals(y)) y = v;
            index++;
          }
          
          //Is current point below or above center of circle
          Vertex circumcenter = GetCircumcenter(current, x_rot, y_rot);
          double alpha_current_x = 0;
          double alpha_current_y = 0;
          
          Vertex current_x = x.sub(current);
          Vertex current_y = y.sub(current);
          double dot_x = zero_y_axis.dot(current_x);
          double dot_y = zero_y_axis.dot(current_y);
          double current_x_length = current_x.distance(new Vertex(.0, .0));
          double current_y_length = current_y.distance(new Vertex(.0, .0));
          
          double angle_current_x = (dot_x / (zero_y_axis_length * current_x_length)); // = Cos( alpha )
          double angle_current_y = (dot_y / (zero_y_axis_length * current_y_length)); // = Cos( alpha )
          
          alpha_current_x = Math.toDegrees(Math.acos(angle_current_x));
          alpha_current_y = Math.toDegrees(Math.acos(angle_current_y));
          
          System.out.println(x);
          System.out.println(y);
          if(x_rot.x > current.x && y_rot.x > current.x) {
            System.out.println("true");
          } else {
            System.out.println("false");
          }
          
          if(current.y >= circumcenter.x) {
            if(alpha_current_x < alpha_current_y) {
              current = y;
            } else {
              current = x;
            }
          } else {
            if(alpha_current_x < alpha_current_y) {
              current = x;
            } else {
              current = y;
            }
          }
          System.out.println("Chosen: " + current);
          path.add(current);
          // current = vList.get(1);
        }
        return path;
    }
    

    public List<Vertex> greedyRoutingPath(){
        Vertex current = vList.get(0);
        List<Vertex> path = new ArrayList<>();
        path.add(current);
        Vertex best = current;
        while(!current.equals(vList.get(1))){
            for(Vertex v : current.neighbours){
                if(best.distance(vList.get(1)) > v.distance(vList.get(1))){
                    best = v;
                }
            }
            path.add(best);
            current = best;
        }
        return path;
    }

    public List<Vertex> optimalRoutingPath(){
        for(Vertex v : V){
            v.l = Double.POSITIVE_INFINITY;
        }
        vList.get(0).l = 0;
        PriorityQueue<Vertex> q = new PriorityQueue<>(11, new VertexComperator());
        q.add(vList.get(0));
        HashSet<Vertex> R = new HashSet<>(V);
        Hashtable<Vertex, Vertex> p = new Hashtable<>();
        while(R.size() > 0){
            Vertex v = 	q.poll();
            while(!R.contains(v)){
                v = q.poll();
            }
            R.remove(v);
            for(Vertex w : v.neighbours){
                if(R.contains(w)){
                    if(w.l > v.l + v.distance(w)){
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
