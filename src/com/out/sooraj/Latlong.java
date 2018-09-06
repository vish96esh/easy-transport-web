/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.out.sooraj;


import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import org.w3c.dom.Document;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class will get the lat long values.
 */
public class Latlong
{
     public static ResultSet rs=null;
     public static Statement stmt,stmt1;
     static Connection con;
    
     public static void main(String[] args) throws Exception
     {
    	 System.setProperty("java.net.useSystemProxies", "true");
    	 System.out.println("Please enter a location:");
    	 try{  
    		 Class.forName("com.mysql.jdbc.Driver");  
    		 con=DriverManager.getConnection("jdbc:mysql://localhost:3306/minor","root","vishesh");  
    		 stmt=con.createStatement();
    		 stmt1=con.createStatement();
    	 }
    	 catch(SQLException se)
    	 {
    		 System.out.println("SQL Erro");
    	 }
    	 catch(ClassNotFoundException e)
    	 {	
    		 System.out.println(e);
    	 } 
                
    	 String sql = "SELECT id,name  FROM stops where latitude IS NULL or longitude IS NULL";
    	 rs=stmt.executeQuery(sql);
                
    	 while(rs.next())
    	 {
    	  
    		 String  latLongs[] = getLatLongPositions(rs.getString("name")+"delhi india");
    		 if(!latLongs[0].equals("") ||!latLongs[1].equals("") ){
    			 try{
    				 String sql1 = "UPDATE stops " +  "SET latitude =?, longitude=? WHERE id=?";
    				 PreparedStatement p = con.prepareStatement(sql1);
    				 p.setDouble(1,Double.parseDouble(latLongs[0]));
    				 p.setDouble(2,Double.parseDouble(latLongs[1]));
    				 p.setInt(3, rs.getInt("id"));
    				 p.executeUpdate();
    				 //stmt1.executeUpdate(sql);
    			 }
    			 catch(NullPointerException e1)
    			 {
    				 System.out.println("null pointer");
    			 }
    			 catch(SQLException see)
    			 {
    				 System.out.println("update query error");
    			 }
    			 System.out.println("add"+rs.getString("name"));
    			 System.out.println("lat"+latLongs[0]+" long"+latLongs[1]);
    		 }
    		 else
    		 {
    			 String sql1 = "UPDATE rost_stage " + "SET latitude =?, longitude=? WHERE id=?";
    			 PreparedStatement p = con.prepareStatement(sql1);
    			 p.setDouble(1,28.7018380);
    			 p.setDouble(2,77.1593116);
    			 p.setInt(3, rs.getInt("id"));
    			 p.executeUpdate();
    			 System.out.println("add"+rs.getString("name"));
    			 System.out.println("lat none long");
    		
    		 }
    	 }
     }

     public static String[] getLatLongPositions(String address) throws Exception
     {
    	 String latitude="",longitude="";
    	 int responseCode = 0;
    	 String api = "http://maps.googleapis.com/maps/api/geocode/xml?address="+URLEncoder.encode(address, "UTF-8")+"&sensor=true";
    	 //System.out.println("URLk : "+api);
    	 URL url = new URL(api);
    	 HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
    	 httpConnection.connect();
    	 responseCode = httpConnection.getResponseCode();
    	 if(responseCode == 200)
    	 {
    		 DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    		 Document document = builder.parse(httpConnection.getInputStream());
    		 XPathFactory xPathfactory = XPathFactory.newInstance();
    		 XPath xpath = xPathfactory.newXPath();
    		 XPathExpression expr = xpath.compile("/GeocodeResponse/status");
    		 String status = (String)expr.evaluate(document, XPathConstants.STRING);
    		 try{
    			 if(status.equals("OK"))
    			 {
    				 expr = xpath.compile("//geometry/location/lat");
    				 latitude = (String)expr.evaluate(document, XPathConstants.STRING);
    				 expr = xpath.compile("//geometry/location/lng");
    				 longitude = (String)expr.evaluate(document, XPathConstants.STRING);
    				 //System.out.println("lat= "+latitude+" long="+longitude);
    				 return new String[] {latitude, longitude};
    				 
    			 }
    			 else
    			 {
    				 throw new Exception("Error from the API - response status: "+status);     
    			 }
    		 }      
    		 catch(Exception r)
    		 {System.out.println("connection error http");}
    	 }
    	 return new String[] {latitude, longitude};
     }
}