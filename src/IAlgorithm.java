import java.util.Hashtable;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;
import java.lang.Math;


interface IAlgorithm {
  public Vertex step(Vertex current);
  public List<Vertex> run();
  public void setAnimator();
}
