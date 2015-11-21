<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!--   <meta name="viewport" content="width=device-width, initial-scale=1"> -->
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <title>PR Tests</title>
<!--   <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script> -->
<!--   <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script> -->
<!--   <script language="javascript" type="text/javascript" src="jquery.min.js"></script> -->
  
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script> 
<script type="text/javascript" src="js/jquery-1.11.3.min.js"></script> 
<script type="text/javascript" src="js/jquery.canvasjs.min.js"></script> 
<script type="text/javascript">
window.onload = function() {
	$("#chartContainer").CanvasJSChart({ 
		zoomEnabled: true,
		title: { 
			text: "Precision Recall Plot for Query 1", 
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
    <div id="indexCentreColumn">
     <a href="rankedsearch">Simple Ranked Search</a> <br>
     <a href="phrasesearch">Phrase Search</a> <br>
     <a href="bm25rankedsearch">BM25 Ranked Search</a><br>
          <form action='queryprecisionrecall' method="post">
		<div>	
			<input type="text" class="form-control" name="queryNum" value="${queryNum}">
	      	<input type="submit" class="btn btn-success" value="Next">
       	</div> 
    </form> 
               
    </div>
    <div id="chartContainer" style="width: 80%; height: 75%"></div>
    </body>
</html>