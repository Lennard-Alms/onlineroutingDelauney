/**
 * 2D edge class implementation.
 *
 * @author Johannes Diemke
 */
public class Edge2D {

    public Vertex a;
    public Vertex b;

    /**
     * Constructor of the 2D edge class used to create a new edge instance from
     * two 2D vectors describing the edge's vertices.
     *
     * @param a
     *            The first vertex of the edge
     * @param b
     *            The second vertex of the edge
     */
    public Edge2D(Vertex a, Vertex b) {
        this.a = a;
        this.b = b;
    }

}
