/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nature.platform.entity;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author hutianlong
 */
@Entity
@Table(name = "SYS_MENU")
@NamedQueries({
		@NamedQuery(name = "SysMenu.findSysMenuByRoot", query = "select sysMenu from SysMenu sysMenu where sysMenu.parentMenuId is null and sysMenu.isDelete = 1 order by sysMenu.sequence desc"),
		@NamedQuery(name = "SysMenu.findSysMenuByParentId", query = "select sysMenu from SysMenu sysMenu where sysMenu.parentMenuId.id=:parentMenuId and sysMenu.isDelete = 1 order by sysMenu.sequence desc"),
		@NamedQuery(name = "SysMenu.findSysMenuByName", query = "select sysMenu from SysMenu sysMenu where sysMenu.name=:name and sysMenu.parentMenuId.id=:parentMenuId and sysMenu.isDelete = 1"),
		@NamedQuery(name = "SysMenu.DELETE", query = "UPDATE  SysMenu sysMenu set sysMenu.isDelete=0 where sysMenu.id=:key"),
		@NamedQuery(name = "SysMenu.All", query = "select sysMenu from SysMenu sysMenu where sysMenu.isDelete=1  order by sysMenu.sequence desc")

})
public class SysMenu extends SuperObject implements Serializable, Cloneable {

	public final static String ROOT = "SysMenu.findSysMenuByRoot";
	public final static String CHILDREN = "SysMenu.findSysMenuByParentId";
	public final static String OLDNAME = "SysMenu.findSysMenuByName";
	public final static String ALL = "SysMenu.All";
	public final static String DELETESYSMENU = "SysMenu.DELETE";
	private static final long serialVersionUID = 1L;

	private static SysMenu PUSYSMENU = new SysMenu();

	/**
	 * 克隆创建一个bean
	 *
	 * @return
	 */
	public static SysMenu createPuSysMenu() {
		try {
			return (SysMenu) PUSYSMENU.clone();
		} catch (CloneNotSupportedException cnse) {
		}
		return null;
	}

	@Size(max = 255)
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "SEQUENCE")
	private Integer sequence;
	
	@Size(max = 2550)
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Size(max = 255)
	@Column(name = "ICON")
	private String icon;
	
	@Size(max = 255)
	@Column(name = "URL")
	private String url;
	
	@OrderBy(value = "SEQUENCE DESC")
	@OneToMany(mappedBy = "parentMenuId", fetch = FetchType.LAZY)
	private List<SysMenu> sysmenuCollection = new ArrayList<>();
	
	@JoinColumn(name = "PARENT_MENU_ID", referencedColumnName = "Id")
	@ManyToOne(fetch = FetchType.LAZY)
	private SysMenu parentMenuId;

	public SysMenu(String id) {
		super.setId(id);
	}

	public SysMenu() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<SysMenu> getSysMenuCollection() {
		return sysmenuCollection;
	}

	public void setSysMenuCollection(List<SysMenu> sysmenuCollection) {
		this.sysmenuCollection = sysmenuCollection;
	}

	public SysMenu getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(SysMenu parentMenuId) {
		this.parentMenuId = parentMenuId;
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
		if (!(object instanceof SysMenu)) {
			return false;
		}
		SysMenu other = (SysMenu) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return id;
	}

}
