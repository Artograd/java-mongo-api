package me.artograd.javamongoapi.model.system;

public class UserTokenClaims {

	private String username;
	private boolean isOfficer;
	private boolean isArtist;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isOfficer() {
		return isOfficer;
	}
	public void setOfficer(boolean isOfficer) {
		this.isOfficer = isOfficer;
	}
	public boolean isArtist() {
		return isArtist;
	}
	public void setArtist(boolean isArtist) {
		this.isArtist = isArtist;
	}
}
