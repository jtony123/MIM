package retrieval;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import datastructures.AVLTree;


	/** Represents an arithmetic expression in a binary tree structure. Each
	 * internal node represents an operator, and its subtrees represent
	 * operands. All operators are binary, so every operator has both a left
	 * and a right operand. Leaf nodes represent either integers or variables.
	 *
	 * @see Node
	 */
	public class BooleanIR {

	    /** The root node of the tree. */
	    private Node expression;



	    /** Takes a string containing an arithmetic expression and creates a
	     * tree representing that expression.
	     * Precondition: <code>expStr</code> must be fully parenthesized and
	     * have whitespace around each operator. For example, "(a * (5 / n))".
	     *
	     * @param expStr a string representation of the expression.
	     * @param invertedIndex 
	     * @param i 
	     * @throws MalformedExpressionException if <code>expStr</code> is not 
	     *               a fully-parenthesized, whitespace-separated expression.
	     */
	    public BooleanIR( String expStr, AVLTree<String> invertedIndex, int numDocs ) {//throws Exception {
	    	
	        try {
	            // Call a recursive helper method to create the tree.
	            expression = createTree( new Scanner( expStr, invertedIndex, numDocs));
	        } catch( Exception e ) {
	            //throw new Exception(
	                    //"Input expression ended prematurely" );
	        }
	    }




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
	    public List<Integer> eval() {
	        //throws Exception {
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
	    private List<Integer> eval(Node root) {
	        //throws Exception {
	    	//BooleanIR booleanIR = new BooleanIR();
	        // If the node a leaf, just return its value.
	        if (root.isLeaf()) {
	            // Evaluation should only be attempted on numeric expressions.
	            if( ! ( root.value instanceof List )) {
	                //throw new Exception( 
	                //        "Expression to evaluate contains a variable: "
	                //        + root.value );
	            }
	            
	            return (List<Integer>) (root.value);

	        // Otherwise, look at the type of the operand and perform the
	        // appropriate operation on it.
	        } else if (root.value.equals( "AND" )) {
	        	return doAND(eval(root.leftOp), eval(root.rightOp));        
	            
	        } else if (root.value.equals( "OR" )) {
	        	return doOR(eval(root.leftOp), eval(root.rightOp));
	        	
	        } else if (root.value.equals( "NOT" )) {
	        	return doNOT(eval(root.leftOp), eval(root.rightOp));
	        	        
	        // If none of the previous cases matched, then the operator must be
	        // invalid.
	        } else {
	        	return null;
	            //throw new Exception(
	            //        "Invalid operator in expression to evaluate: " 
	            //        + root.value );
	        }
	    }
		private List<Integer> doOR (List<Integer> list1, List<Integer> list2){
			
			List<Integer> matchedDocs = new ArrayList<Integer>();         
	 		Set<Integer> set = new TreeSet<Integer>(list1);
	 		set.addAll(list2);
	 		matchedDocs.addAll(set);     	
			return matchedDocs;			
		}
		
		
		private List<Integer> doAND (List<Integer> list1, List<Integer> list2){
			
		     List<Integer> matchedDocs = new ArrayList<Integer>();
	         
	         if(list1.size()>0 &&list2.size()>0){
	         	int i=0,j=0;
	         	while(i < list1.size() && j < list2.size()){
		            	
	         		if(list1.get(i).equals(list2.get(j))) {
		            		matchedDocs.add((Integer) list1.get(i));
		            		++i;++j;
		            } else {
		            		    	
		            	if(list1.get(i) < list2.get(j)){
		            		++i;
		            	} else {
		            		++j;
		            	}
		            }
	         	}
	     	}         
			return matchedDocs;		
		}


		private List<Integer> doNOT(List<Integer> list1, List<Integer> list2) {
			// TODO Auto-generated method stub
			return null;
		}
	  
}
