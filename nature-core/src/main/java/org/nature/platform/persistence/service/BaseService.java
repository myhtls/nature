package org.nature.platform.persistence.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;


import org.nature.platform.persistence.dao.BaseDao;
import org.nature.platform.persistence.dao.BaseDaoImpl;
import org.slf4j.LoggerFactory;

/**
 * 基础服务类
 * @author hutianlong
 *
 * @param <T>
 * @param <E>
 */

public class BaseService<T,E extends BaseDao<T, ?>> {
	
	private E baseDao;
	private Class<E> baseDaoClass;
	private @Inject Instance<E> baseDaoInstace;
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public  BaseService(){
	    try {
            Type genericSuperclass = getClass().getGenericSuperclass();
            if (genericSuperclass != null && genericSuperclass instanceof ParameterizedType) {
                Type[] arguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
                if (arguments != null && arguments.length > 0) {
                    Object object = arguments[1];
                    if (object instanceof Class<?>) {
                    	baseDaoClass = (Class<E>) object;
                    } else {
                        if (object instanceof Class) {
                        	baseDaoClass = (Class) object;
                        }
                    }
                }
            }
          } catch (Exception ex) {
            logger.debug("BaseService:"+ex.getMessage());
        }
	}
	
	/**
	 * 加载数据
	 */
	protected void loadData(){}
	
	@PostConstruct
	public void init(){
		 baseDao = baseDaoInstace.select(baseDaoClass).get();
		loadData();
	}

	public E getBaseDao() {
		return baseDao;
	}
	
	/**
	 * 销毁时所做动作
	 */
	protected void doPreDestory(){}

	/**
	 * 销毁
	 */
	@PreDestroy
	private void preDestory() {
		doPreDestory();
	}
	
	
	
	

}
