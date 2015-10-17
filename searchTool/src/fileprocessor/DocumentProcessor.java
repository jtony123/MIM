package fileprocessor;

import java.io.File;
import datastructures.AVLTree;

public class DocumentProcessor {
	
	//private Document document = new Document();
	
//	public DocumentProcessor(){
//		document = new Document();
//	}

//	public Document getDocument() {
//		
//		return this.document;
//	}
	
	public String[] processQuery(String query){
		
		String[] terms = query.split(",|'|\\.|\\s");
		for(int i=0;i<terms.length;++i){			
			// Singularize and stem the words
			Inflector inflector = new Inflector();	
			Stemmer stemmer = new Stemmer();
			//put everything in lowercase
			terms[i] = terms[i].toLowerCase();
			
			String singularForm = inflector.singularize(terms[i]);
			stemmer.add(singularForm);
			terms[i] = stemmer.stem().toString();
			stemmer.clear();			
		}
		return terms;
	}
	
	public String processTerm(String term){
		
		Inflector inflector = new Inflector();	
		Stemmer stemmer = new Stemmer();
		//put everything in lowercase
		term = term.toLowerCase();
		
		String singularForm = inflector.singularize(term);
		stemmer.add(singularForm);
		term = stemmer.stem().toString();
		stemmer.clear();
		return term;
	}
	
	public Document processFile(File file, AVLTree<String> tree) {
		Document document = new Document();
		// read in the file
		FilesReader filesReader = new FilesReader();
		filesReader.readFiles(file, document);
		//document = filesReader.getDocument();
		
		System.out.println("Full text : " + document.getFullText());
		String[] terms = document.getFullText().split(",|'|\\.|\\s");
		//StringBuilder sb = new StringBuilder();
		for(int i=0;i<terms.length;++i){			
			// Singularize and stem the words
			Inflector inflector = new Inflector();	
			Stemmer stemmer = new Stemmer();
			//put everything in lowercase
			terms[i] = terms[i].toLowerCase();
			
			if(!tree.contains(terms[i])){
				String singularForm = inflector.singularize(terms[i]);
				stemmer.add(singularForm);
				//sb.append(stemmer.stem().toString()).append(" ");
				document.addToken(stemmer.stem().toString());
				stemmer.clear();
			}
		}
		//document.setProcessedText(sb.toString());
		//System.out.println("Processed as : " + document.getProcessedText());
		return document;
		
		
	}

}
