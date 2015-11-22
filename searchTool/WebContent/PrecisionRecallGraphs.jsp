<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<title>PR Tests</title>

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="js/jquery.canvasjs.min.js"></script>
<script type="text/javascript">
window.onload = function() {
	$("#chartContainer").CanvasJSChart({ 
		zoomEnabled: true,
		title: { 
			text: "Precision Recall Plot", 
			fontSize: 22 
		}, 
		axisY: { 
			title: "Precision", 
	        labelFontColor: "#369EAD" ,
	        lineColor: "#369EAD" ,
	        lineThickness: 3,
			includeZero: true, 
			maximum:100,
			
		}, 
		axisX: { 
			title: "Recall",
			interval: 10, 
			//intervalType: "day", 
			//valueFormatString: "DAY", 
			//labelAngle: -45 
		}, 
		data: [ 
		{ 
			type: "line",
			toolTipContent: "{y}%",
			dataPoints: [ 
			             
			      <c:forEach items="${precisionList}" var="pr">
                     {x: parseInt("${pr.rec}"), y: parseInt("${pr.prec}")},
                  </c:forEach>      
			] 
		}		
		] 
	});
	}
	</script>

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
			<br>
			<div class="form-inline">
				<div class="form-group">
					<h3>Performance Figures</h3>
				</div>
				<div class="form-group">
					Time to build IR system <input type="text" class="form-control"
						name="" value="${IndexBuildTime}ms">
				</div>
				<div class="form-group">
				Time to run Queries Test
					<input type="text" class="form-control" name=""
						value="${TestQueryTime}ms">
				</div>
			</div>
			<br> <br> <br>


			<div class="row">
				<div class="col-sm-4">
					
					<div class="form-inline">
						<div class="form-group">
							<form action='prevqueryprecisionrecall' method="post">
								<input type="hidden" class="form-control" name="qrynumber"
									value="${queryNum}">
								<button type="submit" class="btn btn-success">Previous</button>
							</form>
						</div>
						<div class="form-group">
							<input type="text" class="form-control" name="qrynumber"
								value="Query Number ${queryNum}">
						</div>
						<div class="form-group">
							<form action='nextqueryprecisionrecall' method="post">
								<input type="hidden" class="form-control" name="qrynumber"
									value="${queryNum}">
								<button type="submit" class="btn btn-success">Next</button>
							</form>
						</div>

					</div>
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
							<c:forEach var="doc" items="${docQryScores}" varStatus="loop">
								<tr>
									<td>${loop.index +1}</td>
									<td>${doc.docId}</td>
									<td>${doc.score}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

				</div>
				<div class="col-sm-8">
					<h3>Precision Recall Graph</h3>
					<div id="chartContainer"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>