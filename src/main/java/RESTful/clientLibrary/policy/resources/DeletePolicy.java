package RESTful.clientLibrary.policy.resources;

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

import RESTful.clientLibrary.policy.model.Policy;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Servlet implementation class DeletePolicy
 */

public class DeletePolicy extends HttpServlet {
	
	private static final long serialVersionUID = 1L;       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeletePolicy() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter printWriter = response.getWriter();
		
		Client client= Client.create();
		WebResource webResource= client.resource("http://localhost:8080/clientLibrary/webapi/policy");
		
		//create an object of RequestDispatcher 
		RequestDispatcher rd = request.getRequestDispatcher("GetPolicy"); 
		
		// send the client data available with req of delete to req of getPolicy with include() 
		rd.include(request, response);
		
		
		//GetPolicy policy = new GetPolicy();
		//List<Policy> policies= policy.service(request,response);
			

		//printWriter.print(policies);
		
		/*for(Policy policy : policies) {
            System.out.println(policy.getId()+" "+policy.getMax_books()+", "+policy.getYear_book()+", "+policy.getActivate()+", ");
        }*/
		
		printWriter.print("I am comming back in Delete to send a request to Delete method");
		
		/*ClientResponse rs=webResource.accept(
		           MediaType.APPLICATION_JSON_TYPE,
		           MediaType.APPLICATION_XML_TYPE).
		           delete(ClientResponse.class,input);
		
		printWriter.print("Delete a policy");*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
