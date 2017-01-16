package org.nature.platform.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

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
	@GeneratedValue(generator = "sequence_core_id" )
	@GenericGenerator( name = "sequence_core_id", strategy = "org.nature.platform.persistence.id.PKGenerator" )
	protected String id;
	
	@Version
	@Column(name = "version")
	protected Long version;
	
	@Column(name = "CREATEDDATE")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	protected Date createdDate;
	
	@Column(name = "LASTMODIFIEDDATE")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	protected Date updateDate;
	
	@Column(name = "ISDELETE")
	protected boolean isDelete;
	
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
	
	

}
