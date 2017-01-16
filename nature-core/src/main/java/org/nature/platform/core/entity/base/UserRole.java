/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nature.platform.core.entity.base;

import org.nature.platform.core.entity.SuperObject;
import org.hibernate.annotations.BatchSize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "USER_ROLE")
@NamedQueries({
    @NamedQuery(name = "UserRole.findAll", query = "SELECT u FROM UserRole u"),
    @NamedQuery(name = "UserRole.findById", query = "SELECT u FROM UserRole u WHERE u.id = :id"),
    @NamedQuery(name = "UserRole.findByName", query = "SELECT u FROM UserRole u WHERE u.name = :name"),
    @NamedQuery(name = "UserRole.findByOrganizationId", query = "SELECT u FROM UserRole u WHERE u.organizationId = :organizationId")})
public class UserRole extends SuperObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 255)
    @Column(name = "NAME")
    private String name;
    
    @OneToMany(mappedBy = "parentRoleId", fetch = FetchType.LAZY)
    @BatchSize(size=200)
    private List<UserRole> userRoles = new ArrayList<>();
    
    @JoinColumn(name = "PARENT_ROLE_ID", referencedColumnName = "Id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserRole parentRoleId;
    
    @OneToMany(mappedBy = "userRoleId", fetch = FetchType.LAZY)
    @BatchSize(size=200)
    private List<User> users = new ArrayList<>();


    public UserRole() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public UserRole getParentRoleId() {
        return parentRoleId;
    }

    public void setParentRoleId(UserRole parentRoleId) {
        this.parentRoleId = parentRoleId;
    }

	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
    
}
