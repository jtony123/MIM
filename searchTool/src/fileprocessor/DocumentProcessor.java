package fileprocessor;

import java.io.File;
import datastructures.AVLTree;

public class DocumentProcessor {
	
	private Document document;

	public Document getDocument() {
		
		return this.document;
	}
	
	public void process(File file, AVLTree<String> tree) {

		// read in the file
		FilesReader filesReader = new FilesReader();
		filesReader.readFiles(file);
		document = filesReader.getDocument();
		
		System.out.println("Full text : " + document.getFullText());
		String[] words = document.getFullText().split(",|'|\\.|\\s");
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<words.length;++i){			
			// Singularize and stem the words
			Inflector inflector = new Inflector();	
			Stemmer stemmer = new Stemmer();
			//put everything in lowercase
			words[i] = words[i].toLowerCase();
			
			if(!tree.contains(words[i])){
				String singularForm = inflector.singularize(words[i]);
				stemmer.add(singularForm);
				sb.append(stemmer.stem().toString()).append(" ");
				stemmer.clear();
			}
		}
		document.setProcessedText(sb.toString());
		System.out.println("Processed as : " + document.getProcessedText());
		
		
	}

}
