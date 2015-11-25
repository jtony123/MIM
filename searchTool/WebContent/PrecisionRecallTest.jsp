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
<link rel="stylesheet"
	href="http://css-spinners.com/css/spinner/whirly.css" type="text/css">
<title>PR Tests</title>

<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/jquery.canvasjs.min.js"></script>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>

</head>
<body>

	<div class="container">
		<div class="jumbotron">
			<div class="text-center">
				<h2>CT422 Development Project - IR System</h2>
			</div>
			<br> <br>

			<table class="table table-bordered">
				<tr>
					<td>The collection of documents all within one file</td>
					<td>*.ALL</td>					
				</tr>
				<tr>
					<td>The file with the queries</td>
					<td>*.QRY</td>
				</tr>
				<tr>
					<td>The file with the list of relevant <br>documents for
						each query
					</td>
					<td>*.REL</td>
				</tr>

			</table>
			File structure/formatting must be similar to the MED collection.
			<br> <br>

			<div class="form-inline">
				<form action="upload" method="post" enctype="multipart/form-data">
					<div class="form-group">
						.ALL file<input type="file" name="allfile" />
					</div>
					<div class="form-group">
						.QRY file<input type="file" name="qryfile" />
					</div>
					<div class="form-group">
						.REL file<input type="file" name="relfile" />
					</div>
					<div class="form-group">
						<input type="submit" class="btn btn-success" value="Run Test"
							style="width: 100px;" onclick="$('#loading').show();" />
					</div>
				</form>
			</div>
			<br>
			<br>
			<br>
			<table>
				<tbody>
					<tr>
						<td>The collection will be parsed into separate documents
							with terms being stemmed and all punctuation removed. The system
							will build an inverted index for all terms excluding stop words,
							contained across the collection.<br> When the IR system is
							built, each of the queries is submitted to the system, and
							precision recall graphs generated for the documents returned for
							each query.<br> Once the test is complete, the user is free
							to submit queries manually to the system. <br>
							Four search
							implementations are supported - Simple Vector Space; BM25
							Ranking; Phrase Search; Boolean Search
						</td>
						<td align="center" valign="middle">
							<div id="loading" style="display: none;">
								<img src="img/loading_spinner.gif" alt="help" />
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>