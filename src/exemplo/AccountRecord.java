package exemplo;

public class AccountRecord {
	private int account;
	private String firstName;
	private String lastName;
	private double balance;
	
	public AccountRecord(){
		this(0,"","",0.0);
	}
	
	public AccountRecord(int acct, String first, String last, double bal){
		setAccount(acct);
		setFirstName(first);
		setLastName(last);
		setBalance(bal);
	}

	private void setFirstName(String first) {
		firstName = first;
	}

	private void setBalance(double bal) {
		balance = bal;
	}

	private void setLastName(String last) {
		lastName = last;
	}

	private void setAccount(int acct) {
		account=acct;
	}

	public int getAccount() {
		return account;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public double getBalance() {
		return balance;
	}
	
	
}
