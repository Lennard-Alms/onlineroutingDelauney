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
import java.util.Hashtable;
import javafx.stage.FileChooser;
import javafx.scene.text.*;


public class MainGui extends Application {

  public List<Vector2D> nodeSet = new ArrayList<>();
  VBox verticalBox = new VBox();
  HBox horizontalBox = new HBox();
  Group coordinateRoot = new Group();
  Group triLayer = new Group();
  Group nodeLayer = new Group();
  Text informationText = new Text("  hallo");
  Scene scene = new Scene(verticalBox, 800, 627);

  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage stage){
    stage.setTitle("Onlinerouting");
    setupNodeLayer();
    addButtons(stage);
    stage.setScene(scene);
    coordinateRoot.getChildren().add(triLayer);
    coordinateRoot.getChildren().add(nodeLayer);
    horizontalBox.getChildren().add(informationText);
    verticalBox.getChildren().add(coordinateRoot);
    verticalBox.getChildren().add(horizontalBox);
    stage.show();
  }

  public void setupNodeLayer(){
    Rectangle r = new Rectangle();
    r.setX(0);
    r.setY(0);
    r.setWidth(800);
    r.setHeight(600);
    r.setFill(Color.color(0,0,0,0));
    r.setOnMousePressed(new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        addPoint(event.getX(), event.getY());
      }
    });
    nodeLayer.getChildren().add(r);
  }

  public void addButtons(Stage stage){
    addChooseFileButton(stage);
  }

  public void addChooseFileButton(Stage stage){
    Button btn = new Button("Choose file...");
    btn.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
          loadFile(file);
        }
      }
    });
    horizontalBox.getChildren().add(btn);
  }

  public void loadFile(File file){
    PointLoader loader = new PointLoader(file);
    nodeLayer.getChildren().clear();
    setupNodeLayer();
    nodeSet.clear();
    for(Vector2D node : loader.getNodes()){
      addPoint(node.x, node.y);
    }
  }



  public void addPoint(double x, double y) {
    Circle node = new Circle();
    node.setCenterX(x);
    node.setCenterY(y);
    node.setRadius(4.0);
    int position = nodeSet.size();
    nodeSet.add(new Vector2D(x,y));
    if(position == 0){
      node.setFill(Color.LAWNGREEN);
    } else if (position == 1){
      node.setFill(Color.RED);
    }
    node.setOnMouseDragged(new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        double deltaX = Math.abs(node.getCenterX() - event.getSceneX());
        double deltaY = Math.abs(node.getCenterY() - event.getSceneY());
        if(deltaX + deltaY > 2){
          node.setCenterX(event.getSceneX());
          node.setCenterY(event.getSceneY());
          nodeSet.set(position, new Vector2D(event.getSceneX(), event.getSceneY()));
          List<Triangle2D> triangles = getTriangulation();
          if(triangles != null){
            drawTriangulation(triangles);
            drawRoutingPath(triangles);
          }
        }
      }
    });
    nodeLayer.getChildren().add(node);
    List<Triangle2D> triangles = getTriangulation();
    if(triangles != null){
      drawTriangulation(triangles);
      drawRoutingPath(triangles);
    }
  }

  public List<Triangle2D> getTriangulation(){
    DelaunayTriangulator triangulator = new DelaunayTriangulator(nodeSet);
    try {
      triangulator.triangulate();
      return triangulator.getTriangles();
    } catch (NotEnoughPointsException e1) { /* not enough points */ }
    return null;
  }

  public void drawTriangulation(List<Triangle2D> triangles){
    triLayer.getChildren().clear();
    for (int i = 0; i < triangles.size(); i++) {
      Triangle2D triangle = triangles.get(i);
      Polygon polygon = new Polygon();
      polygon.getPoints().addAll(new Double[]{
          triangle.a.x, triangle.a.y,
          triangle.b.x, triangle.b.y,
          triangle.c.x, triangle.c.y });
      polygon.setFill(Color.color(0.9,0.9,0.9));
      polygon.setStroke(Color.BLACK);
      polygon.setStrokeWidth(1);
      triLayer.getChildren().add(polygon);
    }
  }

  public void drawRoutingPath(List<Triangle2D> triangles){ // TODO: distances and ratios from the routing in the textfield. also the routingpath sould return a list of verticies not vectors.
    Graph G = setupGraph(triangles);
    List<Vector2D> path = G.greedyRoutingPath();
    List<Vector2D> pathOpt = G.optimalRoutingPath();
    double greedyDist = 0.0;
    for(int i = 0; i < path.size() - 1; i++){
      Line line = new Line(path.get(i).x, path.get(i).y, path.get(i+1).x, path.get(i+1).y);
      line.setStroke(Color.RED);
      line.setStrokeWidth(2);
      triLayer.getChildren().add(line);

    }
    double optimalDist = 0.0;
    for(int i = 0; i < pathOpt.size() - 1; i++){
      Line line = new Line(pathOpt.get(i).x, pathOpt.get(i).y, pathOpt.get(i+1).x, pathOpt.get(i+1).y);
      line.setStroke(Color.LAWNGREEN);
      triLayer.getChildren().add(line);
    }
    double eucidianDist = G.V.get(0).getDistance(G.V.get(1));
    informationText.setText("  " + )
  }

  public Graph setupGraph(List<Triangle2D> triangles){
    Hashtable<String, Integer> m = new Hashtable<>();
    Graph G = new Graph();
    for(int i = 0; i < nodeSet.size(); i++){
      m.put(nodeSet.get(i).toString(), i);
      G.addVertex(nodeSet.get(i));
    }
    for(Triangle2D tri : triangles){
      G.addEdge(m.get("[" + tri.a.x + ", " + tri.a.y + "]"),m.get("[" + tri.b.x + ", " + tri.b.y + "]"));
      G.addEdge(m.get("[" + tri.a.x + ", " + tri.a.y + "]"),m.get("[" + tri.c.x + ", " + tri.c.y + "]"));
      G.addEdge(m.get("[" + tri.c.x + ", " + tri.c.y + "]"),m.get("[" + tri.b.x + ", " + tri.b.y + "]"));
    }
    return G;
  }
}





















// END
