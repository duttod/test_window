package karrus.client.users.users;

 class UserContent {
	
	String login;
	String password;
	String email;
	String credential;

	public UserContent(String login, String password, String email, String credential) {
		this.login = login;
		this.password = password;
		this.email = email;
		this.credential = credential;
	}
	
	public String getLogin() {
		return login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getCredential() {
		return credential;
	}
}
