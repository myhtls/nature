package org.nature.platform.persistence.dao;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.nature.platform.persistence.query.jpql.QueryBuilder;
import org.nature.platform.persistence.utils.EntityUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.beans.PropertyDescriptor;
import javax.inject.Inject;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.slf4j.LoggerFactory;

/**
 * 
 * @author hutianlong
 *
 * @param <T>
 * @param <ID>
 */
public abstract class BaseDaoImpl<T, ID extends Serializable> implements BaseDao<T, ID> {
	private static final String[] columns = {};// 更改忽略的字段
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);

	/**
	 * 当前的数据实体操作对象
	 */
	private Class<T> entityClass;

	private @Inject EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		try {

			Type genericSupercclass = getClass().getGenericSuperclass();
			if (genericSupercclass != null && genericSupercclass instanceof ParameterizedType) {
				Type[] arguments = ((ParameterizedType) genericSupercclass).getActualTypeArguments();
				if (arguments != null && arguments.length > 0) {
					Object object = arguments[0];
					if (object instanceof Class<?>) {
						this.entityClass = (Class<T>) object;
					} else if (object instanceof Class) {
						this.entityClass = (Class<T>) object;
					}
				}
			}
		} catch (Exception e) {
			System.err.println("生成类出现导常---------------");
			System.err.println("在初始化对应的类错误非常严重");
			System.err.println("生成类出现导常---------------");
			// logger.log(Level.SEVERE, null, e);
		}
	}

	// 1.---以下是实现查询环节
	/**
	 * 根据主键ID值查找到受控的实体
	 * 
	 * @param id
	 *            主键id
	 * 
	 *            <p>
	 *            说明
	 *            <p>
	 *            当在数据库中没有找到记录时，getReference()和find()是有区别的，
	 *            find()方法会返回null，而getReference()
	 *            方法会抛出javax.persistence.EntityNotFoundException
	 *            例外，另外getReference()方法不保证 entity
	 *            Bean已被初始化。如果传递进getReference()或find()方法的参数 不是实体Bean，都会引发
	 *            IllegalArgumentException例外
	 * 
	 */
	@Override
	public T find(ID idValue) {
		if (idValue != null) {
			// 以主键查询实体对象，this.entityObj是实体的类，id是主键值，如以下的代码查询this.entityObj实体：
			return this.entityManager.find(this.entityClass, idValue);
		}
		return null;
	}

	/**
	 * @param idValue
	 * @param lockModeType
	 *            琐类型 lockModeType.OPTIMISTIC:乐观读取琐定(可重读(Repeatable
	 *            Read)),其防止所谓不可重复读取的异常.当同在一事务中对相同的数据事务
	 *            查询两次时，与第一次返回的数据相比，第二次查询将返回数据的不同版本，因为另一个事务在中间时间内修改了它。换言之,可重复读隔离级别意味着
	 *            一旦一个事务访问数据，且另一个事务修改了数据，就必须阻止至少一个事务的提交.
	 * 
	 *            lockModeType.OPTIMISTIC_FORCE_INCREMENT:乐观写入琐(optimistic write
	 *            lock),
	 */
	@Override
	public T find(ID idValue, LockModeType lockModeType) {

		if (idValue != null) {
			// 以主键查询实体对象，this.entityObj是实体的类，id是主键值，如以下的代码查询this.entityObj实体：
			return this.entityManager.find(this.entityClass, idValue, lockModeType);
		}
		return null;
	}

	@Override
	public T find(ID idValue, Map<String, Object> properties) {
		if (idValue != null) {
			// 以主键查询实体对象，this.entityObj是实体的类，id是主键值，如以下的代码查询this.entityObj实体：
			return this.entityManager.find(this.entityClass, idValue, properties);
		}
		return null;
	}

	@Override
	public T find(ID idValue, LockModeType lockModeType, Map<String, Object> properties) {

		if (idValue != null) {
			// 以主键查询实体对象，this.entityObj是实体的类，id是主键值，如以下的代码查询this.entityObj实体：
			return this.entityManager.find(this.entityClass, idValue, lockModeType, properties);
		}
		return null;
	}

	/**
	 * 查询所有数据,并返回list的object对象
	 */
	@Override
	public List<T> findAll() {
		CriteriaBuilder _cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = _cb.createQuery(this.entityClass);
		criteriaQuery.select(criteriaQuery.from(this.entityClass));
		return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();

	}

	/**
	 * 通过ids查询多个数据实体对象,并返回list的object对象
	 * 
	 * @param ids
	 *            多个id的数组
	 * @return 返回list的object对象
	 */
	@Override
	public List<T> findList(ID[] ids) {
		StringBuilder sb = new StringBuilder("select u from ").append(entityClass.getName()).append(" u where u.id ")
				.append(" exists(").append(ids).append(")");
		return entityManager.createQuery(sb.toString(), entityClass).getResultList();

	}

	@SuppressWarnings("unchecked")
	@Override
	public T findByColName(String colName, Object value) {

		String jpql = "SELECT u FROM " + this.entityClass.getName() + " u WHERE u." + colName + " = ?1";
		Query query = this.entityManager.createQuery(jpql);
		query.setParameter(1, value);// 给编号为1的参数设值
		List<?> lists = query.getResultList();
		if (null != lists && lists.size() > 0) {
			return (T) lists.get(0);
		} else {
			return null;
		}

	}

	// ----2.以下是实现命名查询环节
	/**
	 * 
	 * @param named
	 *            命名查询对应的命名对象
	 * @param paramMap
	 *            参数Map<参数名称, 参数的值>
	 * @return List<T>对应的实体集
	 */
	@Override
	public List<T> findListByNameQuery(String named, Map<String, Object> paramMap) {

		TypedQuery<T> typeQuery = this.entityManager.createNamedQuery(named, entityClass);
		paramMap.keySet().stream().forEach((key) -> {
			typeQuery.setParameter(key, paramMap.get(key));
		});
		return typeQuery.getResultList();
	}

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
	@Override
	public List<T> findListByNameQuery(String named, String paramName, Object value) {
		TypedQuery<T> typeQuery = entityManager.createNamedQuery(named, entityClass);
		typeQuery.setParameter(paramName, value);
		return typeQuery.getResultList();
	}

	/**
	 * 
	 * @param named
	 *            命名查询对应的命名对象
	 * @return List<T>对应的实体集
	 */
	@Override
	public List<T> findListByNameQuery(String named) {
		return this.entityManager.createNamedQuery(named, this.entityClass).getResultList();
	}

	/**
	 * 通过命名查询只返回唯一值
	 */
	@Override
	public T uniqueByNameQuery(String named, String paramName, Object value) {
		TypedQuery<T> typeQuery = entityManager.createNamedQuery(named, entityClass);
		typeQuery.setParameter(paramName, value);
		typeQuery.setMaxResults(1);
		List<T> result = typeQuery.getResultList();
		return result.isEmpty() ? null : result.get(0);
	}


	
	
	/**
	 * 通过命名查询并且多条件只返回唯一值
	 */
	@Override
	public T uniqueByNameQuery(String named, Map<String, Object> paramMap) {
		TypedQuery<T> typeQuery = this.entityManager.createNamedQuery(named, entityClass);
		paramMap.keySet().stream().forEach((key) -> {
			typeQuery.setParameter(key, paramMap.get(key));
		});
		typeQuery.setMaxResults(1);
		List<T> result = typeQuery.getResultList();
		return result.isEmpty() ? null : result.get(0);
	}

	/**
	 * 如果你怀疑当前被管理的实体已经不是数据库中最新的数据，你可以通过refresh()方法刷新实体 ，容器会把数据库中的新值重写进实体。
	 * 这种情况一般发 生在你获取了实体之后，有人更新了数据
	 * 库中的记录，这时你需要得到最新的数据。当然你再次调用find()或getReference()方法也可 以得到 最新数据，但这种做法并不优雅。
	 * 
	 * @param entity
	 *            需要进行刷新的实体
	 * 
	 *            <p>
	 *            例子
	 *            <p>
	 *            <code>
	 * Person person = em.find(Person.class, 2);
	 * //如果此时person 对应的记录在数据库中已经发生了改变，
	 * //可以通过refresh()方法得到最新数据。
	 * em.refresh (person);
	 * </code>
	 */
	@Override
	public void refresh(T entity) {
		if (entity != null)
			this.entityManager.refresh(entity);
	}

	/**
	 * 增加记录
	 * 
	 * @param t
	 *            待增加的实体bean
	 * 
	 *            <p>
	 *            说明:
	 *            <p>
	 *            如果传递进persist()方法的参数不是实体Bean，会引发IllegalArgumentException 例子:
	 *            <code>
	 * insert —— persist()
	 * Person person = new Person();
	 * person.setName(name);
	 * //把数据保存进数据库中
	 * em.persist(person);
	 * </code>
	 * 
	 */
	@Override
	public void save(T entity) {
		if (entity != null)
			this.entityManager.persist(entity);
	}

	/**
	 * 
	 * 更新记录,生成update
	 * 
	 * @param t
	 *            待增加的实体bean
	 */
	@Override
	public T update(T entity) {
		if (entity != null) {
			return this.entityManager.merge(entity);
		}
		return null;
	}

	/**
	 * 
	 * 
	 * @param entity
	 *            待更新的实体
	 * @param ignoreField
	 *            更新要忽略的整段
	 *            <p>
	 *            例子:
	 *            <p>
	 *            <code>new String[] { "RUuid", "RIssystem","admins" }</code>
	 * @return 更新实体记录到数据库,update
	 */
	@Override
	public T update(T entity, String[] ignoreFields) {

		if (entity == null)
			return null;

		// 检查这个实体是否还受容器管理
		if (isManaged(entity)) {
			throw new IllegalArgumentException("Entity must not be managed");
		}

		// 查出关联数据库的实体对象
		ID id = getIdentifier(entity);
		T dbEntity = find(id);

		if (dbEntity != null) {

			// 如果涉及到要忽略的字段，则对系统里核心的"createman","createtime"属性不进行更改。
			// String[] stro=new String[]{"city"};
			String[] tmp = new String[ignoreFields.length + 2];
			int i = 0;
			for (String ignorePropertie : ignoreFields) {
				tmp[i] = ignorePropertie;
				i++;
			}
			tmp[i] = "createdBy"; // 因为这个是创建人，所以不可以更新，整个系统都不可以
			i++;
			tmp[i] = "createdDate";// 因为这个是创建时间，所以不可以更新，整个系统都不可以
			// 编辑人，编辑时间则要用系统购置更新，不要更改

			// 对dbEntity关联数据库的实体进行赋值
			updateMergeEntity(entity, dbEntity, (String[]) ArrayUtils.addAll(tmp, columns));

			// 调用重载进行更新,并且进行生成静态页面,记住,这里要注意,如果有扩展的类,如文章,则会先调用文章下面的update
			return update(dbEntity);
		}
		return update(entity);
	}

	@Override
	public int updateByNamed(String named, String param, Object value) {
		Query query = entityManager.createNamedQuery(named);
		query.setParameter(param, value);
		return query.executeUpdate();
	}

	@Override
	public int updateByNamed(String named, Map<String, Object> paramMap) {
		Query query = entityManager.createNamedQuery(named);
		paramMap.keySet().stream().forEach((key) -> {
			query.setParameter(key, paramMap.get(key));
		});
		return query.executeUpdate();

	}

	/**
	 * 通过对托管的实体对象执行物理删除
	 * 
	 * @param t
	 *            受控待删除的实体
	 */
	@Override
	public void delete(T entity) {
		if (entity != null) {
			this.entityManager.remove(entity);
		}

	}

	@Override
	public void delete(ID id) {
		delete(find(id));
	}

	@Override
	public void delete(ID[] ids) {
		if (ids != null) {
			for (ID id : ids) {
				/**
				 * 一条条记录执行删除,这里有一个技巧,查询出的实体,如果extends子类,
				 * 有实现对应的接口,会先调用相关的子类,就是重载实现一样,再调这里的基本delete,
				 * 这样很重要,方便在子类处理删除前必做的任务
				 */
				delete(find(id));
			}
		}
	}

	/**
	 * 删除实体表所有的记录
	 */
	@Override
	public void deleteAll() {
		String tbName = this.entityClass.getName();
		tbName = tbName.substring(tbName.lastIndexOf('.') + 1, tbName.length());
		String delSql = "delete from " + tbName;
		// JPA的FlushModeType只有两种： 建议刷新，不然会在查询时，由于二级缓存的影响，不能及时取出数据。
		// 1、COMMIT：仅当提交事务时才能进行刷新
		// 2、AUTO：（默认）在执行查询时进行刷新
		this.entityManager.createQuery(delSql).setFlushMode(FlushModeType.COMMIT).executeUpdate();
	}

	// ----------其它控管方法的实现--------------

	/**
	 * 通过对实体查询返回相关的资源id
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ID getIdentifier(T entity) {
		if (entity != null) {
			// 通过实体返回相关的资源id
			Object id = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
			// return (ID) this.entityManager.getEntityManagerFactory()
			// .getPersistenceUnitUtil().getIdentifier(entity);
			return (ID) id;
		}
		return null;
	}

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
	public boolean isManaged(T entity) {
		return this.entityManager.contains(entity);
	}

	// -------私有方法的实现
	/**
	 * 对通过Id查询到数据库表的资源实体对象进行赋值 实现把sourceEntity来源的实体复制给到从数据库里查询出更新的实体dbEntity
	 * 
	 * @param sourceEntity
	 *            要保存的实体对象
	 * @param dbEntity
	 *            通过Id查询到数据库表的资源实体对象
	 * @param ignoreField
	 *            忽略的列名
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateMergeEntity(Object sourceEntity, Object dbEntity, String[] ignoreField) {

		if (sourceEntity == null) {
			logger.error("Source entity must not be null");
			return;
		}

		if (dbEntity == null) {
			logger.error("Target entity must not be null");
			return;
		}

		// 复制出数据库对应的实体对象所有的属性
		PropertyDescriptor[] _propArr = PropertyUtils.getPropertyDescriptors(dbEntity.getClass());

		// 对忽略的字段进行转成list对象,方便操作
		List<String> _fieldList = ignoreField != null ? Arrays.asList(ignoreField) : null;

		// 循环对对象的属性赋值
		for (PropertyDescriptor _propT : _propArr) {

			// 如果这个属性可以转成set或者get的方法,并且忽略字段为空或者属性不在忽略的字段,就一个个进行赋值
			if ((_propT.getWriteMethod() != null)
					&& ((ignoreField == null) || (!_fieldList.contains(_propT.getName())))) {

				// 从源bean里取得同名的属性对象
				PropertyDescriptor _prop = null;

				try {
					String _proptName = _propT.getName();
					_prop = PropertyUtils.getPropertyDescriptor(sourceEntity, _proptName); // sourceEntity.getClass()
																							// 改成sourceEntity

				} catch (Exception e) {

					e.printStackTrace();
				}

				// 如果值不为空_prop.getReadMethod()取出getter方法对象
				if ((_prop != null) && (_prop.getReadMethod() != null)) {
					try {

						Method _getMethod = _prop.getReadMethod();// getReadMethod()得到此属性的get方法----Method对象

						// 参数包括 public 修饰符，则返回 true，否则返回 false。
						if (!Modifier.isPublic(_getMethod.getDeclaringClass().getModifiers())) {
							/*
							 * * 将此对象的 accessible 标志设置为指示的布尔值。值为 true
							 * 则指示反射的对象在使用时应该取消 Java 语言访问检查。值为 false
							 * 则指示反射的对象应该实施 Java 语言访问检查。实际上setAccessible
							 * 是启用和禁用访问安全检查的开关,并不是为true就能访问为false就不能访问
							 * 设为true的话就是跳过访问检查，也就可以访问private的东西 false就是强制访问检查
							 */
							// 使用了method.setAccessible(true)后 性能有了20倍的提升
							// Accessable属性是继承自AccessibleObject 类. 功能是启用或禁用安全检查
							_getMethod.setAccessible(true);
						}

						// invoke调用这个方法对应的get方法返回值,Object[0]来代替null
						Object sourceGet = _getMethod.invoke(sourceEntity, new Object[0]);
						Object targetGet = _getMethod.invoke(dbEntity, new Object[0]);

						// 如果目标源是一个 集合（Collection）对象
						if ((sourceGet != null) && (targetGet != null) && ((targetGet instanceof Collection))) {

							Collection _value = (Collection) targetGet;
							_value.clear(); // 清空原来的集合数据
							_value.addAll((Collection) sourceGet);// 复制整个集合数据到源目标

						} else {
							// 得到此属性的set方法----Method对象,然后用invoke调用这个方法
							Method _methodSet = _propT.getWriteMethod();
							if (!Modifier.isPublic(((Method) _methodSet).getDeclaringClass().getModifiers())) {
								((Method) _methodSet).setAccessible(true);
							}
							// 其它非集合对象,则用set模式进行赋值
							((Method) _methodSet).invoke(dbEntity, new Object[] { sourceGet });
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public Class<T> getEntityClass() {
		return entityClass;
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		return new QueryBuilder(entityManager);
	}

	/**
	 * 琐
	 */
	@Override
	public void lock(LockModeType locketModeType) {
		entityManager.lock(entityClass, locketModeType);
	}

	@Override
	public Long countByNamed(String jpql, String param, Object value) {
		TypedQuery<Long> typeQuery = entityManager.createNamedQuery(jpql, Long.class);
		typeQuery.setParameter(param, value);
		return typeQuery.getResultList().get(0);
	}

	@Override
	public Object findAttribute(String attributeName, ID id) {

		QueryBuilder builder = getQueryBuilder();

		return builder.select("o." + attributeName).from(getEntityClass(), "o")
				.add("o." + EntityUtils.getIdFieldName(getEntityClass()), id).getSingleResult();
	}

	@Override
	public Object findAttribute(String attributeName, Object object) {
		return findAttribute(attributeName, (Number) EntityUtils.getId(object));
	}

	@Override
	public void evictById(ID id) {
		Cache cache = entityManager.getEntityManagerFactory().getCache();
		cache.evict(getEntityClass(), id);
	}

	@Override
	public void evictById(Class<?> clases, ID id) {
		Cache cache = entityManager.getEntityManagerFactory().getCache();
		cache.evict(getEntityClass(), id);
	}

}
