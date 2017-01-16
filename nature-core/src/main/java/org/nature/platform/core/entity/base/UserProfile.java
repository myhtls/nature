/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nature.platform.core.entity.base;

import org.nature.platform.core.entity.SuperObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author hutianlong
 */
@Entity
@Table(name = "USER_PROFILE")
@NamedQueries({ @NamedQuery(name = "UserProfile.findAll", query = "SELECT u FROM UserProfile u"),
		@NamedQuery(name = "UserProfile.findById", query = "SELECT u FROM UserProfile u WHERE u.id = :id"),
		@NamedQuery(name = "UserProfile.findByName", query = "SELECT u FROM UserProfile u WHERE u.name = :name"),
		@NamedQuery(name = "UserProfile.findByDescription", query = "SELECT u FROM UserProfile u WHERE u.description = :description"),
		@NamedQuery(name = "UserProfile.findByUserLicenseId", query = "SELECT u FROM UserProfile u WHERE u.userLicenseId = :userLicenseId"),
		@NamedQuery(name = "UserProfile.findByOrganizationId", query = "SELECT u FROM UserProfile u WHERE u.organizationId = :organizationId") })

public class UserProfile extends SuperObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Size(max = 255)
	@Column(name = "Name")
	private String name;
	@Size(max = 2550)
	@Column(name = "Description")
	private String description;

	@Size(max = 255)
	@Column(name = "UserLicenseId")
	private String userLicenseId;

	@OneToMany(mappedBy = "userProfileId", fetch = FetchType.LAZY)
	private List<User> users = new ArrayList<>();

	public UserProfile() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserLicenseId() {
		return userLicenseId;
	}

	public void setUserLicenseId(String userLicenseId) {
		this.userLicenseId = userLicenseId;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
