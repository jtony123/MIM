<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!--   <meta name="viewport" content="width=device-width, initial-scale=1"> -->
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
					<td>The collection of documents</td>
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
			<br><br><br>

			<!-- 			<form action='testcollection' method="post"> -->
			<!-- 				<div> -->
			<!-- 					Enter path to test collection (Eg. C:\\MyFiles\\Folder):  -->
			<!-- 					<input type="text" size="50" maxlength="255" name="path"> -->
			<!-- 					<input type="submit" class="btn btn-success" value="Run Test" style="width: 100px;" onclick="$('#loading').show();" /> -->
			<!-- 				</div> -->
			<!-- 			</form> -->
			<table>
				<tbody>
					<tr>
						<td>The collection will be parsed into separate documents
							with terms being stemmed and all punctuation removed. The system
							will build an inverted index for all terms excluding stop words,
							contained across the collection.<br> When the IR system is
							built, each of the queries is submitted to the system, and precision 
							recall graphs generated for the documents returned for each
							query.
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