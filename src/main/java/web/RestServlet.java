package web;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.Column;
import db.ColumnFactory;
import db.Table;
import dbtype.Attribute;

public class RestServlet extends HttpServlet {
	// GET:
	// getDB 	db
	// getTable db/tableName
	// search 	db/tableName/search
	//
	// POST:
	// createTable	db
	// addNewRow	db/tableName
	//
	// PUT:
	// editCell		db/tableName
	
	private static final long serialVersionUID = 1L;
       
    public RestServlet() {
        super();
        
        try {
			Common.server.getDB();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private static String[] parseUrl(String requestUrl) {
    	String requestString = requestUrl.substring(Common.serverLink.length() + "rest/".length());
    	return requestString.split("/");
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("get " + request.getRequestURL().toString());
		String[] urlParameters = parseUrl(request.getRequestURL().toString());
		
		if (urlParameters.length == 0 || !urlParameters[0].equals("db"))
			return;

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		if (urlParameters.length == 1) { // getDB
			Common.server.getDB().writeToPrintWriter(out);
		}
		else if (urlParameters.length == 2) { // get table
			String tableName = urlParameters[1];
			Common.server.getTable(tableName).writeToPrintWriter(out);
		}
		else if (urlParameters[2].equals("search")) { // search in table
			String tableName = urlParameters[1];
			ArrayList<String> fieldsSearch = new ArrayList<>();
	    	try (BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
				String inputLine;
	    		while ((inputLine = br.readLine()) != null) {
	    			fieldsSearch.add(inputLine);
	    		}
	        }
	    	
	    	Table searchResult = null;
			try {
				searchResult = Common.server.search(tableName, fieldsSearch);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			searchResult.writeToPrintWriter(out);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("post " + request.getRequestURL().toString());
		String[] urlParameters = parseUrl(request.getRequestURL().toString());
		
		if (urlParameters.length == 0 || !urlParameters[0].equals("db"))
			return;
	
		if (urlParameters.length == 1) { // create table
			String tableName = null;//urlParameters[1];
			ArrayList<Column> columns = new ArrayList<>();
	    	try (BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
	    		String inputLine;
	    		tableName = br.readLine();
	    		while ((inputLine = br.readLine()) != null) {
	    			try {
						columns.add(ColumnFactory.createColumn(inputLine));
					} catch (Exception e) {
						e.printStackTrace();
					}
	    		}
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
			
	    	try {
				Common.server.createTable(tableName, columns);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (urlParameters.length == 2) { // addNewRow
			String tableName = urlParameters[1];
			ArrayList<String> values = new ArrayList<>();
	    	try (BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
	    		String inputLine;
	    		while ((inputLine = br.readLine()) != null) {
	    			values.add(inputLine);
	    		}
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	    	
	    	Table table = null;
	    	try {
				table = Common.server.getTable(tableName);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	
	    	ArrayList<Attribute> attributes = new ArrayList<>(values.size());
			for (int i = 0; i < values.size(); i++) {
				try {
					attributes.add(table.constructField(i, values.get(i)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				Common.server.addNewRow(tableName, attributes);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("put " + request.getRequestURL().toString());
		String[] urlParameters = parseUrl(request.getRequestURL().toString());
		
		if (urlParameters.length != 2 || !urlParameters[0].equals("db"))
			return;
		
		// editCell
		String tableName = urlParameters[1];
    	int rowId = -1;
    	int columnId = -1;
    	String value = null;
    	try (BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
    		rowId = Integer.parseInt(br.readLine());
    		columnId = Integer.parseInt(br.readLine());
    		value = br.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    	
		try {
			Common.server.editCell(tableName, rowId, columnId, value);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
