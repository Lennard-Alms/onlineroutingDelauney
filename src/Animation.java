import javafx.scene.shape.Shape;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.*;
import javafx.scene.paint.Color;

import javafx.scene.layout.Pane;


class Animation {
  private String classname;

  public static final int CIRCLE = 0;
  public static final int TEXT = 1;
  public static final int LINE = 2;
  public static final int RESET = 3;

  public int type;
  public double duration = 0;
  public Shape shape;
  public Color color;

  public Animation() {

  }

  public Animation circle(double radius, double x, double y) {
    Circle node = new Circle();
    node.setCenterX(x);
    node.setCenterY(y);
    node.setRadius(radius);
    shape = node;
    type = CIRCLE;
    return this;
  }

  public Animation line(double x1, double y1, double x2, double y2) {
    shape = new Line(x1, y1, x2, y2);
    shape.setStrokeWidth(2);
    type = LINE;
    return this;
  }

  public Animation text(String text) {
    shape = new Text(text);
    type = TEXT;
    return this;
  }

  public Animation reset() {
    shape = null;
    type = RESET;
    return this;
  }

  public Animation color(float r, float g, float b) {
    color = new Color(r / 255, g / 255, b / 255, 1.0);
    return this;
  }

  public Animation duration(double duration) {
    this.duration = duration;
    return this;
  }
}
