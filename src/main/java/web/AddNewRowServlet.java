package web;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.Table;
import dbtype.Attribute;

/**
 * Servlet implementation class AddNewRowServlet
 */
public class AddNewRowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddNewRowServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) {
    	String tableName = request.getParameter("tableName");
    	
    	String inputLine;
    	StringBuilder stringBuilder = new StringBuilder();
    	try (BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
    		while ((inputLine = br.readLine()) != null) {
    			stringBuilder.append(inputLine);
    			stringBuilder.append(System.lineSeparator());
    		}
        } catch (IOException e) {
			e.printStackTrace();
		}
    	
    	Common.server.addNewRow(tableName, stringBuilder.toString());
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(request, response);
	}

}
