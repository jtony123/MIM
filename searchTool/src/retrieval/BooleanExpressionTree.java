package retrieval;

import java.util.List;

import datastructures.AVLTree;

/** Represents an arithmetic expression in a binary tree structure. Each
 * internal node represents an operator, and its subtrees represent
 * operands. All operators are binary, so every operator has both a left
 * and a right operand. Leaf nodes represent either integers or variables.
 *
 * @see Node
 */
public class BooleanExpressionTree {

    /** The root node of the tree. */
    private Node expression;



    /** Takes a string containing an arithmetic expression and creates a
     * tree representing that expression.
     * Precondition: <code>expStr</code> must be fully parenthesized and
     * have whitespace around each operator. For example, "(a * (5 / n))".
     *
     * @param expStr a string representation of the expression.
     * @param invertedIndex 
     * @param documentsIdList 
     * @throws MalformedExpressionException if <code>expStr</code> is not 
     *               a fully-parenthesized, whitespace-separated expression.
     */
    public BooleanExpressionTree( String expStr, AVLTree<String> invertedIndex, List<Integer> documentsIdList ) throws Exception {
    	
        try {
            // Call a recursive helper method to create the tree.
            expression = createTree( new Scanner( expStr, invertedIndex, documentsIdList));
        } catch( NullPointerException e ) {
            throw new Exception(
                    "Input expression ended prematurely" );
        }
    }



    /** Private constructor: creates a new ExpressionTree with root node
     * <code>root</code>. Used by the <code>simplify()</code> method to
     * create the simplified tree.
     *
     * @param root the root of the new tree.
     */
//    private BooleanExpressionTree( Node root ) {
//        expression = root;
//    }



    /** Recursive helper method of the public constructor. Reads an
     * expression specification from the scanner until the parentheses (if
     * any) are matched and creates the corresponding tree. 
     * Note: <code>createTree</code> does not read until the end of the
     * input, only to the end of the current subexpression.
     *
     * @param scanner the token stream representing the part of the
     *                expression that has not been read yet.
     * @throws MalformedExpressionException if the input expression is
     *                not valid.
     */
    private Node createTree( Scanner scanner ) 
        throws Exception {
        // Read the next token from the scanner.
        Token nextTok = scanner.getToken();
        // If it's null, then the expression must be wrong.
        if( nextTok == null ) {
            throw new Exception( 
                    "Input expression ended prematurely");
        }
        
        // Otherwise, check if it's the beginning of a subexpression.
        if( nextTok.equals( "(" )) {
            // It's a subexpression; eat the "(" token.
            scanner.eatToken();

            // Read the operator and the operands and create left and right
            // subtrees for the operands.
            Node leftOperand = createTree( scanner );
            Token opTok = scanner.getToken();

            // If opTok is null, then the expression is wrong.
            if( opTok == null ) {
                throw new Exception(
                        "Input expression ended prematurely" );
            }

            String op = opTok.getName();
            // Eat the operator token.
            scanner.eatToken();
            Node RightOperand = createTree( scanner );

            // Make sure the next token is ")"; otherwise, the input is
            // bad.
            scanner.useToken( ")" );

            // Create the root of a subtree to represent the expression
            // just read.
            return new Node( op, leftOperand, RightOperand );

        } else {
            // The expression is just a single value; create a leaf node
            // for it.            
            Object value;
            if( nextTok.isList() ) {
                value = nextTok.getValue();
            } else {
                value = nextTok.getName();
            }

            // Eat the integer/variable token.
            scanner.eatToken();
            return new Node( value );
        }
    }



    /** Returns the result of evaluating this expression tree.
     * Precondition: this expression tree must consist only of integer
     * values (no variables), with no division by zero, and every operator
     * must be valid.
     *
     * @return the integer result of evaluating this expression tree. 
     * @throws MalformedExpressionException if the expression contains
     *         a variable or if the operator is invalid.
     * @throws ArithmeticException if an attempt to divide by 0 occurs.
     */
    public List<Integer> eval() 
        throws Exception {
        // Call a recursive helper method to perform the actual evaluation.
        return eval(expression);
    }



