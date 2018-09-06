package com.out.sooraj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.*;

/**
 *
 * @author Sooraj
 */

public class Submit1 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
		String from=request.getParameter("From");
		String to=request.getParameter("To");
		//System.out.println(from+" "+to);
		String t=request.getParameter("Mode"); 
		String l2[]=Latlong.getLatLongPositions(to);
		String l1[]=Latlong.getLatLongPositions(from);
		//System.out.println("lat1 "+l1[0]+"lon1 "+l1[1]+"lat2 "+l2[0]+"lon2 "+l2[1]);
		String str=getpos(l1,l2,t);
		RouteArr r12[]=parse(str,l1,l2);
		HttpSession session =request.getSession();
        session.setAttribute("result", r12);
        RequestDispatcher dispatch = request.getRequestDispatcher("index2.jsp");
        dispatch.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        }
        catch (Exception ex) {
        	System.out.println("EERRRROOOORRRR: "+ex.toString());
            Logger.getLogger(Submit1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        }
        catch (Exception ex) {
        	System.out.println("EERRRROOOORRRR: "+ex.toString());
            Logger.getLogger(Submit1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    public String getpos(String l1[],String l2[],String t) throws Exception
    {
    	//System.out.println("test");
    	URL url;
    	HttpURLConnection conn = null;
    	int rs=0;
    	if(t.equals("Bus"))
    	{
    		url = new URL("http://localhost:8080/Minor_1/api/v1/bus?slat="+l1[0]+"&slong="+l1[1]+"&dlat="+l2[0]+"&dlong="+l2[1]);
    		System.out.println("url "+url);
    		conn = (HttpURLConnection) url.openConnection();
    		conn.setRequestMethod("GET");
    		conn.connect();
    		rs=conn.getResponseCode();
    	}
    	else if(t.equals("Metro"))
    	{
          	url = new URL("http://localhost:8080/Minor_1/api/v1/metro?slat="+l1[0]+"&slong="+l1[1]+"&dlat="+l2[0]+"&dlong="+l2[1]);
          	System.out.println("url "+url);
          	conn = (HttpURLConnection) url.openConnection();
          	conn.setRequestMethod("GET");
          	conn.connect();
          	rs=conn.getResponseCode();
    	}
     
    	String line;
    	String outputString="";
    	if(rs == 200)
    	{     
    		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    		while ((line = reader.readLine()) != null) {
    			outputString += line+"\n";
    		}
    	}
    	else
    		System.out.println("rs"+rs);
    	return outputString;
    }

    public RouteArr[] parse(String str,String l1[],String l2[]) throws JSONException, Exception
    {
	    JSONObject jsonObj = new JSONObject(str);
	    //System.out.println(jsonObj.toString());
	    JSONArray arr=jsonObj.getJSONArray("routes");
	    JSONArray path;
	    JSONObject obj1;
	    RouteArr r[]=new RouteArr[arr.length()];
    
	    int i,j;
	    for(i=0;i<arr.length();i++)
	    {
	        obj1=arr.getJSONObject(i);
	        path=obj1.getJSONArray("path");
	        System.out.println(path.toString());
	        r[i]=new RouteArr(path.length());
	        for(j=0;j<path.length();j++)
	        {
	            r[i].p[j]=new Path();
	            r[i].p[j].tolat=path.getJSONObject(j).getDouble("to_lat");
	            r[i].p[j].no=path.getJSONObject(j).getInt("no_of_stops");
	            r[i].p[j].bus=path.getJSONObject(j).getString("bus");
	            r[i].p[j].tdist=path.getJSONObject(j).getDouble("to_distance");
	            r[i].p[j].pno=path.getJSONObject(j).getInt("path_no");
	            r[i].p[j].from=path.getJSONObject(j).getString("from");
	            r[i].p[j].flong=path.getJSONObject(j).getDouble("from_long");
	            r[i].p[j].tlong=path.getJSONObject(j).getDouble("to_long");
	            r[i].p[j].to=path.getJSONObject(j).getString("to");
	            r[i].p[j].type=path.getJSONObject(j).getString("type");
	            r[i].p[j].flat=path.getJSONObject(j).getDouble("from_lat");
	            r[i].p[j].fdist=path.getJSONObject(j).getDouble("from_distance");    
	            //System.out.println("tolat "+r[i].p[j].tolat+" to long "+ r[i].p[j].tlong);
	            //System.out.println("fromlat "+r[i].p[j].flat+" fromlon"+r[i].p[j].flong);
	            
	            //r[i].p[j].tim=getval(r[i].p[j].flat,r[i].p[j].flong,r[i].p[j].tolat,r[i].p[j].tlong,1);
	            r[i].p[j].tim=0;
	            //System.out.println("time "+r[i].p[j].tim);
	        }
	        //r[i].walk=getval(Double.parseDouble(l1[0]),Double.parseDouble(l1[1]),r[i].p[0].flat,r[i].p[0].flong,2);
	        //r[i].walk+=getval(Double.parseDouble(l2[0]),Double.parseDouble(l2[1]),r[i].p[path.length()-1].flat,r[i].p[path.length()-1].flong,2);
	      	r[i].walk=0;
	        r[i].rno=obj1.getInt("route_no");
	    }
	   return r;      
	}
    
	public int parse2(String str,int ty) throws JSONException
	{
	    JSONObject jsonObj = new JSONObject(str);
	    JSONArray arr=jsonObj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");
	    int cnt=0;
	    if(!arr.getJSONObject(0).getString("status").equals("OK"))
        {
            cnt=-1;
            return cnt;
        }
	    if(ty==1)
	    {
	    	cnt=arr.getJSONObject(0).getJSONObject("duration").getInt("value");
	    }
	    else if(ty==2){
	    	cnt=arr.getJSONObject(0).getJSONObject("distance").getInt("value")/1000;
	    }
	    return cnt;
	}
	
	public int getval(double lat1,double lon1,double lat2,double lon2,int ty) throws Exception
	{
		int rs=0;
		String l1=lat1+","+lon1;
		String add=lat2+","+lon2;
        URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins="+ URLEncoder.encode(l1+"", "UTF-8")+"&destinations="+URLEncoder.encode(add+"", "UTF-8")+"&mode=transit&key=AIzaSyBuJbLGReZLB1UMNUxAuXE3WUPMUVd9Sps");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        rs=conn.getResponseCode();
        String line, outputString = "";
        if(rs == 200)
        {
        	
        	BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        	while ((line = reader.readLine()) != null) {
        		outputString += line+"\n";
        	}
        }
        else
        	System.out.println("rs"+rs);
        int k=parse2(outputString,ty);
        return k;
	}
}
