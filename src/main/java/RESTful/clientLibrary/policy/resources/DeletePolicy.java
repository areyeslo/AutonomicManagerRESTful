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
		
		if (!policies.isEmpty()){		
			//Show to the user the possible options to delete using radio button
			request.setAttribute("policies", policies);

        	RequestDispatcher rd2 = getServletConfig().getServletContext().getRequestDispatcher("/showRecordsToDelete.jsp");
        	rd2.include(request,response);
                
        	//Receive the answer
        	String policyID = request.getParameter("id");
				
        	//Delete the choose policy
        	doDelete(request, response);
		
		}
		printWriter.print("<a href=\"index.jsp\">Back</a>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.print("Delete post");
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter printWriter = response.getWriter();  
		response.setContentType("text/html");
				
		String policyID = request.getParameter("id");
		if (policyID == null) return;
		
		Client client= Client.create();
		WebResource webResource= client.resource("http://localhost:8080/clientLibrary/webapi/policy/"+policyID);

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
		printWriter.println("alert('Policy is deleted!');");  
		printWriter.println("</script>");
		
	}
}
