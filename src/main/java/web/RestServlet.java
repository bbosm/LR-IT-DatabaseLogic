package web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import db.Column;
import db.ColumnFactory;
import db.Table;
import dbtype.Attribute;
import transfer.Server;

@Path("/db")
public class RestServlet {
	private static Server server = new Server();
	
    public RestServlet() {
        super();
        
        try {
			server.getDB();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("")
    public String getDB() throws IOException {
    	System.out.println("GET db");
    	StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        
        server.getDB().writeToPrintWriter(writer);
    	return out.toString();
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{tableName}")
    public String getTable(@PathParam("tableName") String tableName) throws IOException {
    	System.out.println("GET " + tableName);
    	StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        
        server.getTable(tableName).writeToPrintWriter(writer);
    	return out.toString();
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{tableName}/search")
    public String search(
    		@PathParam("tableName") String tableName, 
    		@QueryParam("p") final List<String> list) throws IOException {
    	System.out.println("GET " + tableName + "/search");
    	
    	ArrayList<String> fieldsSearch = new ArrayList<>(list);
    	
    	Table searchResult = null;
		searchResult = server.search(tableName, fieldsSearch);
    	
    	StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        
        searchResult.writeToPrintWriter(writer);
    	return out.toString();
    }
    
    
    
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("")
    public String createTable(String str) throws IOException {
		String[] inputLines = str.split("\\r?\\n");
		
    	String tableName = inputLines[0];
    	System.out.println("POST createTable " + tableName);
		ArrayList<Column> columns = new ArrayList<>();
		for (int i = 1; i < inputLines.length; i++) {
			try {
				columns.add(ColumnFactory.createColumn(inputLines[i]));
			} catch (NoSuchMethodException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		server.createTable(tableName, columns);
		
		return "";
    }
    
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{tableName}")
    public String addNewRow(@PathParam("tableName") String tableName, String str) throws IOException {
    	System.out.println("POST addNewRow " + tableName);
		String[] inputLines = str.split("\\r?\\n");
		
		Table table = server.getTable(tableName);
		ArrayList<Attribute> attributes = new ArrayList<>(inputLines.length);
		for (int i = 0; i < inputLines.length; i++) {
			try {
				attributes.add(table.constructField(i, inputLines[i]));
			} catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
		}
		
		server.addNewRow(tableName, attributes);
		
		return "";
    }
    
    
    
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{tableName}")
    public String editCell(@PathParam("tableName") String tableName, String str) throws IOException {
    	System.out.println("PUT editCell " + tableName);
		String[] inputLines = str.split("\\r?\\n");
		
		int rowId = Integer.parseInt(inputLines[0]);
		int columnId = Integer.parseInt(inputLines[1]);
		String value = inputLines[2];

		server.editCell(tableName, rowId, columnId, value);
		
		return "";
    }
}
