package org.nature.platform.persistence.query.jpql;

import javax.persistence.TemporalType;

/**
 * 查询参数
 * 
 * @author hutianlong
 *
 */
public class QueryParameter {
	private Integer position;
	private String property;
	private Object value;
	private TemporalType temporalType;

	public QueryParameter(int position, Object value, TemporalType temporalType) {
		this.position = position;
		this.value = value;
		this.temporalType = temporalType;
	}

	public QueryParameter(int position, Object value) {
		this.position = position;
		this.value = value;
	}

	public QueryParameter(String property, Object value) {
		this.property = property;
		this.value = value;
	}

	public QueryParameter(String property, Object value, TemporalType temporalType) {
		this.property = property;
		this.value = value;
		this.temporalType = temporalType;
	}

	/**
	 * 实体属性
	 * @return
	 */
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * 对应条件参数？后面的数值，也是索引位置，如nome = ?1 OR nome = ?2里的1和2
	 * @return
	 */
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	/**
	 * 实体查询值
	 * @return
	 */
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * 日期类型
	 * @return
	 */
	public TemporalType getTemporalType() {
		return temporalType;
	}

	public void setTemporalType(TemporalType temporalType) {
		this.temporalType = temporalType;
	}

	@Override
	public String toString() {
		return "QueryParameter{" + "position=" + position + ", value=" + value + ", temporalType=" + temporalType + '}';
	}

}
