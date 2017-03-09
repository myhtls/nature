/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nature.platform.core.entity.base;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.nature.platform.core.entity.SuperObject;

/**
 *
 * @author hutianlong
 */
@Entity
@Table(name = "USER")
@NamedQueries({
		@NamedQuery(name = "User.findUserByuserNameAndPassword", query = "select user from User user where user.userName=:userName and user.password=:password"),
		@NamedQuery(name ="user.findUserByuserName",query = "select NEW org.nature.platform.core.entity.base.User(user.firstName,user.lastName,user.userName,user.email) from User user where user.userName=:userName")
		
})

public class User extends SuperObject implements Serializable {

	public final static String LOGINSQL = "User.findUserByuserNameAndPassword";
	/**
	 * 通过用户名查找user实体类
	 */
	public final static String BYUSERNAME = "user.findUserByuserName";

	private static final long serialVersionUID = 1L;

	@Size(max = 255)
	@Column(name = "FIRSTNAME")
	private String firstName;

	@Size(max = 255)
	@Column(name = "LASTNAME")
	private String lastName;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "USERNAME")
	private String userName;

	// @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
	// message="电子邮件无效")//if the field contains email address consider using
	// this annotation to enforce field validation
	@Size(max = 255)
	@Column(name = "EMAIL")
	private String email;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "PASSWORD")
	private String password;

	@Size(max = 255)
	@Column(name = "WORKPHONE")
	private String workPhone;

	@Size(max = 255)
	@Column(name = "HOMEPHONE")
	private String homePhone;

	@Size(max = 255)
	@Column(name = "COMPANY")
	private String company;

	@Size(max = 255)
	@Column(name = "TITLE")
	private String title;

	
	@Column(name = "ACTIVE")
	private boolean active;

	@Size(max = 255)
	@Column(name = "EMPLOYEE_ID")
	private String employeeId;

	@Basic(fetch=FetchType.LAZY)
	@JoinColumn(name = "USER_PROFILE_ID", referencedColumnName = "Id")
	@ManyToOne(fetch = FetchType.LAZY)
	private UserProfile userProfileId;

	@JoinColumn(name = "USER_ROLE_ID", referencedColumnName = "Id")
	@ManyToOne(fetch = FetchType.LAZY)
	private UserRole userRoleId;

	public User() {
	}
	
	

	public User(String firstName, String lastName, String userName, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.email = email;
	}
	
	



	public User(String userName,String lastName,UserProfile userProfileId) {
		super();
		this.lastName = lastName;
		this.userName = userName;
		this.userProfileId = userProfileId;
	}



	

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getEmployeeID() {
		return employeeId;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeId = employeeID;
	}

	public UserProfile getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(UserProfile userProfileId) {
		this.userProfileId = userProfileId;
	}

	public UserRole getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(UserRole userRoleId) {
		this.userRoleId = userRoleId;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof User)) {
			return false;
		}
		User other = (User) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "org.colt.platform.core.entity.base.User[ id=" + id + " ]";
	}

}
