package cs263w16;

import java.util.HashMap;
import java.util.Map;

public enum TodoDao {
	instance;
	
	private Map<String, Todo> contentProvider = new HashMap<>();
	
	private TodoDao() {
		
		Todo todo = new Todo("1", "Learn REST");
		todo.setDescription("Read https://www.cs.ucsb.edu/~cs263/gae/Assignment3.html");
		contentProvider.put("1", todo);
		
		todo = new Todo("2", "Do something");
		todo.setDescription("Read complete https://www.cs.ucsb.edu/~cs263/gae/tests.txt");
		contentProvider.put("2", todo);
	}
	
	public Map<String, Todo> getModel(){
		return contentProvider;
	}
} 