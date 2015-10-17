package retrieval;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class BooleanIR {
	
	
	public List<Integer> doOR (List<Integer> list1, List<Integer> list2){
		
		List<Integer> matchedDocs = new ArrayList<Integer>();         
 		Set<Integer> set = new TreeSet<Integer>(list1);
 		set.addAll(list2);
 		matchedDocs.addAll(set);     	
		return matchedDocs;			
	}
	
	
	public List<Integer> doAND (List<Integer> list1, List<Integer> list2){
		
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


	public List<Integer> doNOT(List<Integer> list1, List<Integer> list2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
