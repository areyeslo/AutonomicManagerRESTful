package RESTful.clientLibrary.policy.resources;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import RESTful.clientLibrary.policy.model.Book;
import RESTful.clientLibrary.policy.model.Policy;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Servlet implementation class GetPolicy
 */
public class GetPolicy extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetPolicy() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter printWriter = response.getWriter();
		
		Client client= Client.create();
		WebResource webResource= client.resource("http://localhost:8080/clientLibrary/webapi/policy");
		
		printWriter.println("<u>Searching for current policies...</u><br>");
		
		ClientResponse rs=webResource.accept(
		           MediaType.APPLICATION_JSON_TYPE,
		           MediaType.APPLICATION_XML_TYPE).
		           get(ClientResponse.class);
		
		//ClientResponse rs = webResource.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class,input);
		
		/*Transform json to java object*/
		String jsonPolicy=rs.getEntity(String.class);		
		Gson gson = new Gson();
		Policy[] PolicyA = gson.fromJson(jsonPolicy, Policy[].class);
		List<Policy> policies = Arrays.asList(PolicyA);
		
		for(Policy policy : policies) {
            System.out.println(policy.getId()+" "+policy.getMax_books()+", "+policy.getYear_book()+", "+policy.getActivate()+", ");
        }
		
		//Send List to the servlet that is calling
		request.setAttribute("policies", policies);
				
		/*Display book list in the servlet*/
		printWriter.println("<h1>List of Policies</h1>");
        
        if (policies.isEmpty()){
        	printWriter.println("<html><body>Sorry, we did not have any policy"+"<br>");
        }else{
        	printWriter.println("<html><body>The complete list of policies: <br>");
            printWriter.println("<ul>");
            for(Policy policy : policies) {
                printWriter.println("<li>"+"ID: "+policy.getId()+"<br>"+"Max Number of Books: "+policy.getMax_books()+"<br>"+"Year of Book: "+policy.getYear_book()+"<br>"+"Activated: "+policy.getActivate()+"<br></li><br>");
            }
        }
        printWriter.println("</body></html>");       
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
