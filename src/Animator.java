import java.util.Hashtable;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;
import java.lang.Math;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import javafx.scene.shape.Shape;
import javafx.scene.layout.Pane;

import java.lang.Thread;

class Animator {

  private List<Animation> animations = new ArrayList<>();
  private Pane layer;
  private int cursor = 0;
  private VBox textbox;
  private double rate = 1;

  public Animator(Pane layer, VBox textbox) {
    this.layer = layer;
    this.textbox = textbox;
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
      cursor++;
      if(animation.duration == 0) {
        drawNext();
      }
    }
  }

  private void draw(Animation animation) {
    Shape shape = animation.shape;
    int type = animation.type;

    switch(type) {
      case Animation.TEXT:
        textbox.getChildren().add(animation.shape);
        break;
      case Animation.RESET:
        textbox.getChildren().clear();
        Text txt = new Text("Scores:");
        txt.setStroke(Color.RED);
        textbox.getChildren().add(txt);
        layer.getChildren().clear();
        break;
      case Animation.LINE:
        if(animation.color != null) {
          shape.setStroke(animation.color);
        }
        shape.setStrokeWidth(6);
        layer.getChildren().add(animation.shape);
        break;
    }

  }





}
