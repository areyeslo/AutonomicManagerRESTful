package RESTful.clientLibrary.policy.resources;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import RESTful.clientLibrary.policy.model.Book;
import RESTful.clientLibrary.policy.model.Policy;


public class KnowledgeDB {
	public KnowledgeDB() {
		Connection c = null;
		c = accessDB();
		if (c != null) {
			Statement stmt = null;
			try {
				DatabaseMetaData md = c.getMetaData();
				ResultSet rs = md.getTables(null, null, "%", null);
				
				while (rs.next()) {
					if ((rs.getString(3).equals("POLICIES"))|| (rs.getString(3).equals("BOOK_TMP"))){
						c.close();	 
						return;
					}
				}
				
				stmt = c.createStatement();
				String sql_tmp = "CREATE TABLE BOOKS_TMP " +
						"(ID 			  INTEGER  NOT NULL," +
						" NAME            CHAR(50) NOT NULL," +
						" AUTHOR          CHAR(50) NOT NULL,"+
						" YEAR            INTEGER  NOT NULL,"+
						" PUBLISHER       CHAR(50) NOT NULL)"; 
				stmt.executeUpdate(sql_tmp);

				// Execute a query
				String sql = "CREATE TABLE POLICIES " +
						"(ID INTEGER PRIMARY KEY   AUTOINCREMENT   NOT NULL," +
						" MAX_BOOKS   INT  NOT NULL," +
						" YEAR_BOOK   INT  NOT NULL UNIQUE,"+
						" ACTIVATE    INT  NOT NULL);"; 
				stmt.executeUpdate(sql);
				
				stmt.close();
				c.close();
			} catch (Exception e) {
				// Handle errors for Class.forName and handle errors for JDBC 
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );	      
				//System.exit(0);
			}
		}
	}
	
	/** 
	 * @return  
	 */
	public Connection accessDB(){
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			//c = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Arturo\\Documents\\SelfAdaptiveSystems\\workspace\\AutonomicManagerRESTful\\knowledgeDB.db");
			c = DriverManager.getConnection("jdbc:sqlite:knowledgeDB.db");
			//System.out.println("Access Granted."); 
			
		} catch (Exception e) {
			// Handle errors for Class.forName and handle errors for JDBC
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );	      
			System.exit(0);
		}

		return c;
	}
	
	public List<Policy> query(){
		List<Policy> results = new ArrayList<>();
		Connection c = null; 
		c = accessDB();
		if (c != null){
			try{
				ResultSet rs=null;				 
				Statement stmt = c.createStatement();
				//rs = stmt.executeQuery( "SELECT id,max_books,year_book,date,activate FROM POLICIES order by id, date asc;" );								
				rs = stmt.executeQuery( "SELECT id,max_books,year_book,activate FROM POLICIES order by id,year_book asc;" );
				while ( rs.next() ) {
					//Get record from cursor
					int id = rs.getInt("id");
					int  max_books = rs.getInt("max_books");
					int  year_book  = rs.getInt("year_book");
					int activate = rs.getInt("activate");
					Policy p= new Policy(id,max_books,year_book,activate); 
					//add the record into the list
					results.add(p);
				}

				rs.close();
				stmt.close();
				c.close();
			}catch ( Exception e ) {
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}  
		return results;

	}
	
	public Policy queryByYear(int year){
		Policy policy= new Policy();
		Connection c = null; 
		c = accessDB();
		if (c != null){
			try{
				ResultSet rs=null;				 
				Statement stmt = c.createStatement();
				rs = stmt.executeQuery( "SELECT id,max_books,year_book,activate FROM POLICIES WHERE year_book="+year+";" );
				while ( rs.next() ) {
					//Get record from cursor
					int id = rs.getInt("id");
					policy.setId(id);
					int  max_books = rs.getInt("max_books");
					policy.setMax_books(max_books);
					int  year_book  = rs.getInt("year_book");
					policy.setYear_book(year_book);
					int activate = rs.getInt("activate");
					policy.setActivate(activate);
				}
				rs.close();
				stmt.close();
				c.close();
			}catch ( Exception e ) {
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}  
		return policy;

	}
	
	/** Insert a new policy in the database
	 * @param args max_books
	 * @param args year_book
	 * @param args date
	 * @param args activate
	 * @return  
	 */
	public int put(int max_books, int year_book, int activate) throws Exception {
		Connection c = null;
		Statement stmt = null;
		int lastID = 0;
		int insert = 0;
		c = accessDB();
		
		if (c != null) {
			try {
				c.setAutoCommit(false);
				
				// Execute a query
				stmt = c.createStatement();
								
				String sql = "INSERT OR REPLACE INTO POLICIES (MAX_BOOKS,YEAR_BOOK,ACTIVATE) VALUES ( '"+ max_books +"', '" + year_book + "', '" + activate + "' );";
				insert = stmt.executeUpdate(sql);
				
				ResultSet rs = null;
				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					lastID = rs.getInt(1);
                } else {
                    System.out.println("can't find most recent insert we just entered!");
                }
			} catch ( Exception e ) {
				// Handle errors for Class.forName and handle errors for JDBC
				if (e.getMessage().contains("UNIQUE")) 
					return 0;
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				throw e;
			} finally {
				try {
					stmt.close();
					c.commit();
					c.close();
				} catch (SQLException e) {
					// Handle errors for JDBC
					e.printStackTrace();
				}
			}
		}
		if (insert > 0)
			System.out.println("Stored a new policy: Limit of maximum books: "+max_books+" for the year "+year_book+" is activated: "+activate+")");
		return lastID;
	}

	/** Delete a policy
	 * @param id
	 * @return true if everything is done otherwise returns false
	 */
	public boolean delete(int id) {
		Connection c = null;
		c = accessDB();
		if (c != null) {
			try {
				Statement stmt = null;
				// Execute a query
				stmt = c.createStatement();
				String sql = "DELETE FROM POLICIES WHERE ID='"+ id +"';";
				stmt.executeUpdate(sql);
				c.commit();

				stmt.close();
				c.close();
			} catch ( Exception e ) {
				// Handle errors for Class.forName
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				return false;
			}
		}
		System.out.println("Deleted "+id);
		return true;
		
	}
	
	/********************************************
	 * Section of Temporal Books
	 ********************************************/
		
	/** Query all books
	 * @param 
	 * @return all books  
	 */	
	public List<Book> queryTmpBooks(){
		List<Book> results = new ArrayList<>();
		Connection c = null; 
		c = accessDB();
		if (c != null){
			try{
				ResultSet rs=null;				 
				Statement stmt = c.createStatement();
				rs = stmt.executeQuery( "SELECT id,name,author,year,publisher FROM BOOKS_TMP order by year, id asc;" );								
				while ( rs.next() ) {
					//Get record from cursor
					int id = rs.getInt("id");
					String  name = rs.getString("name");
					String author  = rs.getString("author");
					int year = rs.getInt("year");
					String publisher = rs.getString("publisher");
					Book b= new Book(id,name,author,year,publisher);                
					//add the record into the list
					results.add(b);
				}

				rs.close();
				stmt.close();
				c.close();
			}catch ( Exception e ) {
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}  
		return results;

	}
	
	public List<Book> queryTmpBookByYear(int year) {
		// TODO Auto-generated method stub
		List<Book> bookYear = new ArrayList<>();
		Connection c = null; 
		c = accessDB();
		if (c != null){
			try{
				ResultSet rs = null;				 
				Statement stmt = c.createStatement();
				rs = stmt.executeQuery( "SELECT * FROM BOOKS_TMP WHERE YEAR='"+year+"'"+" order by year, id asc;" );								
				while ( rs.next() ) {
					//Get record from cursor
					int id = rs.getInt("id");
					String  name = rs.getString("name");
					String author  = rs.getString("author");
					String publisher = rs.getString("publisher");
					Book b= new Book(id,name,author,year,publisher);                
					//add the record into the list
					bookYear.add(b);
				}

				rs.close();
				stmt.close();
				c.close();
			}catch ( Exception e ) {
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
			
		}
		return bookYear;
	}
	
	public boolean deleteAllTmpBooks() {
		Connection c = null;
		c = accessDB();
		if (c != null) {
			try {
				c.setAutoCommit(false);
				Statement stmt = null;
				// Execute a query
				stmt = c.createStatement();
				String sql = "DELETE FROM BOOKS_TMP;";
				stmt.executeUpdate(sql);
				c.commit();

				stmt.close();
				c.close();
			} catch ( Exception e ) {
				// Handle errors for Class.forName
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				return false;
			}
		}
		System.out.println("All Books deleted in temporal table");
		return true;
	}
	
	public boolean deleteTmpBooksByYear(int year) {
		Connection c = null;
		c = accessDB();
		if (c != null) {
			try {
				c.setAutoCommit(false);
				Statement stmt = null;
				// Execute a query
				stmt = c.createStatement();
				String sql = "DELETE FROM BOOKS_TMP WHERE year="+year+ ";";
				stmt.executeUpdate(sql);
				c.commit();

				stmt.close();
				c.close();
			} catch ( Exception e ) {
				// Handle errors for Class.forName
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				return false;
			}
		}
		System.out.println("All Books deleted in temporal table");
		return true;
	}
	
	public boolean InsertBooks(List<Book> temporalBooks){
		Connection c = null; 
		c = accessDB();
		int id=0;
		String name=null;
		String author=null;
		String publisher=null;
		int year=0;
	    
		try{
			c.setAutoCommit(false);
			Statement stmt = c.createStatement();
			for(Book book : temporalBooks) {
                System.out.println("ID: "+book.getId()+"Name: "+book.getName()+"Author: "+book.getAuthor()+"Publisher: "+book.getPublisher()+"Year: "+book.getYear());
				// Execute a query
                id=book.getId();
                name=book.getName();
                author=book.getAuthor();
                publisher=book.getPublisher();
                year=book.getYear();				
				String sql = "INSERT OR REPLACE INTO BOOKS_TMP (ID,NAME,AUTHOR,YEAR,PUBLISHER) VALUES ( '"+ id +"', '" +name +"', '" + author + "', '" + year + "', '" + publisher + "' );";
				stmt.executeUpdate(sql);
				c.commit();
			}
			stmt.close();
			c.close();
        }
	    catch ( Exception e ) {
	    	// Handle errors for Class.forName
	    	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    	return false;
	    }
		
		return true;

	}
	
	
}
