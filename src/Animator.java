import java.util.Hashtable;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;
import java.lang.Math;

import javafx.scene.shape.Shape;
import javafx.scene.layout.Pane;

class Animator {

  private List<Animation> animations = new ArrayList<>();
  private Pane layer;
  private int cursor = 0;

  public Animator(Pane layer) {
    this.layer = layer;
  }

  public void addAnimation(Animation animation) {
    animations.add(animation);
  }

  public void reset() {
    cursor = 0;
  }

  public void drawNext() {
    if(cursor < animations.size()) {
      Animation animation = animations.get(cursor);
      draw(animation);
    }
    cursor++;
  }

  private void draw(Animation animation) {
    Shape shape = animation.shape;


  }





}
