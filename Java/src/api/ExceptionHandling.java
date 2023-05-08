package api;

public class ExceptionHandling {
	
	
	

}

class InvalidInputException extends Exception{

	private static final long serialVersionUID = 1L;
	
	InvalidInputException(){
		System.out.println("Enter Valid input");
	}
	
}
