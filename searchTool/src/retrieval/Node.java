package retrieval;

/** Represents a node in an expression tree. A leaf node just contains a
 * value (integer or variable); an internal node has an operator and two
 * Nodes representing its operands.
 *
 * @see ExpressionTree
 */

public class Node {

    /** The content of this Node. In internal nodes, it is the operator; in
     * leaf nodes, it is an integer or variable. */
    public Object value;

    /** The left operand of this Node. */
    public Node leftOp;

    /** The right operand of this Node. */
    public Node rightOp;



    /** Creates a Node with value <code>value</code> and operands 
     * <code>leftOp</code> and <code>rightOp</code>. Used to create
     * internal nodes.
     *
     * @param value the "value" of this node (an operator)
     * @param leftOp the left operand
     * @param rightOp the right operand
     */
    public Node(Object value, Node leftOp, Node rightOp) {
        this.value = value;
        this.leftOp = leftOp;
        this.rightOp = rightOp;
    }



    /** Creates a Node with value <code>value</code> and no operands. Used
     * to create leaf nodes.
     *
     * @param value the "value" of this node (an integer or a variable)
     */
    public Node(Object value) {
        this( value, null, null );
    }



    /** Checks whether this Node is a leaf node.
     *
     * @return true if this node has no children and false otherwise.
     */
    public boolean isLeaf() {
        return (leftOp == null) && (rightOp == null);
    }
}

