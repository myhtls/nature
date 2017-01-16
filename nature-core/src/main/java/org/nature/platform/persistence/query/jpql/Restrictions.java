package org.nature.platform.persistence.query.jpql;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.TemporalType;

/**
 * 限制条件查询
 * @author hutianlong
 *
 */
public class Restrictions extends ArrayList<Restriction>{
	 /**
     * 增加一个 RestrictionType.MEMBER_OF (value 'member of' property)
     *
     * @param value
     * @param property
     * @return Current Restrictions with added restriction
     */
    public Restrictions memberOf(Object value, String property) {
        this.add(new Restriction(property, RestrictionType.MEMBER_OF, value));
        return this;
    }

    /**
     * 增加一个 RestrictionType.NOT_MEMBER_OF (value 'member of' property)
     *
     * @param value
     * @param property
     * @return Current Restrictions with added restriction
     */
    public Restrictions notMemberOf(Object value, String property) {
        this.add(new Restriction(property, RestrictionType.NOT_MEMBER_OF, value));
        return this;
    }

    /**
     * 增加一个 restriction
     *
     * @param property
     * @param restrictionType
     * @return Current Restrictions with added restriction
     */
    public Restrictions add(String property, RestrictionType restrictionType) {
        this.add(new Restriction(property, restrictionType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.EQUALS
     *
     * @param property
     * @param value
     * @return Current Restrictions with added restriction
     */
    public Restrictions add(String property, Object value) {
        this.add(new Restriction(property, value));
        return this;
    }

    /**
     * 增加一个 Date restriction
     *
     * @param property
     * @param restrictionType
     * @param value
     * @param temporalType
     * @return Current Restrictions with added restriction
     */
    public Restrictions add(String property, RestrictionType restrictionType, Date value, TemporalType temporalType) {
        this.add(new Restriction(property, restrictionType, value, temporalType));
        return this;
    }

    /**
     * 增加一个 Calendar restriction
     *
     * @param property
     * @param restrictionType
     * @param value
     * @param temporalType
     * @return Current Restrictions with added restriction
     */
    public Restrictions add(String property, RestrictionType restrictionType, Calendar value, TemporalType temporalType) {
        this.add(new Restriction(property, restrictionType, value, temporalType));
        return this;
    }

    /**
     * 增加一个 restriction
     *
     * @param property
     * @param restrictionType
     * @param value
     * @return Current Restrictions with added restriction
     */
    public Restrictions add(String property, RestrictionType restrictionType, Object value) {
        this.add(new Restriction(property, restrictionType, value));
        return this;
    }

    /**
     * 增加一个 restriction
     *
     * @param property
     * @param restrictionType
     * @param value
     * @param likeType
     * @return Current Restrictions with added restriction
     */
    public Restrictions add(String property, RestrictionType restrictionType, Object value, LikeType likeType) {
        this.add(new Restriction(property, restrictionType, value, likeType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.QUERY_STRING
     *
     * @param property
     * @return Current Restrictions with added restriction
     */
    public Restrictions addQueryString(String property) {
        this.add(new Restriction(property, RestrictionType.QUERY_STRING));
        return this;
    }

    /**
     * 增加一个 RestrictionType.QUERY_STRING
     *
     * @param property
     * @param parameters
     * @return Current Restrictions with added restriction
     */
    public Restrictions addQueryString(String property, List<QueryParameter> parameters) {
        Restriction restriction = new Restriction(property, RestrictionType.QUERY_STRING);
        restriction.setParameters(parameters);
        this.add(restriction);
        return this;
    }

    /**
     *增加一个 RestrictionType.QUERY_STRING
     *
     * @param property
     * @param parameter
     * @return Current Restrictions with added restriction
     */
    public Restrictions addQueryString(String property, QueryParameter parameter) {
        Restriction restriction = new Restriction(property, RestrictionType.QUERY_STRING);
        List<QueryParameter> parameters = new ArrayList<QueryParameter>();
        parameters.add(parameter);
        restriction.setParameters(parameters);
        this.add(restriction);
        return this;
    }

    /**
     *增加一个 RestrictionType.NULL (property 'is null')
     *
     * @param property
     * @return Current Restrictions with added restriction
     */
    public Restrictions isNull(String property) {
        this.add(new Restriction(property, RestrictionType.NULL));
        return this;
    }

    /**
     * 增加一个 RestrictionType.NOT_NULL (property 'is not null')
     *
     * @param property
     * @return Current Restrictions with added restriction
     */
    public Restrictions isNotNull(String property) {
        this.add(new Restriction(property, RestrictionType.NOT_NULL));
        return this;
    }

    /**
     * 增加一个 RestrictionType.EMPTY (property 'is empty')
     *
     * @param property
     * @return Current Restrictions with added restriction
     */
    public Restrictions isEmpty(String property) {
        this.add(new Restriction(property, RestrictionType.EMPTY));
        return this;
    }

    /**
     * 增加一个 RestrictionType.NOT_EMPTY (property 'is not empty')
     *
     * @param property
     * @return Current Restrictions with added restriction
     */
    public Restrictions isNotEmpty(String property) {
        this.add(new Restriction(property, RestrictionType.NOT_EMPTY));
        return this;
    }

    /**
     * 增加一个 RestrictionType.LIKE (property 'like' value)
     *
     * @param property
     * @param value
     * @return Current Restrictions with added restriction
     */
    public Restrictions like(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.LIKE, value));
        return this;
    }

    /**
     * 增加一个 RestrictionType.LIKE (property 'like' value)
     *
     * @param property
     * @param value
     * @param likeType
     * @return Current Restrictions with added restriction
     */
    public Restrictions like(String property, Object value, LikeType likeType) {
        this.add(new Restriction(property, RestrictionType.LIKE, value, likeType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.NOT_LIKE (property 'not like' value)
     *
     * @param property
     * @param value
     * @return Current Restrictions with added restriction
     */
    public Restrictions notLike(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.NOT_LIKE, value));
        return this;
    }

    /**
     * 增加一个 RestrictionType.NOT_LIKE (property 'not like' value)
     *
     * @param property
     * @param value
     * @param likeType
     * @return Current Restrictions with added restriction
     */
    public Restrictions notLike(String property, Object value, LikeType likeType) {
        this.add(new Restriction(property, RestrictionType.NOT_LIKE, value, likeType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.GREATER_THAN (property '>' value)
     *
     * @param property
     * @param value
     * @return Current Restrictions with added restriction
     */
    public Restrictions greaterThan(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.GREATER_THAN, value));
        return this;
    }

    /**
     *增加一个 Date RestrictionType.GREATER_THAN (property '>' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current Restrictions with added restriction
     */
    public Restrictions greaterThan(String property, Date value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.GREATER_THAN, value, temporalType));
        return this;
    }

    /**
     * 增加一个 Calendar RestrictionType.GREATER_THAN (property '>' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current Restrictions with added restriction
     */
    public Restrictions greaterThan(String property, Calendar value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.GREATER_THAN, value, temporalType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.GREATER_EQUALS_THAN (property '>=' value)
     *
     * @param property
     * @param value
     * @return Current Restrictions with added restriction
     */
    public Restrictions greaterEqualsThan(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.GREATER_EQUALS_THAN, value));
        return this;
    }

    /**
     * 增加一个 Date RestrictionType.GREATER_EQUALS_THAN (property '>=' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current Restrictions with added restriction
     */
    public Restrictions greaterEqualsThan(String property, Date value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.GREATER_EQUALS_THAN, value, temporalType));
        return this;
    }

    /**
     * 增加一个 Calendar RestrictionType.GREATER_EQUALS_THAN (property '>=' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current Restrictions with added restriction
     */
    public Restrictions greaterEqualsThan(String property, Calendar value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.GREATER_EQUALS_THAN, value, temporalType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.LESS_THAN (property '&lt' value)
     *
     * @param property
     * @param value
     * @return Current Restrictions with added restriction
     */
    public Restrictions lessThan(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.LESS_THAN, value));
        return this;
    }

