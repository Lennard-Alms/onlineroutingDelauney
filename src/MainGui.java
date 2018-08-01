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
import javafx.scene.control.CheckBox;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.MalformedURLException;

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
  VBox lengthinformationBox = new VBox();
  VBox animationinformationBox = new VBox();
  Scene scene = new Scene(rootBox, 900, 627);
  Animator animator;
  ButtonFactory buttons;
  int generationCount = 0;
  Boolean[] showAlg = new Boolean[]{true, true, true, true, true};
  int showAlgNum = 31;

  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage stage){
    stage.setTitle("Onlinerouting");
    addButtons(stage);
    setupNodeLayer();
    stage.setScene(scene);
    coordinateSystem.getChildren().add(edgeLayer);
    coordinateSystem.getChildren().add(topLayer);
    coordinateSystem.getChildren().add(nodeLayer);
    verticalBox.getChildren().add(coordinateSystem);
    verticalBox.getChildren().add(horizontalBox);
    rootBox.getChildren().add(verticalBox);
    informationBox.getChildren().add(lengthinformationBox);
    informationBox.getChildren().add(animationinformationBox);
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
        if(buttons.addOnClick()){
          addNode(event.getX(), event.getY(), true);
        }
      }
    });
    nodeLayer.getChildren().add(r);
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
    // Vertex s = G.vList.get(0);
    // Vertex t = G.vList.get(1);
    // Circle node = new Circle();
    // node.setCenterX(s.add(t).mult(0.5).x);
    // node.setCenterY(s.add(t).mult(0.5).y);
    // node.setRadius(s.distance(t) / 2);
    // node.setFill(Color.color(0,0,0,0));
    // node.setStrokeWidth(1);
    // node.setStroke(Color.BLACK);
    // edgeLayer.getChildren().add(node);
    for (Vertex v : G.V) {
      for (Vertex w : v.neighbours) {
        if(v.isHighway && w.isHighway){
          Line highwayEdge = new Line(v.x, v.y, w.x, w.y);
          highwayEdge.setStroke(Color.ORANGE);
          highwayEdge.setStrokeWidth(6);
          edgeLayer.getChildren().add(highwayEdge);
        } else {
          edgeLayer.getChildren().add(new Line(v.x, v.y, w.x, w.y));
        }
      }
    }
    if(G.V.size() > 1){
      lengthinformationBox.getChildren().clear();
      topLayer.getChildren().clear();
      // G.setOnlineStrategy(new LaubStrategy(G.vList.get(0), G.vList.get(1), new Animator(topLayer)));
      // drawRoutingPath(G.route(), Color.VIOLET, 4, "LAUB");
      IAlgorithm chew = new ChewStrategy(G.vList.get(0), G.vList.get(1), new Animator(topLayer, animationinformationBox));
      IAlgorithm comp = new CompasStrategy(G.vList.get(0), G.vList.get(1), new Animator(topLayer, animationinformationBox));
      IAlgorithm greed = new GreedyStrategy(G.vList.get(0), G.vList.get(1), new Animator(topLayer, animationinformationBox));
      IAlgorithm opt = new OptimalStrategy(G.vList.get(0), G.vList.get(1), new Animator(topLayer, animationinformationBox), G);
      IAlgorithm laub = new LaubStrategyAnimated(G.vList.get(0), G.vList.get(1), new Animator(topLayer, animationinformationBox));
      G.setOnlineStrategy(laub);
      buttons.setAnimator(laub.getAnimator());
      if(showAlg[0]) drawRoutingPath(G.route(), Color.VIOLET, 5, "Laub");
      G.setOnlineStrategy(opt);
      if(showAlg[1]) drawRoutingPath(G.route(), Color.LAWNGREEN, 4, "Djiks");
      G.setOnlineStrategy(greed);
      if(showAlg[2]) drawRoutingPath(G.route(), Color.AQUA, 3, "Greedy");
      G.setOnlineStrategy(comp);
      if(showAlg[3]) drawRoutingPath(G.route(), Color.GOLD, 2, "Compas");
      G.setOnlineStrategy(chew);
      if(showAlg[4]) drawRoutingPath(G.route(), Color.RED, 1, "Chew");
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
    lengthinformationBox.getChildren().add(algoName);
    lengthinformationBox.getChildren().add(euclidRatio);
  }

  public void addButtons(Stage stage){
    buttons = new ButtonFactory(stage, G);
    buttons.addButton(horizontalBox, "Choose File...", new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
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
      }
    });
    buttons.addSaveFileButton(horizontalBox);
    buttons.addOnClickToggleButton(horizontalBox);
    buttons.addAnimationStepButton(horizontalBox);
    buttons.addButton(horizontalBox, "Random Set", new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        if(generationCount < 50){
          generationCount++;
          nodeLayer.getChildren().clear();
          setupNodeLayer();
          G.clear();
          Random rand = new Random();
          // addNode(400,20, false);
          // addNode(400,580, false);
          addNode(20,300, false);
          addNode(680,300, false);
          for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
              // addNode(i*10,rand.nextInt(560)+20, false);
              addNode(40 + i*20, 200 + j*20, false);
            }
          }
          for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
              // addNode(i*10,rand.nextInt(560)+20, false);
              addNode(440 + i*20, 200 + j*20, false);
            }
          }
        }
        G.calculateTriangulation();
        updateEdgesAndRoute();
      }
    });
    buttons.addButton(horizontalBox, "Clear", new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        nodeLayer.getChildren().clear();
        lengthinformationBox.getChildren().clear();
        animationinformationBox.getChildren().clear();
        topLayer.getChildren().clear();
        edgeLayer.getChildren().clear();
        G.clear();
        setupNodeLayer();
      }
    });
    buttons.addButton(horizontalBox, "Set Highway", new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        G.calculateHighway();
        updateEdgesAndRoute();
      }
    });
    Button btn = new Button("Show: 11111");
    btn.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        showAlgNum = (showAlgNum + 1) % 32;
        String showAlgNumString = Integer.toBinaryString(showAlgNum);
        btn.setText("Show: " + showAlgNumString);
        showAlg = new Boolean[]{false, false, false, false, false};
        for(int i = 0; i < showAlgNumString.length(); i++) {
          char c = showAlgNumString.charAt(i);
          if(c == "1".charAt(0)){
            showAlg[i] = true;
          }
        }
        updateEdgesAndRoute();
      }
    });
    horizontalBox.getChildren().add(btn);
    buttons.addWorstCaseButton(horizontalBox);
  }



}
