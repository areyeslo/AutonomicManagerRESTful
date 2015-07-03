package RESTful.clientLibrary.policy.model;

public class DeleteObject {
	private String id = "";
	
	public DeleteObject() {
	}
	
	public DeleteObject(String ID) {
		this.id = ID;
	}
	
	public String getID() {
		return this.id;
	}
	
}
