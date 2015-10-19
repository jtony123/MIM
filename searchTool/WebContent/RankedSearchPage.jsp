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
    <form action='rankedsearch' method="post">
    Ranked Vector Model Search: Rank is determined by the sum of all (term frequency over normalised document vector)<br>
    Examples: dog cat pet<br><br>    	
		<div>
	        Query:<input type="text"
	               size="50"
	               maxlength="45"
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
