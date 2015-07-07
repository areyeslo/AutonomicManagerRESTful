package RESTful.clientLibrary.MAPEK;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import RESTful.clientLibrary.policy.model.Book;
import RESTful.clientLibrary.policy.model.DataObject;
import RESTful.clientLibrary.policy.model.Policy;
import RESTful.clientLibrary.policy.resources.BackupDB;
import RESTful.clientLibrary.policy.resources.KnowledgeDB;

/**
 * Servlet implementation class Executer
 */
public class Executer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Executer() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*Actions to be taken by the Executer:
		 * 1.- Delete indicated books in the list from temporal table
		 * 2.- Insert indicated books in the backup table
		 * 3.- Delete indicated books in the library database. 
		 */
		response.setContentType("text/html");
		PrintWriter printWriter = response.getWriter();
		KnowledgeDB knDB = new KnowledgeDB();
		printWriter.println("<html><body>Executer  takes actions to recover space in Database: <br>");
		BackupDB bckDB = new BackupDB();
		List<Book> booksUpdate = (List<Book>) request.getAttribute("books");
		
		for(Book book : booksUpdate) {
			int id= book.getId();
            String name= book.getName();
            String author= book.getAuthor();
            String publisher = book.getPublisher();
            int year= book.getYear();
            try {
            	//Backup the book
            	String idB= String.valueOf(id);
            	bckDB.deleteBackupYear(year);
				bckDB.insertBook(id, name, author, publisher, year);
				//Delete the book in the library
				request.setAttribute("id", idB);
	        	doDelete(request, response);
	        	printWriter.println("<li>Backup the book "+name+" of the year "+ year+" written by "+ author +"<br></li><br>");
	        	printWriter.println("<li>Deleted the book "+name+" of the year "+ year+" written by "+ author +"<br></li><br>");	        	
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		printWriter.println("</body></html>");		
		System.out.println("Finish executer.");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter printWriter = response.getWriter();  
		response.setContentType("text/html");
				
		String id = (String) request.getAttribute("id");
		if (id == null) return;
		
		Client client= Client.create();
		WebResource webResource= client.resource("http://localhost:8080/library/webapi/books/"+id);

		ClientResponse rs = webResource.accept("application/json")
				.type("application/json").delete(ClientResponse.class);
		
		// check response status code
		if (rs.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ rs.getStatus());
		}
		
		// display response
		String output = rs.getEntity(String.class);
		
		printWriter.println("<script type=\"text/javascript\">");  
		printWriter.println("alert('Books is deleted!');");  
		printWriter.println("</script>");
		
	}

}
