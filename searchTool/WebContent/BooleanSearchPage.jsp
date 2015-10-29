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
<title>Boolean Search</title>
</head>
<body>  
    <div id="indexCentreColumn">
     <a href="rankedsearch">Simple Ranked Search</a> 
    <form action='booleansearch' method="post">
    Boolean Search: <br>
    Examples: dog; <br>
    			(dog AND cat); <br>
    			(dog AND NOT cat); <br>
    			((dog AND cat) OR pet)<br>
    			((dog OR cat) AND NOT pet)<br><br>
    	
		<div>
	        Query:<input type="text"
	               size="50"
	               maxlength="45"
	               name="searchterms">
	      	<input type="submit" value="Search">
       	</div> 
       	
    </form>
    ${errormessage}${queryterms}    
            <div>
            <c:forEach var="doc" items="${matchingDocuments}">
                  <div>
                        ${doc.documentName} can be found here 
                </div>
            </c:forEach>
        </div>
    </div>
    </body>
</html>
