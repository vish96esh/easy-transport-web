/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.out.sooraj;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
/**
 *
 * @author Sooraj
 */
//@WebServlet(name = "Sort", urlPatterns = {"/Sort"})
public class Sort extends HttpServlet {

    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
		HttpSession session = request.getSession(false);
		RouteArr r1[]=(RouteArr[])session.getAttribute("array");
		RouteArr r;
        String s=request.getParameter("s");
        //System.out.println("s"+s);
        if(s.equals("t")){
        
        	int f=0;
       
        	for (int i = 0; i < r1.length; i++) 
        	{
        		for (Path p : r1[i].p) {
        			f += p.tim;
        		}
        		int n=0;
                for (int j = i + 1; j < r1.length; j++) 
                {
                	for(int k=0;k<r1[j].p.length;k++)
                		n+=r1[i].p[k].tim;
            		if (f>n) 
            		{
            			r = r1[i];
            			r1[i] = r1[j];
            			r1[j] = r;
            		}
                }
        	}   
        }
        else if(s.equals("p"))
        {
            int f=0,p1=0;
            for (int i = 0; i < r1.length; i++) 
            {
            	for (Path p : r1[i].p) {
            		f += p.no;
            	}
            	if(f>0 && f<=10)
            		p1=5;
            	else if(f>10&&f<=20)
            		p1=10;
            	else
            		p1=15;
            	int n=0,p2;
            	for (int j = i + 1; j < r1.length; j++) 
            	{
            		for(int k=0;k<r1[j].p.length;k++)
            			n+=r1[i].p[k].tim;
            		if(n>0 && n<=10)
            			p2=5;
            		else if(n>10&&n<=20)
            			p2=10;
            		else
            			p2=15;
            		if (p1>p2) 
            		{
            			r = r1[i];
            			r1[i] = r1[j];
            			r1[j] = r;
            		}
            	}
            }
            
        }
        else if(s.equals("c"))
        {
           // int f=0;
        	for (int i = 0; i < r1.length; i++) 
        	{
            
           // int n=0;
        		for (int j = i + 1; j < r1.length; j++) 
        		{
                    
        			if (r1[i].walk>r1[j].walk) 
        			{
        				r = r1[i];
        				r1[i] = r1[j];
        				r1[j] = r;
        			}
        		}
        	}
        }
        session.setAttribute("result1", r1);
        RequestDispatcher dispatch = request.getRequestDispatcher("index3.jsp");
        dispatch.forward(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(Sort.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(Sort.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>


}
