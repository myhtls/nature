package org.nature.platform.persistence.home;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.inject.Inject;

import org.nature.platform.enums.StatusCode;
import org.nature.platform.persistence.dao.BaseDao;

/**
 * 默认实现部分实体管理器,用于专门编辑页面所使用. 该父类默认的完成了对话相应的启动和结束会话，以及相关的返回操作.
 * 同时支持保存，更新，删除，初始，销毁的方法. 但是继承者仍然需要实现几个方法:doLoadEntity(), doInsert(),doUpdate()
 * ,doDelete() 两个可选实现方法:doPreInitData(),doPreDestory()
 * 
 * @author hutianlong
 *
 */
public abstract class AbstractEntityHome<T, ID extends Serializable> implements IEntityHome<T, ID> {

	private T entity;
	private ID key;
	private Class<T> entityClass;
	private String url;
	private long timeout = 5000;

	private @Inject Conversation conversation;

	protected abstract BaseDao<T, Serializable> getBaseDao();

	/**
	 * 初始化bean时调用此方法
	 */
	@PostConstruct
	private void init() {
		beginConversation();
	}

	/**
	 * 销毁
	 */
	@PreDestroy
	private void preDestory() {
		doPreDestory();
	}

	/**
	 * 启动对话
	 */
	private void beginConversation() {
		if (conversation.isTransient()) {
			conversation.begin();
			doPreInitData();
		}
		conversation.setTimeout(getTimeout());
	}

	/**
	 * 结束对话
	 */
	private String endConversation() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
		return getUrl();
	}

	/**
	 * 要初始化执行的操作在里边执行
	 */
	protected void doPreInitData() {
	}

	/**
	 * bean销毁之前要执行的操作
	 */
	protected void doPreDestory() {
	}

	/**
	 * 获取entity
	 * 
	 * @return
	 */
	private T fetchEntity() {
		if (isIdSet()) {
			return doLoadEntity();// 获取
		}
		return doCreateEntity();
	}

	/**
	 * 加载Bean
	 * 
	 * @return
	 */
	protected T doLoadEntity() {
		if (this.isIdSet()) {
			return getBaseDao().find(key);
		}
		return doCreateEntity();
	}

	/**
	 * 创建Bean
	 * 
	 * @return
	 */
	protected T doCreateEntity() {
		Class<T> type = getEntityClass();
		T entity = null;
		try {
			entity = type.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}

	/**
	 * 执行保存相关的功能的方法,如果操作正确返回一个StatusCode.SUCCESS
	 * 
	 * @return StatusCode.SUCCESS
	 */
	protected abstract StatusCode doInsert();

	@Override
	public String insert() {
		return doInsert() == StatusCode.SUCCESS ? endConversation() : null;
	}

	/**
	 * 执行更新相关的功能的方法,如果操作正确返回一个StatusCode.SUCCESS
	 * 
	 * @return StatusCode.SUCCESS
	 */
	protected abstract StatusCode doUpdate();

	@Override
	public String update() {
		return doUpdate() == StatusCode.SUCCESS ? endConversation() : null;
	}

	/**
	 * 执行删除相关的功能的方法,如果操作正确返回一个StatusCode.SUCCESS
	 * 
	 * @return StatusCode.SUCCESS
	 */
	protected abstract StatusCode doDelete();

	@Override
	public String delete() {
		return doDelete() == StatusCode.SUCCESS ? endConversation() : null;
	}

	/**
	 * 取消
	 */
	@Override
	public String cancel() {
		return endConversation();
	}

	@Override
	public boolean isIdSet() {
		return key != null;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getUrl() {
		return url;
	}

	/**
	 * 获取一个实体类
	 */
	@Override
	public T getEntity() {
		if (entity == null) {
			entity = fetchEntity();
		}
		return entity;
	}

	@Override
	public void setEntity(T entity) {
		this.entity = entity;
	}

	@Override
	public ID getId() {
		return key;
	}

	@Override
	public void setId(ID key) {
		this.key = key;
	}

	@SuppressWarnings("unchecked")
	public Class<T> getEntityClass() {
		if (entityClass == null) {
			ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();

			Object value = parameterizedType.getActualTypeArguments()[0];
			if (value instanceof Class) {
				return (Class<T>) value;
			} else {
				throw new IllegalArgumentException("无法提取通用类型信息,请使用setEntityClass()手动设置.");
			}
		}
		return entityClass;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

}
