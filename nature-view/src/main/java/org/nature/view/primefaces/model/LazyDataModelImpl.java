package org.nature.view.primefaces.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nature.platform.persistence.dao.BaseDao;
import org.nature.platform.persistence.query.jpql.JoinBuilder;
import org.nature.platform.persistence.query.jpql.QueryBuilder;
import org.nature.platform.persistence.query.jpql.Restriction;
import org.nature.platform.persistence.utils.EntityUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * primefaces 懒加载模型
 * 
 * @author hutianlong
 *
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public class LazyDataModelImpl<T> extends LazyDataModel {
	private boolean debug = false;
	private static final Logger logger = Logger.getLogger(LazyDataModelImpl.class.getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = -5985718884949828653L;

	private BaseDao<T, java.io.Serializable> baseDao;

	private List<Restriction> restrictions;
	private List<Restriction> queryRestrictions;
	private Restriction restriction;
	private JoinBuilder joinBuilder;
	private String defaultOrder;
	private String currentOrderBy;
	private String attributes;
	private boolean loadData = true;
	private Integer currentRowCount;
	private LazyCountType lazyCountType;
	private OrderByHandler orderByHandler;

	public LazyDataModelImpl() {
	}

	/**
	 * 
	 * @param attributes
	 *            将要加载的对象的属性
	 * @param defaultOrder
	 *            默认排序
	 * @param restriction
	 *            限制条件
	 * @param dao
	 *            实现Dao
	 */
	public LazyDataModelImpl(String attributes, String defaultOrder, Restriction restriction,
			BaseDao<T, java.io.Serializable> dao) {
		this.baseDao = dao;
		this.attributes = attributes;
		this.defaultOrder = defaultOrder;
		this.restriction = restriction;
	}

	/**
	 * @param attributes
	 *            将要加载的对象的属性
	 * @param defaultOrder
	 *            默认 data model "Order By"
	 * @param restrictions
	 *            限制条件
	 * @param
	 */
	public LazyDataModelImpl(String attributes, String defaultOrder, List<Restriction> restrictions,
			BaseDao<T, java.io.Serializable> dao) {
		this.baseDao = dao;
		this.attributes = attributes;
		this.defaultOrder = defaultOrder;
		this.restrictions = restrictions;
	}

	/**
	 * @param 默认
	 *            data model "Order By"
	 * @param restriction
	 *            限制条件
	 * @param dao
	 */
	public LazyDataModelImpl(String defaultOrder, Restriction restriction, BaseDao<T, java.io.Serializable> dao) {
		this.baseDao = dao;
		this.defaultOrder = defaultOrder;
		this.restriction = restriction;
	}

	/**
	 *
	 * @param defaultOrder
	 *            默认 data model "Order By"
	 * @param restrictions
	 *            限制条件
	 * @param dao
	 */
	public LazyDataModelImpl(String defaultOrder, List<Restriction> restrictions,
			BaseDao<T, java.io.Serializable> dao) {
		this.baseDao = dao;
		this.defaultOrder = defaultOrder;
		this.restrictions = restrictions;
	}

	/**
	 * @param defaultOrder
	 *            默认 data model "Order By"
	 * @param dao
	 */
	public LazyDataModelImpl(String defaultOrder, BaseDao<T, java.io.Serializable> dao) {
		this.baseDao = dao;
		this.defaultOrder = defaultOrder;
	}

	/**
	 *
	 * @param defaultOrder
	 *            默认 data model "Order By"
	 * @param restriction
	 *            限制条件
	 * @param
	 * @param joinBuilder
	 */
	public LazyDataModelImpl(String defaultOrder, Restriction restriction, BaseDao<T, java.io.Serializable> dao,
			JoinBuilder joinBuilder) {
		this.baseDao = dao;
		this.defaultOrder = defaultOrder;
		this.restriction = restriction;
		this.joinBuilder = joinBuilder;
	}

	/**
	 *
	 * @param defaultOrder
	 *            默认 data model "Order By"
	 * @param restrictions
	 *            限制条件
	 * @param
	 * @param
	 */
	public LazyDataModelImpl(String defaultOrder, List<Restriction> restrictions, BaseDao<T, java.io.Serializable> dao,
			JoinBuilder joinBuilder) {
		this.baseDao = dao;
		this.defaultOrder = defaultOrder;
		this.restrictions = restrictions;
		this.joinBuilder = joinBuilder;
	}

	/**
	 *
	 * @param defaultOrder
	 *            默认 data model "Order By"
	 * @param
	 * @param
	 */
	public LazyDataModelImpl(String defaultOrder, BaseDao<T, java.io.Serializable> dao, JoinBuilder joinBuilder) {
		this.baseDao = dao;
		this.defaultOrder = defaultOrder;
		this.joinBuilder = joinBuilder;
	}

	public String getOrderBy(String orderBy, SortOrder order) {
		// 当指定排序为空时采用默认排序
		if (orderBy == null || orderBy.trim().isEmpty()) {
			orderBy = defaultOrder;
		} else {
			OrderByHandler orderByHandler = getOrderByHandler();
			// 当排序处理程序不为空时执行排序
			if (orderByHandler != null) {
				orderBy = orderByHandler.getOrderBy(orderBy);
			} else {
				if (joinBuilder != null && joinBuilder.getRootAlias() != null) {
					orderBy = joinBuilder.getRootAlias() + "." + orderBy;
				}
			}

			if (order.equals(SortOrder.DESCENDING)) {
				orderBy = orderBy + " DESC";
			}
		}

		return orderBy;
	}

	@Override
	public List<T> load(int first, int pageSize, String orderBy, SortOrder order, Map filters) {
		if (isLoadData() == false) {
			setRowCount(0);
			return null;
		}

		long begin = System.currentTimeMillis();

		// 获得懒加载统计类型，如果是空将赋于默认的ALWAYS
		LazyCountType lazyCountType = getLazyCountType();
		if (lazyCountType == null) {
			lazyCountType = LazyCountType.ALWAYS;
		}

		// 获得排序
		orderBy = getOrderBy(orderBy, order);

		if (debug) {
			logger.log(Level.INFO, "Lazy Count Type: {0}. Using order by {1}", new Object[] { lazyCountType, orderBy });
		}

		// 获取当前查询条件
		List<Restriction> currentQueryRestrictions = getCurrentQueryRestrictions();

		this.currentOrderBy = orderBy;

		String select = null;
		// 当前查询属性不为空时赋与select,否则当join不为空是赋于select
		if (attributes != null && !attributes.isEmpty()) {
			select = attributes;
		} else if (joinBuilder != null && joinBuilder.getRootAlias() != null && !joinBuilder.getRootAlias().isEmpty()) {
			select = joinBuilder.getRootAlias();
		}

		boolean restrictionsChanged = !currentQueryRestrictions.equals(queryRestrictions);
		// 更新当前 restrictions
		queryRestrictions = currentQueryRestrictions;

		// 添加了独特的验证
		QueryBuilder queryBuilder = buildQueryBuilder();
		if (joinBuilder != null && joinBuilder.isDistinct()) {
			queryBuilder.selectDistinct(select);
		} else {
			queryBuilder.select(select);
		}

		List<T> dados = queryBuilder.orderBy(orderBy).setFirstResult(first).setMaxResults(pageSize).getResultList();

		if (debug) {
			logger.log(Level.INFO, "Select on entity {0}, records found: {1} ",
					new Object[] { baseDao.getEntityClass().getName(), dados.size() });
		}

		// 如果 ALWAYS 或 (ONLY_ONCE 和 不是设置 currentRowCount或 restrictions 已经改变)
		if (lazyCountType.equals(LazyCountType.ALWAYS) || (lazyCountType.equals(LazyCountType.ONLY_ONCE)
				&& (currentRowCount == null || restrictionsChanged))) {

			QueryBuilder queryBuilderCount = buildQueryBuilder();
			// 添加了独特的验证
			if (joinBuilder != null && joinBuilder.isDistinct()) {
				currentRowCount = queryBuilderCount.countDistinct(joinBuilder.getRootAlias()).intValue();
			} else {
				currentRowCount = queryBuilderCount.count().intValue();
			}
			if (debug) {
				logger.log(Level.INFO, "Count on entity {0}, records found: {1} ",
						new Object[] { baseDao.getEntityClass().getName(), currentRowCount });
			}
			this.setRowCount(currentRowCount);
		}
		if (lazyCountType.equals(LazyCountType.ONLY_ONCE)) {
			this.setRowCount(currentRowCount);
		} else if (lazyCountType.equals(LazyCountType.NONE)) {
			currentRowCount = dados.size();
			this.setRowCount(Integer.MAX_VALUE);
		}

		if (debug) {
			long end = System.currentTimeMillis();
			logger.log(Level.INFO, "Load method executed in {0} milliseconds", (end - begin));
		}

		return dados;

	}

	/**
	 * 
	 * @return
	 */
	public boolean isLazyCountTypeNone() {
		LazyCountType lazyCountType = getLazyCountType();
		if (lazyCountType != null && lazyCountType.equals(LazyCountType.NONE)) {
			return true;
		}
		return false;
	}

	/**
	 * 从数据库返回计数对象，基于数据表中的过滤器
	 *
	 * @return
	 */
	public Long getCountAllResults() {

		QueryBuilder queryBuilderCount = baseDao.getQueryBuilder()
				.from(baseDao.getEntityClass(), (joinBuilder != null ? joinBuilder.getRootAlias() : null))
				.join(joinBuilder).add(getCurrentQueryRestrictions()).debug(debug);

		Long rowCount;
		// added distinct verification
		if (joinBuilder != null && joinBuilder.isDistinct()) {
			rowCount = queryBuilderCount.countDistinct(joinBuilder.getRootAlias());
		} else {
			rowCount = queryBuilderCount.count();
		}

		return rowCount;
	}

	/**
	 * 从数据库返回所有对象，基于数据表中的过滤器
	 *
	 * @param orderBy
	 * @return
	 */
	public List getAllResults(String orderBy) {
		return this.baseDao.getQueryBuilder()
				.from(baseDao.getEntityClass(), (joinBuilder != null ? joinBuilder.getRootAlias() : null))
				.select(attributes).add(getCurrentQueryRestrictions()).join(joinBuilder).orderBy(orderBy).debug(debug)
				.getResultList();
	}

	/**
	 * 当前查询条件
	 * 
	 * @return
	 */
	public List<Restriction> getCurrentQueryRestrictions() {
		List<Restriction> currentQueryRestrictions = new ArrayList<>();

		if (restrictions != null && !restrictions.isEmpty()) {
			currentQueryRestrictions.addAll(restrictions);
		}
		if (restriction != null) {
			currentQueryRestrictions.add(restriction);
		}

		return currentQueryRestrictions;
	}

	/**
	 * 返回一个字段的总和，这个方法获取QueryBuilder并添加当前限制来生成查询
	 *
	 * @param field
	 * @return
	 */
	public Object sum(String field) {
		return buildQueryBuilder().sum(field);
	}

	/**
	 * 返回字段的平均值，此方法获取QueryBuilder并添加当前限制来生成查询
	 *
	 * @param field
	 * @return
	 */
	public Object avg(String field) {
		return buildQueryBuilder().avg(field);
	}

	/**
	 * 返回字段的最小值，此方法获取QueryBuilder并添加当前限制以生成查询
	 *
	 * @param field
	 * @return
	 */
	public Object min(String field) {
		return buildQueryBuilder().min(field);
	}

	/**
	 * 返回字段的最大值，此方法获取QueryBuilder并添加当前限制以生成查询
	 *
	 * @param field
	 * @return
	 */
	public Object max(String field) {
		return buildQueryBuilder().max(field);
	}

	/**
	 * 构建查询
	 * 
	 * @return
	 */
	public QueryBuilder buildQueryBuilder() {
		return this.baseDao.getQueryBuilder()
				.from(baseDao.getEntityClass(), (joinBuilder != null ? joinBuilder.getRootAlias() : null))
				.join(joinBuilder).add(queryRestrictions).debug(debug);
	}

	@Override
	public Object getRowData(String rowKey) {
		if (rowKey != null && !rowKey.isEmpty()) {
			// convert id (id can be integer, long, string, etc...)
			Object id = EntityUtils.getIdFromString(rowKey, getBaseDao().getEntityClass());
			if (id != null) {
				return getBaseDao().find((Serializable) id);
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(Object object) {
		if (object != null) {
			return EntityUtils.getId(object);
		}
		return null;
	}

	public List<Restriction> getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(List<Restriction> restrictions) {
		this.restrictions = restrictions;
	}

	public List<Restriction> getQueryRestrictions() {
		return queryRestrictions;
	}

	public void setQueryRestrictions(List<Restriction> queryRestrictions) {
		this.queryRestrictions = queryRestrictions;
	}

	public Restriction getRestriction() {
		return restriction;
	}

	public void setRestriction(Restriction restriction) {
		this.restriction = restriction;
	}

	public JoinBuilder getJoinBuilder() {
		return joinBuilder;
	}

	public void setJoinBuilder(JoinBuilder joinBuilder) {
		this.joinBuilder = joinBuilder;
	}

	public String getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(String defaultOrder) {
		this.defaultOrder = defaultOrder;
	}

	public String getCurrentOrderBy() {
		return currentOrderBy;
	}

	public void setCurrentOrderBy(String currentOrderBy) {
		this.currentOrderBy = currentOrderBy;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public BaseDao<T, java.io.Serializable> getBaseDao() {
		return baseDao;
	}

	public OrderByHandler getOrderByHandler() {
		return orderByHandler;
	}

	public boolean isLoadData() {
		return loadData;
	}

	public void setLoadData(boolean loadData) {
		this.loadData = loadData;
	}

	public LazyCountType getLazyCountType() {
		return lazyCountType;
	}

	public void setLazyCountType(LazyCountType lazyCountType) {
		this.lazyCountType = lazyCountType;
	}

	public Integer getCurrentRowCount() {
		return currentRowCount;
	}

	public void setCurrentRowCount(Integer currentRowCount) {
		this.currentRowCount = currentRowCount;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
