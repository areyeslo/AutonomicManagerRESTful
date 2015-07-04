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

import RESTful.clientLibrary.policy.model.DataObject;
import RESTful.clientLibrary.policy.model.Policy;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Servlet implementation class AddPolicy
 */
public class AddPolicy extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddPolicy() {
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

		/*Transform json to java object*/
		String jsonPolicy=rs.getEntity(String.class);		
		Gson gson = new Gson();
		Policy[] PolicyA = gson.fromJson(jsonPolicy, Policy[].class);
		List<Policy> policies = Arrays.asList(PolicyA);

		for(Policy policy : policies) {
			System.out.println(policy.getId()+" "+policy.getMax_books()+", "+policy.getYear_book()+", "+policy.getActivate()+", ");
		}

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
//		doGet(request, response);
		// extract form data
		int maxBooks = Integer.parseInt(request.getParameter("maxBooks"));
		int yearBook = Integer.parseInt(request.getParameter("yearBook"));
		String strActivate = request.getParameter("activate");
		System.out.print(strActivate);
		int activate = 1;
		if (strActivate == null)
			activate = 0;

		Policy policy = new Policy(0, maxBooks, yearBook, activate);
		Gson gson = new Gson();
		String jsonString = gson.toJson(policy);
		System.out.println(jsonString);

		Client client = Client.create();

		WebResource webResource = client.resource("http://localhost:8080/clientLibrary/webapi/policy");

		// POST method
		ClientResponse rs = webResource.accept("application/json")
				.type("application/json").post(ClientResponse.class, jsonString);

		// check response status code
		if (rs.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ rs.getStatus());
		}
		// display response
		String output = rs.getEntity(String.class);
		System.out.println("Output from Server .... ");
		System.out.println(output + "\n");

		PrintWriter printWriter = response.getWriter();
		printWriter.println("<h1>The new policy is added!</h1>");

		if (rs.getStatus() != 200){
			printWriter.println("<html><body>Sorry, Failed : HTTP error code :"+rs.getStatus()+"<br>");
		}else{
			policy = gson.fromJson(output, Policy.class);

			printWriter.println("<html><body><br>");
			printWriter.println("ID: "+policy.getId()+"<br>"+"Max Books: "+policy.getMax_books()+"<br>"+
					"Year: "+policy.getYear_book()+"<br>"+"Active: "+policy.getActivate()+"<br>"+ 
					"<br><br>");
		}
		printWriter.println("</body></html>");
		printWriter.print("<a href=\"index.jsp\">Back</a>");
	}

}
