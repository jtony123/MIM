package testcorpushandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import datastructures.AVLTree;
import fileprocessor.Document;
import fileprocessor.Inflector;
import fileprocessor.Stemmer;


/**
 * 
 */

/**
 * @author Anthony Jackson
 * @id 11170365
 *
 */
public class DocumentBuilder {

	
	public static String fullFile = "C:\\a_med\\MED.ALL";
	public static String destinationFolder = "C:\\a_med\\files\\";
	List<Document> documents = new ArrayList<Document>();
	AVLTree<String> stopwordTree;
	
	public DocumentBuilder(String corpus, AVLTree<String> stopWordTree){
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



	public void buildDocumentCollection() throws Exception{
		
		File file = new File(fullFile);
		FileInputStream inputStream = new FileInputStream(file);
		InputStreamReader streamReader = new InputStreamReader(inputStream, "UTF-8");
		BufferedReader reader = new BufferedReader(streamReader);
		
		StringBuilder sb = new StringBuilder();
		Document doc = new Document();
		doc.setDocumentId(1);
		//PrintWriter out = new PrintWriter(destinationFolder+"1.txt", "UTF-8");
		
		for (String line;(line = reader.readLine()) != null;) {
		   if (line.startsWith(".I")) {
			   // this is the start of the next document
		    	String filename = line.substring(3);
		    	filename.trim();
		    	//filename=destinationFolder+filename+".txt";
		    	int id = Integer.parseInt(filename);
		    	doc.setDocumentId(id);
		    	doc.setFullText(sb.toString());
		    	
				String[] terms = doc.getFullText().split(",|'|\\.|\\s");

				for(int i=0;i<terms.length;++i){			
					// Singularize and stem the words
					Inflector inflector = new Inflector();	
					Stemmer stemmer = new Stemmer();
					//put everything in lowercase
					terms[i] = terms[i].toLowerCase();
					
					if(!stopwordTree.contains(terms[i])){
						String singularForm = inflector.singularize(terms[i]);
						stemmer.add(singularForm);
						doc.addToken(stemmer.stem().toString());
						stemmer.clear();
					}
				}
		    	
		    	documents.add(doc);
		    	
		        //out.flush();
		        //out.close();
		        //out = new PrintWriter(filename, "UTF-8");
		    	sb = new StringBuilder();
		        doc = new Document();
		    } else if(line.startsWith(".W")){
		    	// skip this line
		    } else {
		    	sb.append(line + " ");
		    	
		        //out.println(line);
		    }
		}
		documents.add(doc);
		
		//out.flush();
		//out.close();
		reader.close();
		streamReader.close();
		inputStream.close();
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		File file = new File(fullFile);
		FileInputStream inputStream = new FileInputStream(file);
		InputStreamReader streamReader = new InputStreamReader(inputStream, "UTF-8");
		BufferedReader reader = new BufferedReader(streamReader);
		PrintWriter out = new PrintWriter(destinationFolder+"1.txt", "UTF-8");
		for (String line;(line = reader.readLine()) != null;) {
		   if (line.startsWith(".I")) {
		    	String filename = line.substring(3);
		    	filename.trim();
		    	filename=destinationFolder+filename+".txt";
		        out.flush();
		        out.close();
		        out = new PrintWriter(filename, "UTF-8");
		    } else if(line.startsWith(".W")){
		    	// skip this line
		    } else {
		        out.println(line);
		    }
		}
		out.flush();
		out.close();
		reader.close();
		streamReader.close();
		inputStream.close();
	}	
}
