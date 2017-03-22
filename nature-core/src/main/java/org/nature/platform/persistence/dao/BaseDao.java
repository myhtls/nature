package org.nature.platform.persistence.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;

import org.nature.platform.persistence.query.jpql.QueryBuilder;

/**
 * dao层接口
 * 
 * @author hutianlong
 *
 * @param <T>
 *            实例参数
 * @param <ID>
 *            主健参数
 */
public interface BaseDao <T, ID extends java.io.Serializable>{
	
	// 以下是实现查询的环节--------------------
		/**
		 * 根据主键ID值查找到受控的实体
		 * 
		 * @param id
		 *            主键id
		 * 
		 *            <p>
		 * 			说明
		 *            <p>
		 *            当在数据库中没有找到记录时，getReference()和find()是有区别的，
		 *            find()方法会返回null，而getReference()
		 *            方法会抛出javax.persistence.EntityNotFoundException
		 *            例外，另外getReference()方法不保证 entity
		 *            Bean已被初始化。如果传递进getReference()或find()方法的参数 不是实体Bean，都会引发
		 *            IllegalArgumentException例外
		 * 
		 */
		public T find(ID idValue);
		
		/**
		 * 根据主键ID值查找到受控的实体 
		 * @param idValue
		 * @param lockModeType 琐类型
		 * @return
		 */
		public T find(ID idValue,LockModeType lockModeType);
		
		/**
		 * 
		 * @param idValue
		 * @param properties 标准和供应商特定的属性和提示
		 * @return
		 */
		public T find(ID idValue,Map<String, Object> properties);
		
		/**
		 * 
		 * @param idValue
		 * @param lockModeType
		 * @param properties 标准和供应商特定的属性和提示
		 * @return
		 */
		public T find(ID idValue,LockModeType lockModeType,Map<String, Object> properties);

		/**
		 * 
		 * @param colName
		 *            列名
		 * @param value
		 *            列名的值
		 * @return 数据实体对象
		 */
		public T findByColName(String colName, Object value);

		/**
		 * 查询所有数据,并返回list的object对象
		 */
		public List<T> findAll();

		/**
		 * 通过ids查询多个数据实体对象,并返回list的object对象
		 * 
		 * @param ids
		 *            多个id的数组
		 * @return 返回list的object对象
		 */
		public List<T> findList(ID[] ids);

		// -----------以上缺内存分页

		// 以下是实现命名查询-----------

		/**
		 * 
		 * @param named
		 *            命名查询对应的命名对象
		 * @param paramMap
		 *            参数Map<参数名称, 参数的值>
		 * @return List<T>对应的实体集
		 */
		public List<T> findListByNameQuery(String named, Map<String, Object> paramMap);

		/**
		 * 
		 * @param named
		 *            命名查询对应的命名对象
		 * @param paramName
		 *            参数名称
		 * @param value
		 *            参数的值
		 * @return List<T>对应的实体集
		 */
		public List<T> findListByNameQuery(String named, String paramName, Object value);

		/**
		 * 
		 * @param named
		 *            命名查询对应的命名对象
		 * @return List<T>对应的实体集
		 */
		public List<T> findListByNameQuery(String named);
		
		/**
		 * 通过命名查询只返回唯一值
		 * @param named
		 * @param paramName
		 * @param value
		 * @return
		 */
		public T uniqueByNameQuery(String named,String paramName,Object value);
		
		/**
		 * 通过命名查询并且多条件只返回唯一值
		 * @param named
		 * @param paramMap
		 * @return
		 */
		public T uniqueByNameQuery(String named, Map<String, Object> paramMap);

		/**
		 * 
		 * @param t
		 *            对实体进行刷新
		 */
		public void refresh(T t);

		/**
		 * 增加记录
		 * 
		 * @param t
		 *            待增加的实体bean
		 * 
		 *<p>
		 *说明:
		 *<p>
		 *如果传递进persist()方法的参数不是实体Bean，会引发IllegalArgumentException 例子:
		 * <code>
		 * insert —— persist()
		 * Person person = new Person();
		 * person.setName(name);
		 * //把数据保存进数据库中
		 * em.persist(person);
		 * </code>
		 * 
		 */
		public void save(T t);

		/**
		 * 更新一个实体到数据库,生成update
		 * <p>
		 * 提示:
		 * <p>
		 * 在实体Bean已经脱离了EntityManager的管理时，你调用实体的set方法对数据进行 修改是无法同步更改到数据库的。你必须调用
		 * EntityManager.merge()方法.
		 * 
		 * 如果该实体Bean是一个全新的bean时，调用Merge方法会是进行插入.
		 * 
		 * @param entity
		 *            受控制的实体
		 */
		public T update(T entity);

		/**
		 * 更新实体记录到数据库,可以设置忽略字段
		 * 
		 * @param entity
		 *            待更新的实体
		 * @param ignoreField
		 *            更新要忽略的字段
		 *            <p>
		 * 			例子:
		 *            <p>
		 *            <code>new String[] { "RUuid", "RIssystem","admins" }</code>
		 * 
		 * @return 受影响的实体
		 */
		public T update(T entity, String[] ignoreFields);
		
		public int updateByNamed(String named,String param,Object value);
		public int updateByNamed(String named,Map<String, Object> paramMap);

		/**
		 * 通过受控待删除的实体对象执行物理删除
		 * 
		 * @param t
		 *            受控待删除的实体
		 */
		public void delete(T t);

		/**
		 * 删除实体表所有的记录
		 */
		public void deleteAll();

		/**
		 * 
		 * @param id
		 *            删除指定id主键的记录
		 */
		public void delete(ID id);

		/**
		 * 通过前端传入的主键ID组对数据进行物理删除
		 * 
		 * @param ids
		 *            传入主键ID组数据
		 */
		public void delete(ID[] ids);

		// 其它的实现
		/**
		 * 通过对实体查询返回相关的资源id
		 */
		public ID getIdentifier(T t);

		/**
		 * Check entity是否在EntityManager管理当中 —— contains()
		 * <p>
		 * 解释
		 * <p>
		 * contains()方法使用一个实体作为参数，如果这个实体对象当前正被持久化内容管理，返回值为true， 否则为false。如果传递的参数不是实体
		 * Bean，将会引发一个IllegalArgumentException. <code>
			Person person = em.find(Person.class, 2);
			。。。
			if (em.contains(person)){
			//正在被持久化内容管理
			}else{
			//已经不受持久化内容管理
			}
		</code>
		 */
		public boolean isManaged(T t);
		
		public Class<T> getEntityClass();
		
		public QueryBuilder getQueryBuilder();
		
		public void lock(LockModeType locketModeType);
		
		public Long countByNamed(String jpql,String param,Object value);

}
