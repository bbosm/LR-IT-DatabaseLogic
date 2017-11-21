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
import transfer.ServerMaster;

@Path("/db")
public class RestServlet {
	private static ServerMaster server = new ServerMaster();
	
    public RestServlet() {
        super();
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("")
    public String getDB() throws IOException {
    	System.out.println("GET db");
    	return server.getDB().toString();
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{tableName}")
    public String getTable(@PathParam("tableName") String tableName) throws IOException {
    	System.out.println("GET " + tableName);
    	return server.getTable(tableName).toString();
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{tableName}/search")
    public String search(
    		@PathParam("tableName") String tableName, 
    		@QueryParam("p") final List<String> list) throws IOException {
    	System.out.println("GET " + tableName + "/search");
    	return server.search(tableName, String.join("\t", list)).toString();
    }
    
    
    
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("")
    public void createTable(String str) throws IOException {
    	System.out.println("POST createTable " + str);
    	server.createTable(str);
    }
    
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{tableName}")
    public void addNewRow(@PathParam("tableName") String tableName, String str) throws IOException {
    	System.out.println("POST addNewRow " + tableName);
    	server.addNewRow(tableName, str);
    }
    
    
    
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{tableName}")
    public void editCell(@PathParam("tableName") String tableName, String str) throws IOException {
    	System.out.println("PUT editCell " + tableName);
    	server.editCell(tableName, str);
    }
}
