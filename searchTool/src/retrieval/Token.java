package retrieval;

import java.util.List;

/**
 * @author Anthony Jackson
 * @id 11170365
 *	4BCT
 */

public class Token {


    private String name;
    private List<Integer> value;
    private boolean isList;
    
    
    public Token( String s ) {
    	isList = false;
        name = s;
    }

    public Token (List<Integer> list){
    	isList = true;
    	value = list;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getValue() {
        return value;
    }

    public boolean isList() {
        return isList;
    }

    public boolean isVariable() {
        return ! isList;
    }

    public boolean equals(String s) {
        return (! isList && name.equals(s));
    }

    public String toString() {
        return isList ? value.toString() : name;
    }
}

