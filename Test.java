import java.util.ArrayList;
import java.util.List;

public class Test {

  public static void main(String[] args) {
    List<Integer> s = new ArrayList<>();
    for(int i = 0; i < 10; i++){
      s.add(i);

    }
    s.add(0,s.remove(3));
    System.out.println(s);
  }

}
