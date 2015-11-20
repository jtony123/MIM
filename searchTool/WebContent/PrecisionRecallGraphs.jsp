<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!--   <meta name="viewport" content="width=device-width, initial-scale=1"> -->
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<title>PR Tests</title>
</head>
<body>  
    <div id="indexCentreColumn">
     <a href="rankedsearch">Simple Ranked Search</a> <br>
     <a href="phrasesearch">Phrase Search</a> <br>
     <a href="bm25rankedsearch">BM25 Ranked Search</a><br>
      
     Example format  C:\\a_med\\files<br>
    <form action='testcollection' method="post">
		<div>		
	        Path to test collection:
	        <input type="text" size="100" maxlength="255" name="path">
	         <label class="radio-inline" active>
	         	<input type="radio" name="optradio" checked="">Vector Space Ranked
	         	</label>
			<label class="radio-inline">
				<input type="radio" name="optradio">BM25 Ranked
				</label>
	      	<input type="submit" class="btn btn-success" value="Run Test">
       	</div> 
    </form>    
            <div>
            <c:forEach var="doc" items="${matchingDocuments}">
                  <div>
                        ${doc.documentName}
                </div>
            </c:forEach>
        </div>
    </div>
    </body>
</html>