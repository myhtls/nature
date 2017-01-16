package org.nature.platform.core.entity.base;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.nature.platform.core.entity.SuperObject;

/**
 *
 * @author hutianlong
 */
@Entity
@Table(name = "ORGANIZATION")
@NamedQueries({ @NamedQuery(name = "Organization.findAll", query = "SELECT o FROM Organization o"),
		@NamedQuery(name = "Organization.findById", query = "SELECT o FROM Organization o WHERE o.id = :id"),
		@NamedQuery(name = "Organization.findByName", query = "SELECT o FROM Organization o WHERE o.name = :name"),
		@NamedQuery(name = "Organization.findByCompanyEmail", query = "SELECT o FROM Organization o WHERE o.companyEmail = :companyEmail") })

public class Organization extends SuperObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Size(max = 255)
	@Column(name = "NAME")
	private String name;

	@Size(max = 255)
	@Column(name = "COMPANY_EMAIL")
	private String companyEmail;

	public Organization() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	@Override
	public String toString() {
		return "org.colt.platform.core.entity.base.Organization[ id=" + id + " ]";
	}

}