    /** Recursive helper method for eval(). Evaluates the expression in the
     * subtree rooted at <code>root</code>.
     *
     * @param root the root of the expression tree to evaluate.
     * @return the integer result of evaluating the expression. 
     * @throws MalformedExpressionException if the expression contains
     *         a variable or if the operator is invalid.
     * @throws ArithmeticException if an attempt to divide by 0 occurs.
     */
    private List<Integer> eval(Node root)
        throws Exception {
    	BooleanIR booleanIR = new BooleanIR();
        // If the node a leaf, just return its value.
        if (root.isLeaf()) {
            // Evaluation should only be attempted on numeric expressions.
            if( ! ( root.value instanceof List )) {
                throw new Exception( 
                        "Expression to evaluate contains a variable: "
                        + root.value );
            }
            
            return (List<Integer>) (root.value);

        // Otherwise, look at the type of the operand and perform the
        // appropriate operation on it.
        } else if (root.value.equals( "AND" )) {
        	return booleanIR.doAND(eval(root.leftOp), eval(root.rightOp));        
            
        } else if (root.value.equals( "OR" )) {
        	return booleanIR.doOR(eval(root.leftOp), eval(root.rightOp));
        	
        } else if (root.value.equals( "NOT" )) {
        	return booleanIR.doNOT(eval(root.leftOp), eval(root.rightOp));
        	        
        // If none of the previous cases matched, then the operator must be
        // invalid.
        } else {
            throw new Exception(
                    "Invalid operator in expression to evaluate: " 
                    + root.value );
        }
    }



    /** Prints out this expression, fully parenthesized.
     */
    public void printExpression() {
        // Call a recursive helper method to perform the actual printing.
        printExpression(expression);
        System.out.println();
    }



    /** Recursive helper method for printExpression(). Prints out the
     * expression in the subtree rooted at <code>root</code>, fully
     * parenthesized.
     *
     * @param root the root of the expression tree to print.
     */
    private void printExpression(Node root) {
        // If the node is a leaf, just print the value.
        if (root.isLeaf()) {
            System.out.print(root.value);

        // Otherwise, print an opening parenthesis, the operands and
        // operator, and a closing parenthesis.
        } else {
            System.out.print("(");
            printExpression(root.leftOp);
            System.out.print(" " + root.value + " ");
            printExpression(root.rightOp);
            System.out.print(")");
        } 
    }



    /** Returns a new ExpressionTree representing a simplified
     * version of this ExpressionTree. Does not change this ExpressionTree.
     *
     * The simplifications performed are as follows (where x is any
     * expression):
     * <ul>
     * <li> (x * 0) and (0 * x) become 0
     * <li> (x * 1) and (1 * x) become x
     * <li> (x + 0) and (0 + x) become x
     * <li> As well, any numeric sum or product expression is replaced by 
     *      its value.
     * </ul>
     *
     * @return an ExpressionTree representing a simplified version of this 
     *         ExpressionTree.
     */
//    public BooleanExpressionTree simplify() {
//        // Call a recursive helper method to perform the actual
//        // simplification.
//        return new BooleanExpressionTree( simplify(expression) );
//    }



    /** Recursive helper method for simplify(). Returns
     * a new tree representing a simplified version of the expression in
     * the subtree rooted at <code>root</code>. Does not change the
     * expression in the subtree rooted at <code>root</code>. 
     *
     * @return a Node pointing to the root of a tree representing the
     *         simplified version of the tree rooted at <code>root</code>.
     *
     * @see #simplify() the simplification rules.
    */
//    private Node simplify(Node root) {
//        // The easy cases.
//        if (root == null) {
//            return null;
//        } else if (root.isLeaf()) {
//            return new Node( root.value );
//
//        // Simplification proceeds differently for products and sums. Call
//        // the appropriate helper method.
//        } else if (root.value.equals( "*" )){
//            try {
//                return simplifyProduct(root);
//            } catch( Exception e ) {
//                // We have checked the parameters, so the exception will
//                // not actually be thrown.
//                return null;
//            }
//        } else if (root.value.equals( "+" )){
//            try {
//                return simplifySum(root);
//            } catch( Exception e ) {
//                // We have checked the parameters, so the exception will
//                // not actually be thrown.
//                return null;
//            }
//
//        // If the node is not a product or a sum, just simplify the two
//        // operands and return the result of that process.
//        } else {
//            return new Node( root.value,
//                    simplify( root.leftOp ),
//                    simplify( root.rightOp ));
//        }
//    }



