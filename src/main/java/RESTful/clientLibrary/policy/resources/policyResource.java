package RESTful.clientLibrary.policy.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import RESTful.clientLibrary.policy.model.Policy;
import RESTful.clientLibrary.policy.service.policyService;



/** This class will implement all the request on the resource: 
 * GET
 * PUT
 * DELETE
 * POST
 */
@Path("/policy")
public class policyResource {
	policyDB db = new policyDB();
	policyService PolicyService = new policyService();
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)	
    public List<Policy> getPolicies(){
		List<Policy> books = PolicyService.getAllPolicies();		
		return books;
	}

	
	@GET
	@Path("/year/{year}")
	@Produces(MediaType.APPLICATION_JSON)
	public Policy getPolicyByYear(@PathParam("year") int year){
		return PolicyService.getPolicyByYear(year);
		
	}
	
		
	
	@DELETE
	@Path("/{policyID}")
	@Produces(MediaType.APPLICATION_JSON)	
	public String removeBook(@PathParam("policyID") int ID) {
		boolean removed = PolicyService.deletePolicy(ID);
		String answer = "Removed successfully";
		if(removed = false){
			answer = "Not removed";
		}
		return answer;
	}
	

	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Policy addPolicy(Policy policy) {
		
		if (policy != null) 
			return PolicyService.addPolicy(policy);
		return null;
	}
}