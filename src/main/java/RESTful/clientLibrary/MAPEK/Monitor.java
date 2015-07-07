package RESTful.clientLibrary.MAPEK;

import java.io.IOException;

import RESTful.clientLibrary.policy.model.Book;
import RESTful.clientLibrary.policy.model.DataObject;
import RESTful.clientLibrary.policy.resources.KnowledgeDB;


import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;


/**
 * Servlet implementation class Monitor
 */
public class Monitor extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Monitor() {
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter printWriter = response.getWriter();
		
		KnowledgeDB db = new KnowledgeDB();
		printWriter.println("<h1>Monitor Module</h1>");
		
		printWriter.println("<html><body> Recovering all books from Library...<br>");
		Client client= Client.create();
		WebResource webResource= client.resource("http://localhost:8080/library/webapi/books");
		
		ClientResponse rs=webResource.accept(
				           MediaType.APPLICATION_JSON_TYPE,
				           MediaType.APPLICATION_XML_TYPE).
				           get(ClientResponse.class);
		
		/*Transform json to java object*/
		String jsonBooks=rs.getEntity(String.class);		
		Gson gson = new Gson();
		Book[] booksA = gson.fromJson(jsonBooks, Book[].class);
		List<Book> books = Arrays.asList(booksA);
		
		/*for(Book book : books) {
            System.out.println(book.getId()+" "+book.getName()+", "+book.getAuthor()+", "+book.getAuthor()+", "+ book.getYear());
        }*/
		
		//Delete books before inserting
		//boolean delete= db.deleteAllTmpBooks();
		
		//Insert books in knowledge database
		boolean resp=db.InsertBooks(books);
		
		//if (resp && delete){
		if(resp){
			//request.setAttribute("books",books);
			printWriter.println("Books were retrieve from Library and they are stored in Knowledge Base for analysis");
			printWriter.println("</body></html>"); 
			request.getRequestDispatcher("/Analyzer").forward(request,response);			
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
