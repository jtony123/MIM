package fileprocessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Anthony Jackson
 * @id 11170365
 *	4BCT
 */

public class FileLoader {
	
	private Document document;
		
	public FileLoader(){
		document = new Document();
	}	
	
	/**
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}


	/**
	 * @param document the document to set
	 */
	public void setDocument(Document document) {
		this.document = document;
	}


	public void readFiles(File file, Document document){		
	
        BufferedReader br = null;
        StringBuilder sb = null;
       
    	document.setFilePath(file.getPath());
    	document.setDocumentName(file.getName());
    	
		try {    			
			String sCurrentLine;    			
			br = new BufferedReader(new FileReader(file.getPath()));
			sb = new StringBuilder();
	
			while ((sCurrentLine = br.readLine()) != null) {
				sb.append(sCurrentLine).append(" ");   				
			}			
			document.setFullText(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
        	
	}
}
