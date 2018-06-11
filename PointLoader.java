import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;


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

    public List<Vector2D> getPointsFromFile(String prefix) {
        String text = this.fileText;
        String result = text.substring(text.indexOf(prefix + "Start") + prefix.length() + 5 , text.indexOf(prefix + "End"));
        String lines[] = result.split("\\r?\\n");
        List<Vector2D> pointSet = new ArrayList<>();

        for(int i = 1; i < lines.length; i += 2){
            Vector2D point = new Vector2D(Double.parseDouble(lines[i]) + 400.0, 300.0 - Double.parseDouble(lines[i+1]));
            pointSet.add(point);
        }

        return pointSet;
    }

    public List<Vector2D> getNodes(){
        List<Vector2D> nodes = getPointsFromFile("GraphNodes");
        nodes.addAll(0, getPointsFromFile("HighwayNodes"));
        nodes.addAll(0, getPointsFromFile("RoutingNodes"));
        HashSet<String> hs = new HashSet<>();
        List<Vector2D> nodes_tmp = new ArrayList<>();
        for(Vector2D v : nodes) {
            if (!hs.contains(v.toString())) {
                nodes_tmp.add(v);
                hs.add(v.toString());
            }
        }
        return nodes_tmp;
    }
}
