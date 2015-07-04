package RESTful.clientLibrary.policy.resources;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		response.setContentType("text/html");
		PrintWriter printWriter = response.getWriter();
		
		Client client= Client.create();
		WebResource webResource= client.resource("http://localhost:8080/clientLibrary/webapi/policy");
		
		//create an object of RequestDispatcher 
		RequestDispatcher rd = request.getRequestDispatcher("GetPolicy"); 
		
		// send the client data available with req of delete to req of getPolicy with include() 
		rd.include(request, response);
		
		List<Policy> policies = (List<Policy>) request.getAttribute("policies");
		
		/*printWriter.print("List of policies in Delete: ");
		
		for(Policy policy : policies) {
             printWriter.println("<li>"+"ID: "+policy.getId()+"<br>"+"Max Number of Books: "+policy.getMax_books()+"<br>"+"Year of Book: "+policy.getYear_book()+"<br>"+"Activated: "+policy.getActivate()+"<br></li><br>");
         }*/
		
		//Show to the user the possible options to delete using radio button
		request.setAttribute("policies", policies);

        RequestDispatcher rd2 = getServletConfig().getServletContext().getRequestDispatcher("/showRecordsToDelete.jsp");
        rd2.include(request,response);
                
		//Receive the answer
		//printWriter.print("I am comming back from showRecordsToDelete.jsp");
		
		String policyID = request.getParameter("id");
				
		//printWriter.print("I will delete id: " + policyID);
		
		//Delete the choose policy
		doDelete(request, response);
		//printWriter.print("Delete a policy"+"<br>"+"<br>");
		printWriter.print("<a href=\"index.jsp\">Back</a>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.print("Delete post");
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String policyID = request.getParameter("id");
		if (policyID == null) return;
		
		Client client= Client.create();
		WebResource webResource= client.resource("http://localhost:8080/clientLibrary/webapi/policy/"+policyID);

		ClientResponse rs = webResource.accept("application/json")
				.type("application/json").delete(ClientResponse.class);
		
		// display response
		String output = rs.getEntity(String.class);
		System.out.println("Output from Server .... ");
		System.out.println(output + "\n");
	}
}
