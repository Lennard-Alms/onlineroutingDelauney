import java.util.Hashtable;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;
import java.lang.Math;

/**
 * Interface der Onlinerouting Algorithmen
 */

interface IAlgorithm {

  /**
   * Führt genau einen Schritt in der Pfadberechnung durch
   * @method step
   * @param  Vertex current       Aktuelle Position im der Punktemenge des Graphen.
   * @return                      Nächster Knoten des Pfades von start nach target von der Position current.
   */
  public Vertex step(Vertex current);

  /**
   * Berechnet den gesamten Pfad von start nach target
   * @method run
   * @return Pfad als Liste
   */
  public List<Vertex> run();

  /**
   * Setzt eine Animator Klasse
   * @method setAnimator
   * @param  Animator    Animator Objekt
   */
  public void setAnimator(Animator animator);

  /**
   * Gibt Animator zurück
   * @method getAnimator
   */
  public Animator getAnimator();
}
