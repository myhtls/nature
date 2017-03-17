package org.nature.credentials;

import org.picketlink.idm.credential.AbstractBaseCredentials;
import org.picketlink.idm.credential.Password;

public class UserBaseCredentials extends AbstractBaseCredentials {
	private String username;
	private Password password;

	public UserBaseCredentials() {
	}

	public UserBaseCredentials(String username, Password password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Password getPassword() {
		return password;
	}

	public void setPassword(Password password) {
		this.password = password;
	}

	@Override
	public void invalidate() {
		setStatus(Status.INVALID);
		password.clear();
	}
}
