/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nature.platform.core.entity.base;

import org.nature.platform.core.entity.SuperObject;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author hutianlong
 */
@Entity
@Table(name = "USER_LICENSE")
@NamedQueries({ @NamedQuery(name = "UserLicense.findAll", query = "SELECT u FROM UserLicense u"),
		@NamedQuery(name = "UserLicense.findById", query = "SELECT u FROM UserLicense u WHERE u.id = :id"),
		@NamedQuery(name = "UserLicense.findByName", query = "SELECT u FROM UserLicense u WHERE u.name = :name"),
		@NamedQuery(name = "UserLicense.findByDescription", query = "SELECT u FROM UserLicense u WHERE u.description = :description"),
		@NamedQuery(name = "UserLicense.findByOrganizationId", query = "SELECT u FROM UserLicense u WHERE u.organizationId = :organizationId") })
public class UserLicense extends SuperObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Size(max = 255)
	@Column(name = "NAME")
	private String name;
	
	@Size(max = 2550)
	@Column(name = "DESCRIPTION")
	private String description;
	
	public UserLicense() {
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

	@Override
	public String toString() {
		return "org.colt.platform.core.entity.base.UserLicense[ id=" + id + " ]";
	}

}
