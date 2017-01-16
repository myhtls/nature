/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nature.platform.core.entity.base;

import org.nature.platform.core.entity.SuperObject;
import java.io.Serializable;
import java.util.Objects;
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
@Table(name = "CUSOME_LABEL")
@NamedQueries({ @NamedQuery(name = "CusomeLabel.findAll", query = "SELECT c FROM CusomeLabel c"),
		@NamedQuery(name = "CusomeLabel.findById", query = "SELECT c FROM CusomeLabel c WHERE c.id = :id"),
		@NamedQuery(name = "CusomeLabel.findByName", query = "SELECT c FROM CusomeLabel c WHERE c.name = :name"),
		@NamedQuery(name = "CusomeLabel.findByValue", query = "SELECT c FROM CusomeLabel c WHERE c.value = :value"),
		@NamedQuery(name = "CusomeLabel.findByCategory", query = "SELECT c FROM CusomeLabel c WHERE c.category = :category"),
		@NamedQuery(name = "CusomeLabel.findByDescription", query = "SELECT c FROM CusomeLabel c WHERE c.description = :description"),
		@NamedQuery(name = "CusomeLabel.findByOrganizationId", query = "SELECT c FROM CusomeLabel c WHERE c.organizationId = :organizationId") })
public class CusomeLabel extends SuperObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Size(max = 255)
	@Column(name = "NAME")
	private String name;
	
	@Size(max = 255)
	@Column(name = "VALUE")
	private String value;
	
	@Size(max = 255)
	@Column(name = "CATEGORY")
	private String category;
	
	@Size(max = 2550)
	@Column(name = "DESCRIPTION")
	private String description;
	
	public CusomeLabel() {
	}

	public CusomeLabel(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 17 * hash + Objects.hashCode(this.id);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CusomeLabel other = (CusomeLabel) obj;
		return Objects.equals(this.id, other.id);
	}

	@Override
	public String toString() {
		return "org.colt.platform.core.entity.base.CusomeLabel[ id=" + id + " ]";
	}

}
