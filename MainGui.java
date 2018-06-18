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
  int generationCount = 0;

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
        addNode(event.getX(), event.getY(), true);
      }
    });
    nodeLayer.getChildren().add(r);
  }

  public void addButtons(Stage stage){
    addChooseFileButton(stage);
    addSaveFileButton(stage);
    addRandomPointSetButton();
    addClearButton();
  }
  public void addRandomPointSetButton() { // dies after 30 or so iterations
    Button btn = new Button("Random Set");
    btn.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
         if(generationCount < 30){
          generationCount++;
          nodeLayer.getChildren().clear();
          setupNodeLayer();
          G.clear();
          Random rand = new Random();
          switch(rand.nextInt(3)){
          case 0:
              addNode(20,20, false);
              addNode(780,580, false);
              break;
          case 1:
              addNode(780,580, false);
              addNode(20,20, false);
              break;
          case 2:
              addNode(20,580, false);
              addNode(780,20, false);
              break;
          case 3:
              addNode(780,20, false);
              addNode(20,580, false);
              break;
          }
          for(int i = 0; i < 50; i++){
            addNode(rand.nextInt(760)+20,rand.nextInt(560)+20, false);
          }
         }
         G.calculateTriangulation();
         updateEdgesAndRoute();
      }
    });
    horizontalBox.getChildren().add(btn);
  }

  public void addClearButton() {
    Button btn = new Button("Clear");
    btn.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        nodeLayer.getChildren().clear();
        informationBox.getChildren().clear();
        topLayer.getChildren().clear();
        edgeLayer.getChildren().clear();
        setupNodeLayer();
        G.clear();
      }
    });
    horizontalBox.getChildren().add(btn);
  }

  public void addSaveFileButton(Stage stage) {
    Button btn = new Button("Save file...");
    btn.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(stage);
        if(file != null){
          SaveFile(getTextOutput(), file);
        }
      }
    });
    horizontalBox.getChildren().add(btn);
  }

  public void SaveFile(String content, File file) {
    try {
      FileWriter fileWriter = null;
      fileWriter = new FileWriter(file);
      fileWriter.write(content);
      fileWriter.close();
    } catch (IOException ex) {
    }
  }

  public String getTextOutput(){
    String output = "GraphNodesStart\n";
    for(Vertex v : G.vList){
      output += (v.x - 400) + "\n" + (300 - v.y) + "\n";
    }
    output += "GraphNodesEnd\n";
    output += "RoutingNodesStart\n";
    output += (G.vList.get(0).x - 400) + "\n" + (300 - G.vList.get(0).y) + "\n";
    output += (G.vList.get(1).x - 400) + "\n" + (300 - G.vList.get(1).y) + "\n";
    output += "RoutingNodesEnd\n";
    output += "HighwayNodesStart\n";
    output += (G.vList.get(2).x - 400) + "\n" + (300 - G.vList.get(2).y) + "\n";
    output += (G.vList.get(3).x - 400) + "\n" + (300 - G.vList.get(3).y) + "\n";
    output += "HighwayNodesEnd";
    return output;
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
    G.clear();
    List<Vertex> newVertices = loader.getVertices();
    for(Vertex v : newVertices) {
      addNode(v.x, v.y, false);
    }
    G.calculateTriangulation();
    updateEdgesAndRoute();
  }

  public void addNode(double x, double y, boolean calcTriang) {
    Circle node = new Circle();
    node.setCenterX(x);
    node.setCenterY(y);
    node.setRadius(4.0);
    int position = G.vList.size();
    G.addVertex(new Vertex(x,y), calcTriang);
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
          updateEdgesAndRoute();
        }
      }
    });
    nodeLayer.getChildren().add(node);
    if(calcTriang){
      updateEdgesAndRoute();
    }
  }

  public void updateEdgesAndRoute() {
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
    double dist = 0.0;
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
