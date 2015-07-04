package RESTful.clientLibrary.policy.resources;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import RESTful.clientLibrary.policy.model.*;

public class policyDB {
	
	public policyDB() {
		Connection c = null;
		c = accessDB();
		if (c != null) {
			Statement stmt = null;
			try {
				DatabaseMetaData md = c.getMetaData();
				ResultSet rs = md.getTables(null, null, "%", null);
				while (rs.next()) {
					if (rs.getString(3).equals("POLICIES")) {
						c.close();	 
						return;
					}
				}

				// Execute a query
				stmt = c.createStatement();
				String sql = "CREATE TABLE POLICIES " +
						"(ID INTEGER PRIMARY KEY   AUTOINCREMENT   NOT NULL," +
						" MAX_BOOKS   INT  NOT NULL UNIQUE," +
						" YEAR_BOOK   INT  NOT NULL,"+
						" ACTIVATE    INT  NOT NULL);"; 
				stmt.executeUpdate(sql);
				stmt.close();
				c.close();
			} catch (Exception e) {
				// Handle errors for Class.forName and handle errors for JDBC 
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );	      
				System.exit(0);
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
			c = DriverManager.getConnection("jdbc:sqlite:policy.db");
			System.out.println("Access Granted.");	    
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
		System.out.println("delete id :"+id);
		c = accessDB();
		if (c != null) {
			try {
				Statement stmt = null;
				// Execute a query
				stmt = c.createStatement();
				String sql = "DELETE FROM POLICIES WHERE ID='"+ id +"';";
				System.out.println(sql);
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

	
}
