<%@ page import="com.out.sooraj.RouteArr"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<!DOCTYPE html>  
<html lang="en">  
<head>  
    <meta charset="utf-8">
     <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Google Maps Example</title>  
    <style type="text/css">  
      html { height: 100% }  
      body { height: 50%; margin: 0; padding: 0 }  
      #map_canvas { height: 30% }        
    </style>  
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <script src='http://code.jquery.com/jquery.min.js' type='text/javascript'></script>  
 <% 
     RouteArr r[]=(RouteArr[])session.getAttribute("result");
        int i=Integer.parseInt(request.getParameter("q"));
%>
</head>  
<body>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?key=AIzaSyBuJbLGReZLB1UMNUxAuXE3WUPMUVd9Sps&AMP&sensor=false"></script>  
<script type="text/javascript">  
var infowindow = null;  
    $(document).ready(function () { initialize();  });  
  
    function initialize() {  
  
         var map;
      var mapOptions = { center: new google.maps.LatLng(28.7041,77.1025), zoom: 11,
        mapTypeId: google.maps.MapTypeId.ROADMAP };

      
        map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
     
var userCoor =  [['anand vihar', 28.6502, 77.3027, 4],
      ['karol bagh', 28.6528, 77.1921, 5]
          ];

var userCoorPath =  [
          {lat: 28.6502, lng: 77.3027},
          {lat: 28.6528, lng: 77.1921}
          
        ];

var userCoordinate = new google.maps.Polyline({
path: userCoorPath,
strokeColor: "#FF0000",
strokeOpacity: 1,
strokeWeight: 2
});
userCoordinate.setMap(map);

var infowindow = new google.maps.InfoWindow();

var marker, i;

for (i = 0; i < userCoor.length; i++) {  
  marker = new google.maps.Marker({
    position: new google.maps.LatLng(userCoor[i][1], userCoor[i][2]),
    //title:"bus"
	map: map
	
  });


  google.maps.event.addListener(marker, 'click', (function(marker, i) {
    return function() {
      infowindow.setContent(userCoor[i][0]);
      infowindow.open(map, marker);
    };
  })(marker, i));


}


     }
</script>  

<div id="map_canvas" style="width:100%; height:100%"></div>  

<div class="row">
   <%int p2=0;
       for(int j=0;j<r[i].p.length;j++){
      int n=r[i].p[j].no;
      
      if(n>0 && n<=10)
                p2=5;
            else if(n>10&&n<=20)
                p2=10;
            else
                p2=15;
            
   %>
    <div class="col-sm-12 col-lg-12 col-md-12">
         <div class="thumbnail">
             
             <div class="caption">
                 <h4>FRom bus stand 
                    <%=r[i].p[j].from%> 
                 </h4>
                 <h4>To bus stand 
                    <%=r[i].p[j].to%> 
                 </h4>
                 <h4>number of bus stop 
                    <%=r[i].p[j].no%> 
                 </h4>
                 <h4>With bus no. 
                    <%=r[i].p[j].bus%> 
                 </h4>
                 <h4>Walking to reach source bus stand
                    <%=r[i].p[j].fdist%> 
                 </h4>
                 <h4>walking distance to reach destination from last bus stand 
                    <%=r[i].p[j].tdist%> 
                 </h4>
                 <h4>Travel time 
                    <%=r[i].p[j].tim%> 
                 </h4>
                 <h4>Expected Fare
                    <%=p2%> 
                 </h4>
                 
                 
             </div>
             
             </div>
    </div>
             <%}%>
</div>
                 </body>
</html> 