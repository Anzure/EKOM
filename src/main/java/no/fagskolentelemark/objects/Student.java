package no.fagskolentelemark.objects;

public class Student {

	private String firstName;
	private String lastName;
	private String username;
	private int phone;
	private String email;
	private String password;

	public Student(String firstName, String lastName, String username, int phone, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.phone = phone;
		this.email = email;
		this.password = password;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getUsername() {
		return this.username;
	}

	public int getPhone() {
		return this.phone;
	}

	public String getEmail() {
		return this.email;
	}

	public String getPassword() {
		return this.password;
	}
}