package retrieval;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import datastructures.AVLTree;

/**
 * @author Anthony Jackson
 * @id 11170365 4BCT
 */

public class BooleanIR {

	private Node expression;


	public BooleanIR(String expStr, AVLTree<String> invertedIndex, int numDocs) {

		try {
			expression = createTree(new Scanner(expStr, invertedIndex, numDocs));
		} catch (Exception e) {
			// TODO catch this properly
		}
	}


	private Node createTree(Scanner scanner) throws Exception {
		
		Token nextTok = scanner.getToken();
		if (nextTok == null) {
			throw new Exception("Input expression ended prematurely");
		}

		if (nextTok.equals("(")) {
			scanner.eatToken();
			Node leftOperand = createTree(scanner);
			Token opTok = scanner.getToken();

			if (opTok == null) {
				throw new Exception("Input expression ended prematurely");
			}

			String op = opTok.getName();
			scanner.eatToken();
			Node RightOperand = createTree(scanner);

			scanner.useToken(")");
			return new Node(op, leftOperand, RightOperand);

		} else {

			Object value;
			if (nextTok.isList()) {
				value = nextTok.getValue();
			} else {
				value = nextTok.getName();
			}
			scanner.eatToken();
			return new Node(value);
		}
	}

	public List<Integer> eval() {
		// throws Exception {
		// Call a recursive helper method to perform the actual evaluation.
		return eval(expression);
	}

	private List<Integer> eval(Node root) {
	
		// If the node a leaf, just return its value.
		if (root.isLeaf()) {
			
			return (List<Integer>) (root.value);

			// Otherwise, look at the type of the operand and perform the
			// appropriate operation on it.
		} else if (root.value.equals("AND")) {
			return doAND(eval(root.leftOp), eval(root.rightOp));

		} else if (root.value.equals("OR")) {
			return doOR(eval(root.leftOp), eval(root.rightOp));

		} else if (root.value.equals("NOT")) {
			return doNOT(eval(root.leftOp), eval(root.rightOp));

		} else {
			return null;
		}
	}

	private List<Integer> doOR(List<Integer> list1, List<Integer> list2) {

		List<Integer> matchedDocs = new ArrayList<Integer>();
		Set<Integer> set = new TreeSet<Integer>(list1);
		set.addAll(list2);
		matchedDocs.addAll(set);
		return matchedDocs;
	}

	private List<Integer> doAND(List<Integer> list1, List<Integer> list2) {

		List<Integer> matchedDocs = new ArrayList<Integer>();

		if (list1.size() > 0 && list2.size() > 0) {
			int i = 0, j = 0;
			while (i < list1.size() && j < list2.size()) {

				if (list1.get(i).equals(list2.get(j))) {
					matchedDocs.add((Integer) list1.get(i));
					++i;
					++j;
				} else {

					if (list1.get(i) < list2.get(j)) {
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
