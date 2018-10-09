import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

/**
 * Erweitert die Klasse LaubStrategy mit der Funktionalität, die Berechnungen der einzelnen Schritte
 * grafisch auf der GUI darzustellen.<br>
 * Dafür wird ein {@link Animator} mit {@link Animation}-Objekten gefüllt, die später schrittweise abgespielt werden können.
 */

class LaubStrategyAnimated extends LaubStrategy {

  public LaubStrategyAnimated(Vertex s, Vertex t, Animator animator) {
    super(s, t, animator);
  }

  protected double calculateScore(Vertex current, Vertex v) {

    double score = super.calculateScore(current, v);

    animator.addAnimation(
      new Animation()
        .text((new DecimalFormat("0.0").format(score)))
        .duration(0)
    );

    float fscore = (float)score;
    float red = fscore < 8000 ? Math.min((fscore) / 30, 255) : 255;
    float green = fscore > 8000 ? 255 - Math.min((fscore - 8000) / 240, 255) : 255;
    animator.addAnimation(
      new Animation()
        .line(current.x, current.y, v.x, v.y)
        .duration(2)
        .color(red, green, 0)
    );

    return score;
  }

  public Vertex step(Vertex current) {
    animator.addAnimation(
      new Animation()
        .reset()
        .duration(0)
    );
    return super.step(current);
  }

  public List<Vertex> run() {

    List<Vertex> list = super.run();
    animator.addAnimation(
      new Animation().reset()
    );
    return list;
  }
}
