<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : Homepage
    Created on : 09-Oct-2015, 21:54:15
    Author     : jtony_000
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Ranked Search</title>
</head>
<body>  
    <div id="indexCentreColumn">
    <a href="booleansearch">Boolean Search</a><br>
    <a href="phrasesearch">Phrase Search</a> <br>
    <a href="bm25rankedsearch">BM25 Ranked Search</a>
    <form action='rankedsearch' method="post">
    Ranked Vector Model Search:<br>
    Term Frequency (tf) = count of occurences of a term in a document<br>
    Normalised Term Frequency (ntf(t)) = tf(d)/|V(d)|<br>
    Inverse Document Frequency (idf(t)) = Log10(Total Document Count / document frequency(t))<br>
    Weight of a term in the query w(t,q) = tf(q) * idf(t)<br>
    
    Rank is determined by the sum of all weights i.e. the dot product of the vectors<br>
    Example Query: dog cat pet<br><br>    	
		<div>
	        Query:<input type="text"
	               size="100"
	               maxlength="255"
	               name="searchterms">
	      	<input type="submit" value="Search">
       	</div> 
       	
    </form>
    ${query}    
            <div>
            <c:forEach var="doc" items="${relevantDocs}">
                  <div>
                        found ${doc.documentName} with score ${doc.documentScore}
                </div>
            </c:forEach>
        </div>
    </div>
    </body>
</html>
