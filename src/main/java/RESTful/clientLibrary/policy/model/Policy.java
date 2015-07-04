package RESTful.clientLibrary.policy.model;

import java.util.Date;

public class Policy {
	private int id;
	private int max_books;
	private int year_book;
	private int activate;
	
	public Policy(){
		
	}
	
	public Policy(int id, int max_books, int year_book,int activate){
		this.id=id;
		this.max_books=max_books;
		this.year_book=year_book;
		this.activate = activate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMax_books() {
		return max_books;
	}

	public void setMax_books(int max_books) {
		this.max_books = max_books;
	}

	public int getYear_book() {
		return year_book;
	}

	public void setYear_book(int year_book) {
		this.year_book = year_book;
	}

	
	public int getActivate() {
		return activate;
	}

	public void setActivate(int activate) {
		this.activate = activate;
	}
}
