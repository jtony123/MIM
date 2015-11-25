package testcorpushandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import datastructures.AVLTree;
import fileprocessor.Document;
import fileprocessor.Inflector;
import fileprocessor.Stemmer;

/**
 * @author Anthony Jackson
 * @id 11170365
 *	4BCT
 */

public class DocumentBuilder {

	public String fullFile;
	List<Document> documents = new ArrayList<Document>();
	AVLTree<String> stopwordTree;

	public DocumentBuilder(String corpus, AVLTree<String> stopWordTree) {
		this.stopwordTree = stopWordTree;
		this.fullFile = corpus;
		try {
			buildDocumentCollection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<Document> getDocuments() {
		return (ArrayList<Document>) documents;
	}

	public void buildDocumentCollection() throws Exception {

		File file = new File(fullFile);
		FileInputStream inputStream = new FileInputStream(file);
		InputStreamReader streamReader = new InputStreamReader(inputStream, "UTF-8");
		BufferedReader reader = new BufferedReader(streamReader);

		StringBuilder sb = new StringBuilder();
		Document doc = new Document();
		doc.setDocumentId(1);
		
		for (String line; (line = reader.readLine()) != null;) {
			if (line.startsWith(".I")) {
				// this is the start of the next document
				String filename = line.substring(3);
				filename.trim();

				int id = Integer.parseInt(filename);
				doc.setDocumentId(id);
				doc.setFullText(sb.toString());

				String[] terms = doc.getFullText().split(",|'|\\.|\\s");

				for (int i = 0; i < terms.length; ++i) {
					// Singularize and stem the words
					Inflector inflector = new Inflector();
					Stemmer stemmer = new Stemmer();
					// put everything in lowercase
					terms[i] = terms[i].toLowerCase();

					if (!stopwordTree.contains(terms[i])) {
						String singularForm = inflector.singularize(terms[i]);
						stemmer.add(singularForm);
						doc.addToken(stemmer.stem().toString());
						stemmer.clear();
					}
				}

				documents.add(doc);

				sb = new StringBuilder();
				doc = new Document();
			} else if (line.startsWith(".W")) {
				// skip this line
			} else {
				sb.append(line + " ");

			}
		}
		documents.add(doc);

		reader.close();
		streamReader.close();
		inputStream.close();
	}

}
