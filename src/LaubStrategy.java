import java.util.Hashtable;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;
import java.lang.Math;


class LaubStrategy implements IAlgorithm {
  private Animator animator;
  private Vertex start = null;
  private Vertex target = null;
  
  public LaubStrategy(Vertex s, Vertex t, Animator animator) {
    setStart(s);
    setTarget(t);
    this.animator = animator;
  }
  
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
      System.out.println(current);
      path.add(current);
    }
    return path;
  }
  
  public Vertex step(Vertex current) {
    Vertex bestDeltaDistance = getNeighbourWithBestDeltaDistance(current);
    Vertex longestVector = getNeighbourWithLongestVector(current);
    Vertex bestNextVertex = current;
    if(bestDeltaDistance.distance(target) != 0) {
        double bestVertexScore = -1;

        for(Vertex v : current.neighbours) {
            double normalizedDeltaDistance = normalizeDeltaDistance(v, bestDeltaDistance);
            double normalizedVectorLength = normalizeVectorLength(v, current, longestVector);
            
            System.out.println(normalizedDeltaDistance);
            System.out.println(normalizedVectorLength);
            
            double vertexScore = calculateScore(v, current, normalizedDeltaDistance, normalizedVectorLength);
            if(vertexScore > bestVertexScore && bestVertexScore != 0) {
                bestNextVertex = v;
                bestVertexScore = vertexScore;
            }
        }
    } else {
      bestNextVertex = bestDeltaDistance;
    }
    return bestNextVertex;
  }
  
  private HashSet<Vertex> removeBadVertices(Vertex current) {
    HashSet<Vertex> candidates = new HashSet<>();
    for(Vertex v : current.neighbours) {
      if(current.distance(target) > v.distance(target)) {
        candidates.add(v);
      }
    }
    return candidates;
  }
  
  private Vertex getNeighbourWithLongestVector(Vertex vertex) {
    Vertex longestVector = vertex;
    for(Vertex v : vertex.neighbours) {
      if(vertex.distance(v) > vertex.distance(longestVector)) {
        longestVector = v;
      }
    }
    return longestVector;
  }
  
  private Vertex getNeighbourWithBestDeltaDistance(Vertex vertex) {
    Vertex bestDeltaDistance = vertex;
    for(Vertex v : vertex.neighbours) {
      if(v.distance(target) < bestDeltaDistance.distance(target)) {
        bestDeltaDistance = v;
      }
    }
    return bestDeltaDistance;
  }
  
  private double normalizeVectorLength(Vertex v, Vertex current, Vertex longest) {
    return current.distance(v) / current.distance(longest);
  }
  
  private double normalizeDeltaDistance(Vertex v, Vertex bestDeltaDistance) {
    return bestDeltaDistance.distance(target) / v.distance(target);
  }
  
  private double calculateScore(Vertex v, Vertex current, double normalizedDeltaDistance, double normalizedVectorLength) {
    Vertex v_ = v.sub(current);
    Vertex t_ = target.sub(current);

    double dot = v_.dot(t_);
    double v_length = v_.distance(new Vertex(.0,.0));
    double t_length = t_.distance(new Vertex(.0,.0));
    double angle = (dot / (v_length * t_length));
    angle = Math.toDegrees(Math.acos(angle)) / 90;
    return ((normalizedDeltaDistance) / (normalizedVectorLength)) / (Math.pow(angle, 3));
  }
  
}