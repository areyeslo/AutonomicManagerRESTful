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

import RESTful.clientLibrary.policy.model.DeleteObject;
import RESTful.clientLibrary.policy.model.Policy;

import com.google.gson.Gson;
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
		response.setContentType("text/html");
		PrintWriter printWriter = response.getWriter();
		
		Client client= Client.create();
		WebResource webResource= client.resource("http://localhost:8080/clientLibrary/webapi/policy");
		
		//create an object of RequestDispatcher 
		RequestDispatcher rd = request.getRequestDispatcher("GetPolicy"); 
		
		// send the client data available with req of delete to req of getPolicy with include() 
		rd.include(request, response);
		
		List<Policy> policies = (List<Policy>) request.getAttribute("policies");
		
		printWriter.print("List of policies in Delete: ");
		
		for(Policy policy : policies) {
             printWriter.println("<li>"+"ID: "+policy.getId()+"<br>"+"Max Number of Books: "+policy.getMax_books()+"<br>"+"Year of Book: "+policy.getYear_book()+"<br>"+"Activated: "+policy.getActivate()+"<br></li><br>");
         }
		
		//Show to the user the possible options to delete using radio button
		request.setAttribute("policies", policies);

        RequestDispatcher rd2 = getServletConfig().getServletContext().getRequestDispatcher("/showRecordsToDelete.jsp");
        rd2.include(request,response);
        
        
		//Receive the answer
		printWriter.print("I am comming back from showRecordsToDelete.jsp");
		
		String policyID = request.getParameter("id");
//		String input = "{\"id\":\"" + policyID +"\"}";
		DeleteObject delObj = new DeleteObject(policyID);
		Gson gson = new Gson();
		String jsonString = gson.toJson(delObj);
		System.out.println(jsonString);
		
		printWriter.print("I will delete id: " + policyID);
		
		//Delete the choose policy
		/*ClientResponse rs=webResource.accept(
		           MediaType.APPLICATION_JSON_TYPE,
		           MediaType.APPLICATION_XML_TYPE).
		           delete(ClientResponse.class,input);*/
		ClientResponse rs = webResource.accept("application/json")
				.type("application/json").delete(ClientResponse.class, jsonString);
		
		//Receive the answer and provide status to user
		printWriter.print("Delete a policy");
		
		/*response.setContentType("text/html");
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
		
		for(Policy policy : policies) {
            System.out.println(policy.getId()+" "+policy.getMax_books()+", "+policy.getYear_book()+", "+policy.getActivate()+", ");
        }
		
		printWriter.print("I am comming back in Delete to send a request to Delete method");
		
		ClientResponse rs=webResource.accept(
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
