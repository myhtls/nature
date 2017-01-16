package org.nature.platform.persistence.home;

import java.io.Serializable;

/**
 * HOME接口
 * @param T 类型
 * @param ID 主键
 * @author hutianlong
 *
 */
public interface IEntityHome<T,ID extends Serializable> {
	
	public T getEntity();
	public void setEntity(T entity);
	
	public ID getId();
	public void setId(ID key);
	
	public void setUrl(String url);
	public String getUrl();
	
	public String insert();
	public String update();
	public String delete();
	
	public String cancel();
	
	
	
	/**
	 * 判断id是否有值
	 * @return
	 */
	public boolean isIdSet();
	
	

}
