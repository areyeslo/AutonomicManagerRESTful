package RESTful.clientLibrary.MAPEK;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

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
import RESTful.clientLibrary.policy.model.Policy;
import RESTful.clientLibrary.policy.resources.KnowledgeDB;

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
		response.setContentType("text/html");
		
		PrintWriter printWriter = response.getWriter();
		KnowledgeDB db = new KnowledgeDB();
		
		Client client= Client.create();
		WebResource webResource= client.resource("http://localhost:8080/clientLibrary/webapi/policy");
		                             
		ClientResponse rs=webResource.accept(
		           MediaType.APPLICATION_JSON_TYPE,
		           MediaType.APPLICATION_XML_TYPE).
		           get(ClientResponse.class);
		
		String jsonPolicy=rs.getEntity(String.class);		
		Gson gson = new Gson();
		Policy[] PolicyA = gson.fromJson(jsonPolicy, Policy[].class);
		List<Policy> policies = Arrays.asList(PolicyA);
		
		for(Policy policy : policies) {
			System.out.println(policy.getId()+" "+policy.getMax_books()+", "+policy.getYear_book()+", "+ policy.getActivate());
			//For each policy decide if you need to react to fix policy:
			int id= policy.getId();
			int maxBooks= policy.getMax_books();
			int year= policy.getYear_book();
			int activate=policy.getActivate();
			
			//Check possible symptom if the policy is activated
			if (activate!= 0){
				//Get the number of books by the policy year
				List<Book> booksTmp = db.queryTmpBookByYear(year);
				for(Book book : booksTmp) {
		            System.out.println(book.getId()+" "+book.getName()+", "+book.getAuthor()+", "+book.getAuthor()+", "+ book.getYear());
		        }
				int numBooks= booksTmp.size();
				if (numBooks > maxBooks){
					//Backup books and Delete
					System.out.println("Go to Planner");
					
					
					int deleteBooks = numBooks-maxBooks;
					
					System.out.println("Planner would delete "+ deleteBooks + " books for the year " + year + " to make space in DB." );
					
					request.setAttribute("deleteBooks", deleteBooks);
					request.setAttribute("year", year);
					request.setAttribute("booksTmp", booksTmp);
					
					//Delete Temporal Books by this Year
					db.deleteTmpBooksByYear(year);
					
					//create an object of RequestDispatcher 
					RequestDispatcher rd = request.getRequestDispatcher("Planner"); 
					
					// send the client data available
					rd.include(request, response);
										
					
					boolean done= (boolean) request.getAttribute("done");
					
					System.out.println("Analyzer has done.");

				}
				
				
			}
		
		}
		printWriter.println("Analyzer has finished to check all policies.");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