    /**
     * 增加一个 Date RestrictionType.LESS_THAN (property '&lt' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current Restrictions with added restriction
     */
    public Restrictions lessThan(String property, Date value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.LESS_THAN, value, temporalType));
        return this;
    }

    /**
     * 增加一个 Calendar RestrictionType.LESS_THAN (property '&lt' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return
     */
    public Restrictions lessThan(String property, Calendar value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.LESS_THAN, value, temporalType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.LESS_EQUALS_THAN (property '&lt=' value)
     *
     * @param property
     * @param value
     * @return Current Restrictions with added restriction
     */
    public Restrictions lessEqualsThan(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.LESS_EQUALS_THAN, value));
        return this;
    }

    /**
     * 增加一个 Date RestrictionType.LESS_EQUALS_THAN (property '&lt=' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current Restrictions with added restriction
     */
    public Restrictions lessEqualsThan(String property, Date value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.LESS_EQUALS_THAN, value, temporalType));
        return this;
    }

    /**
     * 增加一个 Calendar RestrictionType.LESS_EQUALS_THAN (property '&lt=' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current Restrictions with added restriction
     */
    public Restrictions lessEqualsThan(String property, Calendar value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.LESS_EQUALS_THAN, value, temporalType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.IN (property 'in' value)
     *
     * @param property
     * @param value
     * @return Current Restrictions with added restriction
     */
    public Restrictions in(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.IN, value));
        return this;
    }

    /**
     * 增加一个 RestrictionType.NOT_IN (property 'not in' value)
     *
     * @param property
     * @param value
     * @return Current Restrictions with added restriction
     */
    public Restrictions notIn(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.NOT_IN, value));
        return this;
    }

    /**
     * 增加一个 RestrictionType.EQUALS (property '=' value)
     *
     * @param property
     * @param value
     * @return Current Restrictions with added restriction
     */
    public Restrictions equals(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.EQUALS, value));
        return this;
    }

    /**
     * 增加一个 Date RestrictionType.EQUALS (property '=' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current Restrictions with added restriction
     */
    public Restrictions equals(String property, Date value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.EQUALS, value, temporalType));
        return this;
    }

    /**
     *增加一个 Calendar RestrictionType.EQUALS (property '=' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current Restrictions with added restriction
     */
    public Restrictions equals(String property, Calendar value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.EQUALS, value, temporalType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.NOT_EQUALS (property '!=' value)
     *
     * @param property
     * @param value
     * @return Current Restrictions with added restriction
     */
    public Restrictions notEquals(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.NOT_EQUALS, value));
        return this;
    }

    /**
     * 增加一个 Date RestrictionType.NOT_EQUALS (property '!=' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current Restrictions with added restriction
     */
    public Restrictions notEquals(String property, Date value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.NOT_EQUALS, value, temporalType));
        return this;
    }

    /**
     *增加一个 Calendar RestrictionType.NOT_EQUALS (property '!=' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current Restrictions with added restriction
     */
    public Restrictions notEquals(String property, Calendar value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.NOT_EQUALS, value, temporalType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.OR
     *
     * @return Current Restrictions with added restriction
     */
    public Restrictions or() {
        this.add(new Restriction(RestrictionType.OR));
        return this;
    }

    /**
     *增加一个 RestrictionType.START_GROUP
     *
     * @return Current Restrictions with added restriction
     */
    public Restrictions startGroup() {
        this.add(new Restriction(RestrictionType.START_GROUP));
        return this;
    }

    /**
     * 增加一个 RestrictionType.END_GROUP
     *
     * @return Current Restrictions with added restriction
     */
    public Restrictions endGroup() {
        this.add(new Restriction(RestrictionType.END_GROUP));
        return this;
    }

    /**
     * @return 添加当前查询限制字符串
     */
    public String getQueryString() {
        return QueryBuilder.getQueryStringFromRestrictions(this);
    }

}
