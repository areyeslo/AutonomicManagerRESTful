package RESTful.clientLibrary.policy.model;

public class Book {
	private int id;
	private String name;
	private String author;
	private int year;
	private String publisher;
	
	public Book(){
		
	}
	
	public Book(int id, String name, String author, int year, String publisher){
		this.id=id;
		this.name=name;
		this.author=author;
		this.year=year;
		this.publisher=publisher;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
}
