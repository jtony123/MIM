<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : Homepage
    Created on : 31-Oct-2015, 21:54:15
    Author     : jtony_000
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BM 25</title>
</head>
<body>  
    <div id="indexCentreColumn">
    <a href="booleansearch">Boolean Search</a><br>
    <a href="rankedsearch">Simple Ranked Search</a><br>
    <a href="phrasesearch">Phrase Search</a> 
    <form action='bm25rankedsearch' method="post">
    BM25 ranked search:<br>
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
