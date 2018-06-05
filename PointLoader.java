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
        return getPoints(this.fileText, "GraphNodes");
    }
    public List<Vector2D> getRoutingList(){
        return getPoints(this.fileText, "RoutingNodes");
    }
    public List<Vector2D> getHighway(){
        return getPoints(this.fileText, "HighwayNodes");
    }
}
