import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PointLoader {

    public String fileText = null;

    public PointLoader(File file){
        try{
            this.fileText = readWholeFile(file);
        } catch(Exception e){
            System.out.println("ERROR");
        }
    }

    public String readWholeFile(File file) throws FileNotFoundException {
        String entireFileText = new Scanner(file)
            .useDelimiter("\\A").next();
        return (entireFileText);
    }

    public List<Vector2D> getPoints(String text, String prefix) {
        String result = text.substring(text.indexOf(prefix + "Start") + prefix.length() + 5 , text.indexOf(prefix + "End"));
        String lines[] = result.split("\\r?\\n");
        List<Vector2D> pointSet = new ArrayList<>();

        for(int i = 1; i < lines.length; i += 2){
            Vector2D point = new Vector2D(Double.parseDouble(lines[i]), Double.parseDouble(lines[i+1]));
            pointSet.add(point);
        }

        return pointSet;
    }

    public List<Vector2D> getNodes(){
        List<Vector2D> sourceAndSink = getPoints(this.fileText, "RoutingNodes");
        List<Vector2D> nodes = getPoints(this.fileText, "GraphNodes");
        boolean sFound = false;
        for(int i = 0; i < nodes.size(); i++){
            if(nodes.get(i).x == sourceAndSink.get(0).x && nodes.get(i).y == sourceAndSink.get(0).y){
                nodes.add(0,nodes.remove(i));
                sFound = true;
            }
            if(nodes.get(i).x == sourceAndSink.get(1).x && nodes.get(i).y == sourceAndSink.get(1).y){
                if(sFound){
                    nodes.add(1,nodes.remove(i));
                } else {
                    nodes.add(0,nodes.remove(i));
                }
            }
        }
        return nodes;
    }

    public List<Vector2D> getRoutingList(){
        return getPoints(this.fileText, "GraphNodes");
    }

    public List<Vector2D> getHighway(){
        return getPoints(this.fileText, "HighwayNodes");
    }
}
