package org.nature.platform.core.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.nature.platform.core.entity.base.User;
import org.hibernate.annotations.GenericGenerator;


@MappedSuperclass
public abstract class SuperObject implements Serializable, Cloneable {

	private static final long serialVersionUID = 8915767964901493670L;

	@Id
	@Column(name = "Id")
	@GeneratedValue(generator = "sequence_core_id" )
	@GenericGenerator( name = "sequence_core_id", strategy = "org.nature.platform.persistence.id.PKGenerator" )
	protected String id;

	@Version
	@Column(name = "version")
	protected Long version;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATEDBY")
	protected User createdBy;

	@Column(name = "CREATEDDATE")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	protected Date createdDate;
	
	
	@JoinColumn(name = "LASTMODIFIEDBY")
	@ManyToOne(fetch = FetchType.LAZY)
	protected User lastModifiedBy;

	@Column(name = "LASTMODIFIEDDATE")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	protected Date lastModifiedDate;

	@JoinColumn(name = "OWNERID")
	@ManyToOne(fetch = FetchType.LAZY)
	protected User ownerId;

	@Column(name = "ISDELETE")
	protected boolean isDelete;
	
	@Column(name = "ORGANIZATIONID")
	private String organizationId;
	

	public SuperObject() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate( Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate( Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public User getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(User ownerId) {
		this.ownerId = ownerId;
	}

	public boolean setIsDelete() {
		return isDelete;
	}

	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
	
	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + Objects.hashCode(this.id);
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
		final SuperObject other = (SuperObject) obj;
		return Objects.equals(this.id, other.id);
	}
	
	
	@PreUpdate
	@PrePersist
	private void prePersist() {
		
		Date now = new Date();
		this.setDelete(false);
		
		if(this.createdDate == null) this.setCreatedBy( null );
		if(this.createdDate == null) this.setCreatedDate( now );
		
		this.setLastModifiedBy(null);
		this.setLastModifiedDate( now );
	}
	
	@PostLoad
	private void postLoad(){
		
		
	}
}