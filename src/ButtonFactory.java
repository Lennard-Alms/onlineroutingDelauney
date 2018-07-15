import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import java.util.Random;
import java.io.*;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.util.List;


public class ButtonFactory {
    Stage stage = null;
    Boolean addOnClick = true;
    Animator animator;
    Graph G = null;

    public ButtonFactory(Stage stage, Graph G) {
        this.stage = stage;
        this.G = G;
    }

    public void setAnimator(Animator animator) {
        this.animator = animator;
    }

    public Boolean addOnClick() {
        return addOnClick;
    }

    public void addButton(Pane box, String name, EventHandler<ActionEvent> actionEvent) {
        Button btn = new Button(name);
        btn.setOnAction(actionEvent);
        box.getChildren().add(btn);
    }

    public void addAnimationStepButton(Pane box) {
        Button btn = new Button("Animate");
        btn.setOnAction(new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            animator.drawNext();
          }
        });
        box.getChildren().add(btn);
    }

    public void addOnClickToggleButton(Pane box) {
        Button btn = new Button("No new Nodes");
        btn.setOnAction(new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            if(addOnClick){
              btn.setText("New Nodes");
              addOnClick = false;
            } else {
              btn.setText("No new Nodes");
              addOnClick = true;
            }
          }
        });
        box.getChildren().add(btn);
    }


    public void addSaveFileButton(Pane box) {
        Button btn = new Button("Save file...");
        btn.setOnAction(new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(stage);
            if(file != null){
              saveFile(getTextOutput(), file);
            }
          }
        });
    box.getChildren().add(btn);
    }

    public void saveFile(String content, File file) {
        try {
          FileWriter fileWriter = null;
          fileWriter = new FileWriter(file);
          fileWriter.write(content);
          fileWriter.close();
        } catch (IOException ex) {
        }
    }

    public String getTextOutput() {
        String output = "GraphNodesStart\r\n";
        for(Vertex v : G.vList) {
            output += (v.x - 400) + "\r\n" + (300 - v.y) + "\r\n";
        }
        output += "GraphNodesEnd\r\n";
        output += "RoutingNodesStart\r\n";
        output += (G.vList.get(0).x - 400) + "\r\n" + (300 - G.vList.get(0).y) + "\r\n";
        output += (G.vList.get(1).x - 400) + "\r\n" + (300 - G.vList.get(1).y) + "\r\n";
        output += "RoutingNodesEnd\r\n";
        output += "HighwayNodesStart\r\n";
        output += (G.vList.get(2).x - 400) + "\r\n" + (300 - G.vList.get(2).y) + "\r\n";
        output += (G.vList.get(3).x - 400) + "\r\n" + (300 - G.vList.get(3).y) + "\r\n";
        output += "HighwayNodesEnd";
        return output;
    }


    /*

    public void addWorstCaseButton(Pane box){
        Button btn = new Button("Worst Case");
        btn.setOnAction(new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            calculateWorstCase();
          }
        });
        box.getChildren().add(btn);
    }




    public void calculateWorstCase() {
      Random rand = new Random();
      double worst_laub_euclid = 0;
      double worst_dijkstra_euclid = 0;
      double worst_chew_euclid = 0;
      double worst_laub_dijkstra = 0;
      double worst_chew_dijkstra = 0;

      String worstVerticesLaub = "";
      String worstVerticesChew = "";

      for(int index = 0; index < 1; index++) {
        G.clear();
        G.addVertex(new Vertex(250, 300));
        G.addVertex(new Vertex(550, 300));

        for(int k = 0; k < 12; k++) {
          G.addVertex(new Vertex(rand.nextDouble() * 100 + 350, rand.nextDouble() * 100 + 250));
        }
        int rate = 10;
        for(int j = 0; j < 500000; j++) {
          // if(j % 5 == 0) rate -= 5;
          // if(rate < 0) rate = 200;
          rate = rand.nextInt(20);

          List<Vertex> copyList = new ArrayList<>();
          // copyList.add(G.vList.get(0));
          // copyList.add(G.vList.get(1));
          Set<Vertex> path_tmp = new HashSet<>(G.laubenthalschesRouting());
          copyList.addAll(G.vList);
          for(int k = 2 + (j % (G.vList.size() - 2)); k <= 2 + (j % (G.vList.size() - 2)); k++) {
            if(true) {
              G.vList.get(k).x += (rand.nextDouble() - 0.5) * rate;
              G.vList.get(k).y += (rand.nextDouble() - 0.5) * rate;
              if(G.vList.get(k).x > 800) G.vList.get(k).x = 790;
              if(G.vList.get(k).y > 600) G.vList.get(k).y = 590;
              if(G.vList.get(k).x < 0) G.vList.get(k).x = 5;
              if(G.vList.get(k).y < 0) G.vList.get(k).y = 5;
            } else {
              // copyList.add(G.vList.get(k));
            }
          }
          G.calculateTriangulation();

          List<Vertex> path_laubenthal = G.laubenthalschesRouting();
          // List<Vertex> path_chews = G.chewsNew();
          List<Vertex> path_chews = new ArrayList<>();
          List<Vertex> path_dijkstra = G.optimalRoutingPath();
          double dist_laubenthal = 0;
          double dist_greedy = 0;
          double dist_chew = 0;
          double dist_dijkstra = 0;
          for(int i = 0; i < path_laubenthal.size() - 1; i++) {
            dist_laubenthal += path_laubenthal.get(i).distance(path_laubenthal.get(i+1));
          }
          for(int i = 0; i < path_chews.size() - 1; i++) {
            dist_chew += path_chews.get(i).distance(path_chews.get(i+1));
          }
          for(int i = 0; i < path_dijkstra.size() - 1; i++) {
            dist_dijkstra += path_dijkstra.get(i).distance(path_dijkstra.get(i+1));
          }

          double laub_euclid = dist_laubenthal / G.vList.get(0).distance(G.vList.get(1));
          double dijkstra_euclid = dist_dijkstra / G.vList.get(0).distance(G.vList.get(1));
          double chew_euclid = dist_chew / G.vList.get(0).distance(G.vList.get(1));
          double laub_dijkstra = dist_laubenthal / dist_dijkstra;
          double chew_dijkstra = dist_chew / dist_dijkstra;


          if(worst_laub_euclid <= laub_euclid) {
            worst_laub_euclid = laub_euclid;
            worstVerticesLaub = getTextOutput();
            System.out.println("Punkte nach oben verschoben");
            saveFile(worstVerticesLaub, "worstcase.graph");
          } else {
            G.clear();
            G.vList = copyList;
            G.V = new HashSet<>(copyList);
          }
          if(worst_dijkstra_euclid < dijkstra_euclid) worst_dijkstra_euclid = dijkstra_euclid;
          if(worst_chew_euclid <= chew_euclid) {
            worst_chew_euclid = chew_euclid;
            worstVerticesChew = getTextOutput();
          } else {
            // G.vList = copyList;
          }
          if(worst_laub_dijkstra < laub_dijkstra) worst_laub_dijkstra = laub_dijkstra;
          if(worst_chew_dijkstra < chew_dijkstra) worst_chew_dijkstra = chew_dijkstra;
        }


      }

      System.out.println("WORST CASES");
      System.out.println("Laubenthalsches Routing");
      System.out.println("Euclid: " + worst_laub_euclid);
      System.out.println("Dijkstra: " + worst_laub_dijkstra);
      System.out.println("");
      System.out.println("Chews Routing Algorithm");
      System.out.println("Euclid: " + worst_chew_euclid);
      System.out.println("Dijkstra: " + worst_chew_dijkstra);
      System.out.println("");
      System.out.println("DIJKSTRA: " + worst_dijkstra_euclid);

      saveFile(worstVerticesChew, "worstcase_chew.graph");
    }
    public void saveFile(String content, String filename) {
       try {
         BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
         writer.write(content);

         writer.close();
       } catch(Exception e) {}
     }
     */

}
