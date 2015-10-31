<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : Homepage
    Created on : 30-Oct-2015, 11:04:15
    Author     : jtony_000
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Phrase Search</title>
</head>
<body>  
    <div id="indexCentreColumn">
     <a href="rankedsearch">Simple Ranked Search</a><br>
     <a href="booleansearch">Boolean Search</a> <br>
     <a href="bm25rankedsearch">BM25 Ranked Search</a>
    <form action='phrasesearch' method="post">
    Boolean Search: <br>
    Example: "computer science" <br>
    	
		<div>
	        Query:<input type="text"
	               size="100"
	               maxlength="255"
	               name="searchterms">
	      	<input type="submit" value="Search">
       	</div> 
       	
    </form>
    ${errormessage}${query}    
            <div>
            <c:forEach var="doc" items="${docsToReturn}">
                  <div>
                        ${doc.documentName} with score ${doc.documentScore}
                </div>
            </c:forEach>
        </div>
    </div>
    </body>
</html>