    /** Recursive helper method for simplifying a product. Simplifies
     * multiplication by 0 or 1, and all-numeric expressions.
     * Precondition: the operator must be *.
     *
     * @param root the root of an ExpressionTree. 
     * @return the root of a new tree representing the simplified version
     *         of the tree rooted at <code>root</code>.
     * @throws MalformedExpressionException if the operator is not *.
     *
     * @see #simplify()
     */
//    private Node simplifyProduct(Node root) 
//        throws Exception { 
//        // Verify the operator.
//        if( ! root.value.equals( "*" )) {
//            throw new Exception(
//                    "Expression sent to simplifyProduct is not a product: "
//                    + root.value );
//        }
//
//        // Check if the first operand evaluates to 0 or 1.
//        Integer zero = new Integer( 0 );
//        Integer one = new Integer( 1 );
//        Node leftSimp = simplify(root.leftOp);
//        if (leftSimp.value.equals( zero )) {
//            return new Node( zero );
//        } else if (leftSimp.value.equals( one )){
//            return simplify(root.rightOp);
//
//        } else { 
//            // Check if the second operand evaluates to 0 or 1.
//            Node rightSimp = simplify(root.rightOp);
//            if (rightSimp.value.equals( zero )) {
//                return new Node( zero );
//            } else if (rightSimp.value.equals( one )) {
//                return leftSimp;
//
//            // Neither operand is 0 or 1. If both operands are numeric,
//            // then return their product; otherwise no simplifications are
//            // possible.
//            } else if(( leftSimp.value instanceof Integer ) 
//                    && ( rightSimp.value instanceof Integer )) {
//                int newValue = ((Integer)(leftSimp.value)).intValue() *
//                        ((Integer)(rightSimp.value)).intValue();
//                return new Node( new Integer( newValue ));
//            } else {
//                return new Node( "*", leftSimp, rightSimp );
//            }
//        }
//    }



    /** Recursive helper for simplifying a sum. Simplifies addition of 0 
     * and all-numeric expressions.
     *
     * @param root the root of an ExpressionTree. Precondition: the
     *             operator must be +.
     * @return the root of a new tree representing the simplified version
     *         of the tree rooted at <code>root</code>.
     * @throws MalformedExpressionException if the operator is not +.
     *
     * @see #simplify()
     */
//    private Node simplifySum(Node root) 
//        throws Exception {
//        // Verify the operator
//        if( ! root.value.equals( "+" )) {
//            throw new Exception(
//                    "Expression sent to simplifySum is not a sum: "
//                    + root.value );
//        }
//
//        // Simplify the two operands.
//        Node leftSimp = simplify(root.leftOp);
//        Node rightSimp = simplify(root.rightOp);
//
//        // If either operand is 0, then just return the other.
//        Integer zero = new Integer( 0 );
//        if (leftSimp.value.equals( zero )) {
//            return rightSimp;
//        } else if (rightSimp.value.equals( zero )) {
//            return leftSimp;
//
//        // Neither operand is 0. If both operands are numeric, then return
//        // their sum; otherwise no simplifications are possible.
//        } else if(( leftSimp.value instanceof Integer ) 
//                && ( rightSimp.value instanceof Integer )) {
//            int newValue = ((Integer)(leftSimp.value)).intValue() +
//                    ((Integer)(rightSimp.value)).intValue();
//            return new Node( new Integer( newValue ));
//        } else {
//            return new Node( "+", leftSimp, rightSimp );
//        }
//    }
}  

