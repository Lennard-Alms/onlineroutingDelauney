import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import java.io.*;
import javafx.scene.control.Button;



import javafx.stage.FileChooser;


public class MainGui extends Application {

  public List<Vector2D> nodeSet = new ArrayList<>();

  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage stage){
    VBox verticalBox = new VBox();
    HBox horizontalBox = new HBox();
    Group coordinateRoot = new Group();
    Group triLayer = new Group();
    Group nodeLayer = new Group();
    Scene scene = new Scene(verticalBox, 800, 627);
    stage.setTitle("Onlinerouting");
    setupCoordinateSystem(nodeLayer, triLayer);
    addButtons(horizontalBox, triLayer, nodeLayer, stage);
    stage.setScene(scene);
    coordinateRoot.getChildren().add(triLayer);
    coordinateRoot.getChildren().add(nodeLayer);
    verticalBox.getChildren().add(coordinateRoot);
    verticalBox.getChildren().add(horizontalBox);
    stage.show();
  }

  public void addButtons(HBox box, Group triLayer, Group nodeLayer, Stage stage){
    Button btn = new Button("Choose file...");
    btn.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
          loadFile(file, triLayer, nodeLayer);
        }
      }
    });
    box.getChildren().add(btn);
  }

  public void loadFile(File file, Group triLayer, Group nodeLayer){
    PointLoader loader = new PointLoader(file);
    nodeLayer.getChildren().clear();
    nodeSet.clear();
    setupCoordinateSystem(nodeLayer, triLayer);
    for(Vector2D node : loader.getNodes()){
      addPoint(node.x, node.y, nodeLayer, triLayer);
    }
  }

  public void setupCoordinateSystem(Group nodeLayer, Group triLayer){ // creates transparent rectangle that handels the click event
    Rectangle r = new Rectangle();
    r.setX(0);
    r.setY(0);
    r.setWidth(800);
    r.setHeight(600);
    r.setFill(Color.color(0,0,0,0));
    r.setOnMousePressed(new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        addPoint(event.getX(), event.getY(), nodeLayer, triLayer);
      }
    });
    nodeLayer.getChildren().add(r);
  }

  public void addPoint(double x, double y, Group nodeLayer, Group triLayer) { //Creates new node
    Circle node = new Circle();
    node.setCenterX(x);
    node.setCenterY(y);
    node.setRadius(4.0);
    nodeSet.add(new Vector2D(x,y)); // add to the node list
    int position = nodeSet.size() - 1;  // remember wich node it is
    node.setOnMouseDragged(new EventHandler<MouseEvent>() { // Handle drag
      public void handle(MouseEvent event) {
        double deltaX = Math.abs(node.getCenterX() - event.getSceneX());
        double deltaY = Math.abs(node.getCenterY() - event.getSceneY());
        if(deltaX + deltaY > 1){
          node.setCenterX(event.getSceneX());
          node.setCenterY(event.getSceneY());
          nodeSet.set(position, new Vector2D(event.getSceneX(), event.getSceneY())); // update the right node in the node List
          drawTriangulation(triLayer); // update the triangulation
        }
      }
    });
    nodeLayer.getChildren().add(node); // add node
    drawTriangulation(triLayer); // update the triangulation
  }

  public void drawTriangulation(Group group){
    DelaunayTriangulator triangulator = new DelaunayTriangulator(nodeSet);
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



/*

 */















// END
