package org.nature.platform.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.Version;

import org.nature.platform.utils.DateTool;
import org.nature.platform.utils.UUIDGenerator;


/**
 * 实体父类
 * @author hutianlong
 *
 */
@MappedSuperclass
public class SuperObject implements Serializable, Cloneable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7956270958014691404L;

	@Id
	@Column(name = "Id")
	protected String id;
	
	@Version
	@Column(name = "version")
	private Long version;
	
	@Column(name = "createddate")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Column(name = "updateDate")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date updateDate;
	
	
	@Column(name = "createUser",length=255)
	private String createUser;
	
	
	@Column(name = "updateUser",length=255)
	private String updateUser;
	
	@Column(name = "ISDELETE")
	private boolean isDelete;
	
	@Column(name = "enable",length=1)
	private int enable;
	
	public SuperObject() {
	}

	/**
	 * 主键,自动生成,规则为:NATURE0000000000开始，依次加1
	 * @return
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 乐关琐
	 * @return
	 */
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * 创建日期,年月日时分秒
	 * @return
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	
    /**
     * 更新日期,年月日时分秒
     * @return
     */
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * 逻辑删除
	 * @return
	 */
	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}



	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@PrePersist
	private void prePersist() {
		
		this.setId(UUIDGenerator.getUUID());
		this.setDelete(false);
		this.setEnable(1);
		setCreatedDate(DateTool.now() );
		
		
	}
	
	@PreUpdate
	private void preUpdate(){
		this.setUpdateDate(DateTool.now());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SuperObject other = (SuperObject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
