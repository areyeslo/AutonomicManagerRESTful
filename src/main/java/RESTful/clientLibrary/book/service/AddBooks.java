package RESTful.clientLibrary.book.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import RESTful.clientLibrary.policy.model.DataObject;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Servlet implementation class AddBooks
 */
public class AddBooks extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddBooks() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// extract form data
		String bookName = request.getParameter("name");
		String authorName = request.getParameter("author");
		String yearBook = request.getParameter("year");
		String publisherBook = request.getParameter("publisher");

		PrintWriter printWriter = response.getWriter();

		DataObject obj = new DataObject(bookName, authorName, yearBook, publisherBook);
		Gson gson = new Gson();
		String jsonString = gson.toJson(obj);
		System.out.println(jsonString);

		Client client = Client.create();

		WebResource webResource = client.resource("http://localhost:8080/library/webapi/books");

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

		//				PrintWriter printWriter = response.getWriter();
		printWriter.println("<h1>The book is added currently</h1>");

		if (rs.getStatus() != 200){
			printWriter.println("<html><body>Sorry, Failed : HTTP error code :"+rs.getStatus()+"<br>");
		}else{
			obj = gson.fromJson(output, DataObject.class);

			printWriter.println("<html><body><br>");
			printWriter.println("ID: "+obj.getID()+"<br>"+"Name: "+obj.getName()+"<br>"+
					"Author: "+obj.getAuthor()+"<br>"+"Publisher: "+obj.getPublisher()+"<br>"+ 
					"Year: "+obj.getYear()+"<br><br>");
		}
		printWriter.println("</body></html>");
		printWriter.print("<a href=\"index.jsp\">Back</a>");
	}

}
