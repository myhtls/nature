package org.nature.view.primefaces.model;

/**
 * 定义LazyDataModelImpl的计数类型，用于查询计数记录
 * 
 * @author hutianlong
 *
 */
public enum LazyCountType {

	/**
	 * 始终计数数据（即使在分页事件中也执行计数，但仅当过滤器更改时）。它是LazyDataModel Impl的默认值
	 */
	ALWAYS,
	/**
	 * 不要计数数据。 对于具有许多记录的数据表有用。
	 */
	NONE,
	/**
	 *只计算一次数据。 下一页将获得第一个计数。
	 */
	ONLY_ONCE;

}
