package retrieval;

/**
 * @author Anthony Jackson
 * @id 11170365
 *	4BCT
 */

public class Node {

    public Object value;
    public Node leftOp;
    public Node rightOp;

    public Node(Object value, Node leftOp, Node rightOp) {
        this.value = value;
        this.leftOp = leftOp;
        this.rightOp = rightOp;
    }

    public Node(Object value) {
        this( value, null, null );
    }

    public boolean isLeaf() {
        return (leftOp == null) && (rightOp == null);
    }
}

