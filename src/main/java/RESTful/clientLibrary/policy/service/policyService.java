package RESTful.clientLibrary.policy.service;
import RESTful.clientLibrary.policy.resources.*;
import RESTful.clientLibrary.policy.model.*;


import java.util.List;

public class policyService {
	
	private static KnowledgeDB PolicyStore = new KnowledgeDB();
	
	public List<Policy> getAllPolicies(){
		List<Policy> allPolicies = PolicyStore.query();
		for(Policy policies : allPolicies) {
        	System.out.println(policies.getId()+" "+policies.getMax_books()+", "+policies.getYear_book()+", "+policies.getActivate());
        }
		return allPolicies;		
	}
	
	public Policy getPolicyByYear(int year){
		return PolicyStore.queryByYear(year);
	}
	
	
	public Policy addPolicy(Policy policy) {
		try {
			int ID = PolicyStore.put(policy.getMax_books(), policy.getYear_book(), policy.getActivate());
			policy.setId(ID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return policy;
	}
	
	public boolean deletePolicy(int id){
		System.out.println("deletePolicy id :"+id);
		return PolicyStore.delete(id);
	}

	


}



