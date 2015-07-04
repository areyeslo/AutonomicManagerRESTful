package RESTful.clientLibrary.MAPEK;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import RESTful.clientLibrary.policy.model.Book;
import RESTful.clientLibrary.policy.model.NumberBooks;
import RESTful.clientLibrary.policy.model.Policy;

/**
 * Servlet implementation class Analyzer
 */
public class Analyzer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Analyzer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		PrintWriter printWriter = response.getWriter();
		int overLimit=0;
		
		String numberBooks = (String) request.getAttribute("sizeList");
		String year= (String)request.getAttribute("year");
		//printWriter.print("The number of books by year is: " + numberBooks);
		//printWriter.print("The year of books is: " + year);
		
		Client client= Client.create();
		WebResource webResource= client.resource("http://localhost:8080/clientLibrary/webapi/policy/year/" + year);
		                             
		ClientResponse rs=webResource.accept(
		           MediaType.APPLICATION_JSON_TYPE,
		           MediaType.APPLICATION_XML_TYPE).
		           get(ClientResponse.class);
		
		String jsonPolicy=rs.getEntity(String.class);		
		Gson gson = new Gson();
		Policy policy = gson.fromJson(jsonPolicy, Policy.class);
		
		printWriter.println("<li>"+"ID: "+policy.getId()+"<br>"+"Max Number of Books: "+policy.getMax_books()+"<br>"+"Year of Book: "+policy.getYear_book()+"<br>"+"Activated: "+policy.getActivate()+"<br></li><br>");
		//Go to DataBase Policy and query the year and the number of books
		
		int maxBooks=Integer.parseInt(numberBooks);				
		int maxBooksPolicy= policy.getMax_books();
		
		if (maxBooks<maxBooksPolicy){
			overLimit=0;
			printWriter.println("There are space for one more book. The overLimit value is: "+ overLimit );
		}else{
			overLimit=1;
			printWriter.println("There is no more space for books. Delete one book for the same year.");
			printWriter.println("The overLimit value is: "+overLimit);
		}
		/*Sending the value overLimit to planner
		 * overLimit=0 , we can insert the book
		 * overLimit=1 , there is no more space for the book
		 */
		/*
		request.setAttribute("overLimit",overLimit);
		RequestDispatcher rd = request.getRequestDispatcher("/Planer");
		rd.forward(request,response);*/
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
