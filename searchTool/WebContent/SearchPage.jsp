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
<title>CT422 SearchTool</title>
</head>
<body>  
    <div id="indexCentreColumn">
    <form action='search' method="post">
    	Terms:
		<div>
	        <input type="text"
	               size="31"
	               maxlength="45"
	               name="searchterms">
	      	<input type="submit" value="Search">
       	</div> 
       	
    </form>
    ${terms}    
            <div>
            <c:forEach var="doc" items="${matchingDocs}">
                  <div>
                        ${doc.name} can be found here 
                        <a href="${doc.url}">
                        </a>
                </div>
            </c:forEach>
        </div>
    </div>
    </body>
</html>
