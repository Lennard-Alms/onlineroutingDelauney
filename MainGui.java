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
import java.text.DecimalFormat;
import javafx.scene.layout.Pane;

import java.util.Random;

public class MainGui extends Application {

  public Graph G = new Graph();
  HBox rootBox = new HBox();
  VBox verticalBox = new VBox();
  HBox horizontalBox = new HBox();
  Pane coordinateSystem = new Pane();
  Pane nodeLayer = new Pane();
  Pane topLayer = new Pane();
  Pane edgeLayer = new Pane();
  VBox informationBox = new VBox();
  Scene scene = new Scene(rootBox, 900, 627);

  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage stage){
    stage.setTitle("Onlinerouting");
    setupNodeLayer();
    addButtons(stage);
    stage.setScene(scene);
    coordinateSystem.getChildren().add(edgeLayer);
    coordinateSystem.getChildren().add(topLayer);
    coordinateSystem.getChildren().add(nodeLayer);
    verticalBox.getChildren().add(coordinateSystem);
    verticalBox.getChildren().add(horizontalBox);
    rootBox.getChildren().add(verticalBox);
    rootBox.getChildren().add(informationBox);
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
        addNode(event.getX(), event.getY());
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
    coordinateSystem.getChildren().clear();
    setupNodeLayer();
    G.clear();
    List<Vertex> newVertices = loader.getVertices();
    for(Vertex v : newVertices) {
      addNode(v.x, v.y);
    }
  }

  public void addNode(double x, double y) {
    Circle node = new Circle();
    node.setCenterX(x);
    node.setCenterY(y);
    node.setRadius(4.0);
    int position = G.vList.size();
    G.addVertex(new Vertex(x,y));
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
          G.vList.get(position).x = event.getSceneX();
          G.vList.get(position).y = event.getSceneY();
          G.calculateTriangulation();
          updateEdges();
        }
      }
    });
    nodeLayer.getChildren().add(node);
    updateEdges();
  }

  public void updateEdges() {
    edgeLayer.getChildren().clear();
    for (Vertex v : G.V) {
      for (Vertex w : v.neighbours) {
        edgeLayer.getChildren().add(new Line(v.x, v.y, w.x, w.y));
      }
    }
    if(G.V.size() > 1){
      informationBox.getChildren().clear();
      topLayer.getChildren().clear();
      drawRoutingPath(G.laubenthalschesRouting(), Color.VIOLET, 4, "LAUB");
      drawRoutingPath(G.chewsNew(), Color.RED, 3, "CHEW");
      drawRoutingPath(G.optimalRoutingPath(), Color.LAWNGREEN, 2, "Djiks");
      drawRoutingPath(G.greedyRoutingPath(), Color.AQUA, 1, "Greedy");
    }
  }

  public void drawRoutingPath(List<Vertex> path, Color c, int width, String name){
    double dist = 0;
    for(int i = 0; i < path.size() - 1; i++){
      dist += path.get(i).distance(path.get(i+1));
      Line line = new Line(path.get(i).x, path.get(i).y, path.get(i+1).x, path.get(i+1).y);
      line.setStroke(c);
      line.setStrokeWidth(width);
      edgeLayer.getChildren().add(line);
    }
    Text algoName = new Text(name);
    algoName.setStroke(c);
    DecimalFormat decimalFormat = new DecimalFormat("#.000");
    String ratio = decimalFormat.format(dist / G.vList.get(0).distance(G.vList.get(1)));
    Text euclidRatio = new Text("Euclid: " + ratio);
    informationBox.getChildren().add(algoName);
    informationBox.getChildren().add(euclidRatio);
  }

}





















// END
