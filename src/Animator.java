import java.util.Hashtable;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;
import java.lang.Math;

import javafx.scene.layout.Pane;

class Animator {
  
  private List<Animation> queue = new ArrayList<>();
  private Pane layer;
  private int cursor = 0;
  
  public Animator(Pane layer) {
    this.layer = layer;
  }
  
  public void addAnimation(Animation animation) {
    queue.add(animation);
  }
  
  public void drawNext() {
    
  }
  
  public void reset() {
    cursor = 0;
  }
  
  
  
}