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

public class BackupDB {
	public BackupDB(){
		Connection c = null;
		c = accessDB();
		if (c != null) {
			Statement stmt = null;
			try {
				DatabaseMetaData md = c.getMetaData();
				ResultSet rs = md.getTables(null, null, "%", null);
				
				while (rs.next()) {
					if ((rs.getString(3).equals("BACKUP"))){
						c.close();	 
						return;
					}
				}
				
				stmt = c.createStatement();
				String sql_tmp = "CREATE TABLE BACKUP " +
						"(ID 			  INTEGER," +
						" NAME            CHAR(50)," +
						" AUTHOR          CHAR(50),"+
						" YEAR            INTEGER,"+
						" PUBLISHER       CHAR(50))"; 
				stmt.executeUpdate(sql_tmp);
							
				stmt.close();
				c.close();
			} catch (Exception e) {
			// 	Handle errors for Class.forName and handle errors for JDBC 
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );	      
			//	System.exit(0);
			}
		}
	}
	
	public Connection accessDB(){
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Arturo\\Documents\\SelfAdaptiveSystems\\workspace\\AutonomicManagerRESTful\\backupDB.db");
			System.out.println("Access Granted."); 
			
		} catch (Exception e) {
			// Handle errors for Class.forName and handle errors for JDBC
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );	      
			System.exit(0);
		}

		return c;
	}
	
	public List<Book> queryBackup(){
		List<Book> results = new ArrayList<>();
		Connection c = null; 
		c = accessDB();
		if (c != null){
			try{
				ResultSet rs=null;				 
				Statement stmt = c.createStatement();
				rs = stmt.executeQuery( "SELECT id,name,author,year,publisher FROM BACKUP order by year, id asc;" );								
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
	
	public List<Book> queryBackupByYear(int year) {
		// TODO Auto-generated method stub
		List<Book> bookYear = new ArrayList<>();
		Connection c = null; 
		c = accessDB();
		if (c != null){
			try{
				ResultSet rs = null;				 
				Statement stmt = c.createStatement();
				rs = stmt.executeQuery( "SELECT * FROM BACKUP WHERE YEAR='"+year+"'"+" order by year, id asc;" );								
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
	
	public boolean deleteBackup() {
		Connection c = null;
		c = accessDB();
		if (c != null) {
			try {
				c.setAutoCommit(false);
				Statement stmt = null;
				// Execute a query
				stmt = c.createStatement();
				String sql = "DELETE FROM BACKUP;";
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
		System.out.println("All Books deleted");
		return true;
	}
	
	public boolean InsertBooksBackup(List<Book> books){
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
			for(Book book : books) {
                System.out.println("ID: "+book.getId()+"Name: "+book.getName()+"Author: "+book.getAuthor()+"Publisher: "+book.getPublisher()+"Year: "+book.getYear());
				// Execute a query
                id=book.getId();
                name=book.getName();
                author=book.getAuthor();
                publisher=book.getPublisher();
                year=book.getYear();				
				String sql = "INSERT OR REPLACE INTO BACKUP (ID,NAME,AUTHOR,YEAR,PUBLISHER) VALUES ( '"+ id +"', '" +name +"', '" + author + "', '" + year + "', '" + publisher + "' );";
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
	
	public int insertBook(int id, String name, String author, String publisher, int year) throws Exception {
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
				String sql = "INSERT OR REPLACE INTO BACKUP (ID,NAME,AUTHOR,PUBLISHER,YEAR) VALUES ( '"+ id +"', '" +name +"', '" + author + "', '" + publisher + "', '" + year + "' );"; 
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
			System.out.println("Stored ("+name+","+author+")");
		return lastID;
	}
	
	

}
