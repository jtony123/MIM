<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%-- 

@author Anthony Jackson
@id 11170365
4BCT
 
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<title>Phrase Search</title>

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="js/jquery.canvasjs.min.js"></script>
</head>
<body>

	<div class="container">
		<div class="jumbotron">
			<div class="text-center">
				<h2>CT422 Development Project - IR System</h2>
			</div>
			<div class="form-inline">
				<div class="form-group">
					<form action='startnewtest' method="get">
						<input type="submit" class="btn btn-info" value="Run New Test">
					</form>
				</div>
				<div class="form-group">
					<form action='bm25rankedsearch' method="get">
						<input type="submit" class="btn btn-info" value="BM25 Search">
					</form>
				</div>
				<div class="form-group">
					<form action='rankedsearch' method="get">
						<input type="submit" class="btn btn-info"
							value="Vector Space Search">
					</form>
				</div>
				<div class="form-group">
					<form action='phrasesearch' method="get">
						<input type="submit" class="btn btn-info" value="Phrase Search">
					</form>
				</div>
				<div class="form-group">
					<form action='booleansearch' method="get">
						<input type="submit" class="btn btn-info" value="Boolean Search">
					</form>
				</div>
			</div>
			<br> <br> <br>
			<form action='phrasesearch' method="post">
				Phrase Search:<br> Example Query: dog cat pet<br> <br>
				<div>
					Query:<input type="text" size="100" maxlength="255"
						name="searchterms">
					<button type="submit" class="btn btn-success">Search</button>
				</div>
			</form>
			<h3>Ranked Results</h3>
			<table class="table">
				<thead>
					<tr>
						<th>Rank</th>
						<th>Document Id</th>
						<th>Score</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="doc" items="${docsToReturn}" varStatus="loop">
						<tr>
							<td>${loop.index +1}</td>
							<td>${doc.documentId}</td>
							<td>${doc.documentScore}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>