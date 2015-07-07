package RESTful.clientLibrary.MAPEK;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import RESTful.clientLibrary.policy.model.Book;
import RESTful.clientLibrary.policy.resources.BackupDB;
import RESTful.clientLibrary.policy.resources.KnowledgeDB;


/**
 * Servlet implementation class Planner
 */
public class Planner extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Planner() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter printWriter = response.getWriter();
		
		KnowledgeDB db= new KnowledgeDB();
		BackupDB backup= new BackupDB();
		int deleteBook = (int) request.getAttribute("deleteBooks");
		int yearBook = (int) request.getAttribute("year");

		//Recover n books by specific year and backup
		List<Book> booksByYear= db.queryTmpBookByYear(yearBook);
		List<Book> booksBck = new ArrayList<>();
		
		for(Book book : booksByYear) {
			if (deleteBook!=0){
				deleteBook--;
				//System.out.println("I will backup the next book:");
	            System.out.println(book.getId()+" "+book.getName()+", "+book.getAuthor()+", "+book.getPublisher()+", "+ book.getYear());
	            int id= book.getId();
	            String name= book.getName();
	            String author= book.getAuthor();
	            String publisher = book.getPublisher();
	            int year= book.getYear();
	            //Delete book from knowledge base and library database:
	            Book b= new Book(id,name,author,year,publisher);                
				//add the records to be affected by the delete into the list
				booksBck.add(b);
			}
        }
		/*Actions to be taken by the Executer:
		 * 1.- Delete indicated books in the list from temporal table
		 * 2.- Insert indicated books in the backup table
		 * 3.- Delete indicated books in the library database. 
		 */
		//Send control to the executer to apply this Actions
		request.setAttribute("books",booksBck);		
		request.getRequestDispatcher("Executer").forward(request,response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
