package RESTful.clientLibrary.MAPEK;

import java.io.IOException;

import RESTful.clientLibrary.policy.model.Book;
import RESTful.clientLibrary.policy.model.DataObject;

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
@Path("/monitor")
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
		
		String year = request.getParameter("year") ;
		if (year == null)
			year = "2010";
		
		Client client= Client.create();
		WebResource webResource= client.resource("http://localhost:8080/library/webapi/books/year/" + year);
		
		printWriter.print("I entered to doGet");
		
		ClientResponse rs=webResource.accept(
				           MediaType.APPLICATION_JSON_TYPE,
				           MediaType.APPLICATION_XML_TYPE).
				           get(ClientResponse.class);
		
		/*Transform json to java object*/
		String jsonBooks=rs.getEntity(String.class);		
		Gson gson = new Gson();
		Book[] booksA = gson.fromJson(jsonBooks, Book[].class);
		List<Book> books = Arrays.asList(booksA);
		
		for(Book book : books) {
            System.out.println(book.getId()+" "+book.getName()+", "+book.getAuthor()+", "+book.getAuthor()+", "+ book.getYear());
        }
		
		Integer size = books.size();
		String sizeList=size.toString();
		
		
		printWriter.print("The size list is: "+sizeList);
		
		request.setAttribute("sizeList",sizeList);
		request.setAttribute("year",year);
		request.getRequestDispatcher("/Analyzer").forward(request,response);
		
		/*Display book list in the servlet*/
		printWriter.println("<h1>List of books in the Library</h1>");
        
        if (books.isEmpty()){
        	printWriter.println("<html><body>Sorry, we did not have any book in the library"+"<br>");
        }else{
        	printWriter.println("<html><body>The complete list of books: <br>");
            printWriter.println("<ul>");
            for(Book book : books) {
                printWriter.println("<li>"+"ID: "+book.getId()+"<br>"+"Name: "+book.getName()+"<br>"+"Author: "+book.getAuthor()+"<br>"+"Publisher: "+book.getPublisher()+"<br>"+ "Year: "+book.getYear()+"<br></li><br>");
            }
        }
        printWriter.println("</body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
