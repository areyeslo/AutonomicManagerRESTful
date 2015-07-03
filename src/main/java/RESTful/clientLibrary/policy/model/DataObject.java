package RESTful.clientLibrary.policy.model;

public class DataObject {
	private String id = "";
	private String name = "";
	private String author = "";
	private String year = "";
	private String publisher = "";
	
	public DataObject() {
	}
	
	public DataObject(String n, String a, String y, String p ) {
		this.name = n;
		this.author = a;
		this.year = y;
		this.publisher = p;
	}
	
	public String getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public String getYear() {
		return this.year;
	}
	
	public String getPublisher() {
		return this.publisher;
	}
	
	@Override
	public String toString() {
		return "DataObject [name : " + this.name + ", author : " + this.author + ", year :"
				+ this.year + " publisher: "+ this.publisher +"]";
	}
}
