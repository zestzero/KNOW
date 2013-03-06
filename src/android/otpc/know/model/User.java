package android.otpc.know.model;

public class User {
	String username, pwd, email, time;

	public User() {
	}

	public void setUser(String User) {
		this.username = User;
	}

	public String getUser() {
		return this.username;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPwd() {
		return this.pwd;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTime() {
		return this.time;
	}
}