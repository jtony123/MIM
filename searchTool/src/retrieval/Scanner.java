package retrieval;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import datastructures.AVLTree;
import datastructures.BinaryNodeInterface;
import fileprocessor.FilesProcessor;

/**
 * @author Anthony Jackson
 * @id 11170365 4BCT
 */

public class Scanner {

	private StreamTokenizer input;
	private AVLTree<String> invertedIndex;
	private int numDocuments;
	private Token curToken;

	public Scanner(String s, AVLTree<String> invertedIndex, int numDocs) {

		input = new StreamTokenizer(new StringReader(s));
		this.invertedIndex = invertedIndex;
		this.numDocuments = numDocs;
		input.ordinaryChar('/');
		input.parseNumbers();

		curToken = readNextToken();
	}

	public Token useToken(String tok) {

		Token t = curToken;
		if (!t.equals(tok)) {
			System.out
					.println("Line " + input.lineno() + ": Expected \"" + tok + "\" but saw \"" + t.toString() + "\"");
		}
		curToken = readNextToken();
		return t;
	}

	public Token getToken() {
		return curToken;
	}

	public void eatToken() {
		curToken = readNextToken();
	}

	public boolean isEmpty() {
		return (curToken == null);
	}

	private Token readNextToken() {
		try {
			int c = input.nextToken();
			switch (c) {

			case StreamTokenizer.TT_EOF: {
				return null;
			}
			case StreamTokenizer.TT_WORD: {
				if (input.sval.equalsIgnoreCase("AND")) {
					return new Token("AND");
				}
				if (input.sval.equalsIgnoreCase("OR")) {
					return new Token("OR");
				}
				if (input.sval.equalsIgnoreCase("NOT")) {

					eatToken();
					Token token = getToken(); // this is a list
					if (token.isList()) {
						List<Integer> match = token.getValue();
						List<Integer> tempList = new ArrayList<Integer>();
						for (int i = 0; i < numDocuments; ++i) {
							tempList.add(i);
						}

						for (Integer i : match) {
							tempList.remove(i);
						}

						return new Token(tempList);
					} else {
						List<Integer> list = new ArrayList<Integer>();
						return new Token(list);
					}

				}
				FilesProcessor dp = new FilesProcessor();
				String term = dp.processTerm(input.sval);
				BinaryNodeInterface<String> node = invertedIndex.getNode(term);
				if (node != null) {
					return new Token(node.getPostingsDocumentIds());
				} else {
					List<Integer> list = new ArrayList<Integer>();
					return new Token(list);
				}
			}

			default: {
				return new Token((char) (input.ttype) + "");
			}
			}

		} catch (IOException e) {
			System.err.println("Input problem!");
			e.printStackTrace();
			return curToken;
		}
	}

}
