package seleniumUAT;

public class errorReason {
	public String err = new String();
	
	public void clean() {
		err = null;
	}
	
	public void add(String str) {
		if (err != null) {
			err = err + "\n" + str;
		} else {
			err = str;
		}
	}
}
