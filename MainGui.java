import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainGui extends Application {

  public List<Vector2D> pointSet = new ArrayList<>();

  public static void main(String[] args) {
    launch(args);
  }



  public void start(Stage stage){
    Group root = new Group();
    Group triLayer = new Group();
    Group pointLayer = new Group();
    Scene scene = new Scene(root, 800, 600);
    stage.setTitle("Onlinerouting");
    setupCoordinate(pointLayer, triLayer, stage);
    stage.setScene(scene);
    root.getChildren().add(triLayer);
    root.getChildren().add(pointLayer);
    stage.show();
  }




  public void setupCoordinate(Group pointLayer, Group triLayer, Stage stage){ // creates transparent rectangle that handels the click event
    Rectangle r = new Rectangle();
    r.setX(0);
    r.setY(0);
    r.setWidth(800);
    r.setHeight(600);
    r.setFill(Color.color(0,0,0,0));
    r.setOnMousePressed(new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        addPoint(event.getX(), event.getY(), pointLayer, triLayer);
      }
    });
    pointLayer.getChildren().add(r);
  }


  public void addPoint(double x, double y, Group group, Group triLayer) { //Creates new point
    Circle point = new Circle();
    point.setCenterX(x);
    point.setCenterY(y);
    point.setRadius(4.0);
    pointSet.add(new Vector2D(x,y)); // add to the point list
    int position = pointSet.size() - 1;  // remember wich point it is
    point.setOnMouseDragged(new EventHandler<MouseEvent>() { // Handle drag
      public void handle(MouseEvent event) {
        double deltaX = Math.abs(point.getCenterX() - event.getSceneX());
        double deltaY = Math.abs(point.getCenterY() - event.getSceneY());
        if(deltaX + deltaY > 1){
          point.setCenterX(event.getSceneX());
          point.setCenterY(event.getSceneY());
          pointSet.set(position, new Vector2D(event.getSceneX(), event.getSceneY())); // update the right point in the point List
          drawTriangulation(triLayer); // update the triangulation
        }
      }
    });
    group.getChildren().add(point); // add point
    drawTriangulation(triLayer); // update the triangulation
  }



  public void drawTriangulation(Group group){
    DelaunayTriangulator triangulator = new DelaunayTriangulator(pointSet);
    try {
      triangulator.triangulate();
    } catch (NotEnoughPointsException e1) {
      System.out.println("NotEnoughPointsException");
    }
    group.getChildren().clear();
    for (int i = 0; i < triangulator.getTriangles().size(); i++) {
      Triangle2D triangle = triangulator.getTriangles().get(i);
      Polygon polygon = new Polygon();
      polygon.getPoints().addAll(new Double[]{
          triangle.a.x, triangle.a.y,
          triangle.b.x, triangle.b.y,
          triangle.c.x, triangle.c.y });
      polygon.setFill(Color.LIGHTSLATEGRAY);
      polygon.setStroke(Color.BLACK);
      polygon.setStrokeWidth(1);
      group.getChildren().add(polygon);
    }
  }

}



















// END
