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

/**
 * Zeichnet Schritt für Schritt eine Queue von Animation-Elementen auf der GUI.
 * Die Animator Klasse bekommt einzelne Elemente der Klasse Animation übergeben
 * und kann diese nacheinander auf der GUI zeichnen.
 * Die Klasse dient dazu, Linien, Kreise und Berechnungen der Algorithmen
 * zu verdeutlichen und zu markieren.
 */

class Animator {

  private List<Animation> animations = new ArrayList<>();
  private Pane layer;
  private int cursor = 0;
  private VBox textbox;
  private double rate = 1;

  /**
   * @method Animator
   * @param  Pane     layer         [description]
   * @param  VBox     textbox       [description]
   * @return          [description]
   */
  public Animator(Pane layer, VBox textbox) {
    this.layer = layer;
    this.textbox = textbox;
  }


  /**
   * Fügt Animationselement zu Queue hinzu.
   * @method addAnimation
   * @param  Animation    animation [description]
   */
  public void addAnimation(Animation animation) {
    animations.add(animation);
  }

  /**
   * Setzt die Animationen auf Anfangswert zurück.
   * @method reset
   */
  public void reset() {
    cursor = 0;
  }

  /**
   * Zeichnet das nächste Element in der Queue
   * @method drawNext
   */
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

  /**
   * Zeichnet ein Animationselement auf der GUI
   * @method draw
   * @param  Animation animation [description]
   */
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
