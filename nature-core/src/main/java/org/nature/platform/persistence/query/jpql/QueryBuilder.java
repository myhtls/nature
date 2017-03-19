package org.nature.platform.persistence.query.jpql;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.beanutils.PropertyUtils;
import org.nature.platform.persistence.exception.QueryFileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryBuilder {
	
	//排序条件
    private String orderBy;
    
    private String attributes;
    /**
     * to be used in SUM, MAX, MIN, COUNT
     */
    private String attribute;
    
  
    //实体对象或者子查询，用在select 里面
    private Class<?> from;
    
    //别名，也叫标识变量 如select d from dep d中的d
    private String alias;
    
    private final JoinBuilder joins = new JoinBuilder();
    private final List<Restriction> Restrictions = new ArrayList<Restriction>();
    private List<Restriction> normalizedRestrictions = new ArrayList<Restriction>();
    
    //设置对应的字段是求和还是其它方式
    private QueryType type;
    
    //管理单元jpa
    private final EntityManager entityManager;
    
    private Integer maxResults;
    private Integer firstResult;
    
    
    //是否要进行日志信息输出true输出，false不输出
    private boolean debug;
    
    private static final Logger logger = LoggerFactory.getLogger(QueryBuilder.class);
    /**
     * 
     * @param entityManager 管理单元jpa
     */
    public QueryBuilder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    
    /**
     * 定义查询的form对象实体
     * @param from 实体对象或者子查询，用在select 里面
     * @return 当前设置后的查询条件QueryBuilder
     */
    public QueryBuilder from(Class<?>  from) {
        this.from = from;
        return this;
    }

    
    /**
     * 定义查询的form对象实体
     * @param from 实体对象或者子查询，用在select 里面
     * @param alias 别名，也叫标识变量
     * @return 当前设置后的查询条件QueryBuilder
     */
    public QueryBuilder from(Class<?> from, String alias) {
        this.from = from;
        this.alias = alias;
        return this;
    }

    /**
     * 定义order by 排序条件
     * @param order 只可以设置一个条件或者多个条件：
     * 如： "field1, field2" and "asc" and "desc",defined "field1 desc,field2
     * @return 当前设置后的查询条件QueryBuilder
     */
    public QueryBuilder orderBy(String order) {
        this.orderBy = order;
        return this;
    }

  
    /**
     * 通过对List数组进行查询
     * 例子：
     * List<String> _order=new  ArrayList<String>();
		String a1="a.teste asc";
		String a2="a.name desc";
		_order.add(a1);
		_order.add(a2);
		
     * @param order List<String>条件组合
     * @return 当前设置后的查询条件QueryBuilder
     */
    public QueryBuilder orderBy(List<String> order) {
        if (order != null) {
            StringBuilder builder = new StringBuilder();
            for (String s : order) {
                if (builder.length() > 0) {
                    builder.append(",");
                }
                builder.append(s);
            }
            this.orderBy = builder.toString();
        }
        return this;
    }

    /**
     * 
     * 如果设置了dubug,则会输出生成的jpql的日志信息
     * @return 当前设置后的查询条件QueryBuilder
     */
    public QueryBuilder debug() {
        this.debug = true;
        return this;
    }

    /**
     * 如果设置了dubug,则会输出生成的jpql的日志信息
     *
     * @param debug true要输出，false不用输出
     * @return  当前设置后的查询条件QueryBuilder
     */
    public QueryBuilder debug(boolean debug) {
        this.debug = debug;
        return this;
    }

    /**
     * 返回基于QueryType的字符串。
     *
     * <ul>
     * <li>MAX returns a SELECT MAX(field)</li>
     * <li>MIN returns a SELECT MIN(field)</li>
     * <li>AVG returns a SELECT AVG(field)</li>
     * <li>COUNT returns a SELECT COUNT(field)/COUNT(*)</li>
     * <li>SELECT type returns "SELECT <code>select</code>" if
     * <code>select</code> is emptu then returns a empty String</li>
     * </ul>
     *
     * @param type
     * @param select
     * @return 
     */
    public static String getQuerySelectClausule(QueryType type, String select) {

        if (type == null) {
            return "";
        }

        StringBuilder queryString = new StringBuilder();

        if (type.equals(QueryType.COUNT)) {
            if (select != null && !select.trim().isEmpty()) {
                queryString.append("SELECT COUNT(").append(select).append(") ");
            } else {
                queryString.append("SELECT COUNT(*) ");
            }
        } else if (type.equals(QueryType.MAX)) {
            queryString.append("SELECT MAX(").append(select).append(") ");
        } else if (type.equals(QueryType.MIN)) {
            queryString.append("SELECT MIN(").append(select).append(") ");
        } else if (type.equals(QueryType.SUM)) {
            queryString.append("SELECT SUM(").append(select).append(") ");
        } else if (type.equals(QueryType.AVG)) {
            queryString.append("SELECT AVG(").append(select).append(") ");
        } else if (type.equals(QueryType.SELECT) && (select != null && !select.isEmpty())) {
            queryString.append("SELECT ").append(select).append(" ");
        }

        return queryString.toString();

    }

    /**
     * 
     */
    private void loadNormalizedRestrictions() {
        normalizedRestrictions = RestrictionsNormalizer.getNormalizedRestrictions(from, Restrictions, alias);
    }

    /**
     *
     * 返回从JPQL生成的“WHERE”之后的部分的字符串
     *
     * @param Restrictions
     * @return 
     */
    public static String getQueryStringFromRestrictions(List<Restriction> Restrictions) {

        int currentParameter = 1;
        StringBuilder queryString = new StringBuilder();
        boolean processPropertyAndValue = false;
        Restriction lastRestriction = null;

        for (Restriction Restriction : Restrictions) {

            if (lastRestriction != null) {
                if (lastRestriction.getRestrictionType().equals(RestrictionType.OR)) {
                    queryString.append(" OR ");
                    processPropertyAndValue = true;
                } else {
                    if (!lastRestriction.getRestrictionType().equals(RestrictionType.START_GROUP)
                            && !Restriction.getRestrictionType().equals(RestrictionType.OR)
                            && !Restriction.getRestrictionType().equals(RestrictionType.END_GROUP)) {
                        queryString.append(" AND ");
                    }
                    processPropertyAndValue = true;
                }
            }

            if (Restriction.getRestrictionType().equals(RestrictionType.START_GROUP)
                    || Restriction.getRestrictionType().equals(RestrictionType.END_GROUP)) {
                queryString.append(Restriction.getRestrictionType().getSymbol());
                processPropertyAndValue = false;
            } else if (Restriction.getRestrictionType().equals(RestrictionType.OR)) {
                processPropertyAndValue = false;
            } else {
                processPropertyAndValue = true;
            }

            lastRestriction = Restriction;

            if (processPropertyAndValue) {
                String propertyName = Restriction.getProperty();
                if (Restriction.getCastAs() != null && !Restriction.getCastAs().isEmpty()) {
                    propertyName = "CAST(" + propertyName + " AS " + Restriction.getCastAs() + ")";
                }
                //custom query string
                if (Restriction.getRestrictionType().equals(RestrictionType.QUERY_STRING)) {
                    queryString.append(" (").append(Restriction.getProperty()).append(") ");
                } else {
                    if (Restriction.getRestrictionType().equals(RestrictionType.LIKE) || Restriction.getRestrictionType().equals(RestrictionType.NOT_LIKE)) {
                        if (Restriction.isIlike()) {
                            queryString.append("UPPER(").append(propertyName).append(")").append(" ");
                        } else {
                            queryString.append(propertyName).append(" ");
                        }
                    } else if (Restriction.getTemporalType() != null && Restriction.getTemporalType().equals(TemporalType.DATE)) {
                        queryString.append("CAST(").append(propertyName).append(" AS date)").append(" ");
                    } else {
                        //member of has a own logic
                        if (!Restriction.getRestrictionType().equals(RestrictionType.MEMBER_OF) && !Restriction.getRestrictionType().equals(RestrictionType.NOT_MEMBER_OF)) {
                            queryString.append(propertyName);
                        }
                    }
                    //if Value is null set default to IS NULL
                    if (Restriction.getValue() == null || Restriction.getRestrictionType().equals(RestrictionType.NULL)
                            || Restriction.getRestrictionType().equals(RestrictionType.NOT_NULL)
                            || Restriction.getRestrictionType().equals(RestrictionType.EMPTY)
                            || Restriction.getRestrictionType().equals(RestrictionType.NOT_EMPTY)) {
                        //  EQUALS null or IS_NULL
                        if (Restriction.getRestrictionType().equals(RestrictionType.EQUALS)) {
                            queryString.append(" IS NULL ");
                        } else if (Restriction.getRestrictionType().equals(RestrictionType.NULL) || Restriction.getRestrictionType().equals(RestrictionType.EMPTY)) {
                            //if has value and value is false, so negate value 
                            //not NULL == NOT_NULL
                            queryString.append(" IS ");
                            if (Restriction.getValue() != null && Restriction.getValue() instanceof Boolean && (Boolean) Restriction.getValue() == false) {
                                queryString.append("NOT ");
                            }
                            queryString.append(Restriction.getRestrictionType().name());
                        } else if (Restriction.getRestrictionType().equals(RestrictionType.NOT_NULL) || Restriction.getRestrictionType().equals(RestrictionType.NOT_EMPTY)) {
                            //if has value and value is false, so negate value 
                            //not NOT_NULL == NULL
                            queryString.append(" IS ");
                            //negate
                            if (!(Restriction.getValue() != null && Restriction.getValue() instanceof Boolean && (Boolean) Restriction.getValue() == false)) {
                                queryString.append("NOT ");
                            }
                            //remove not (replacing enum name NOT_NULL = NULL)
                            queryString.append(Restriction.getRestrictionType().name().replace("NOT_", ""));
                        }
                    } else {
                        //member of has a own logic
                        if (Restriction.getRestrictionType().equals(RestrictionType.MEMBER_OF) || Restriction.getRestrictionType().equals(RestrictionType.NOT_MEMBER_OF)) {
                            // ?1 member of property
                            queryString.append("?").append(currentParameter);
                            queryString.append(" ").append(Restriction.getRestrictionType().getSymbol()).append(" ");
                            queryString.append(propertyName);
                        } else {
                            queryString.append(" ").append(Restriction.getRestrictionType().getSymbol()).append(" ");
                            if (Restriction.getRestrictionType().equals(RestrictionType.LIKE) || Restriction.getRestrictionType().equals(RestrictionType.NOT_LIKE)) {
                                if (Restriction.isIlike()) {
                                    queryString.append("UPPER(?").append(currentParameter).append(")");
                                } else {
                                    queryString.append("?").append(currentParameter);
                                }
                            } else if (Restriction.getRestrictionType().equals(RestrictionType.IN) || Restriction.getRestrictionType().equals(RestrictionType.NOT_IN)) {
                                queryString.append("(?").append(currentParameter).append(")");
                            } else {
                                queryString.append("?").append(currentParameter);
                            }
                        }
                        currentParameter++;
                    }
                }
            }
        }

        return queryString.toString();
    }

    /**
     * @return 在此QueryBuilder中生成的完整的JPQL字符串
     */
    public String getQueryString() {

        StringBuilder queryString = new StringBuilder();

        if (type == null) {
            type = QueryType.SELECT;
        }

        if (type.equals(QueryType.SELECT) || type.equals(QueryType.COUNT)) {
            queryString.append(QueryBuilder.getQuerySelectClausule(type, attributes));
        } else {
            queryString.append(QueryBuilder.getQuerySelectClausule(type, attribute));
        }

        queryString.append("FROM ").append(from.getName()).append(" ");

        if (alias != null) {
            queryString.append(alias).append(" ");
        }

        if (joins != null && joins.size() > 0) {
            queryString.append(joins.getJoinString(type)).append(" ");
        }

        //normalize
        loadNormalizedRestrictions();

        //where clausule
        if (normalizedRestrictions != null && !normalizedRestrictions.isEmpty()) {
            queryString.append("WHERE ");
        }

        //Restrictions
        queryString.append(QueryBuilder.getQueryStringFromRestrictions(normalizedRestrictions));

        //order by
        if (type.equals(QueryType.SELECT) && orderBy != null && !orderBy.trim().isEmpty()) {
            queryString.append(" ORDER BY ").append(orderBy).toString();
        }

        return queryString.toString();
    }

    public List<QueryParameter> getQueryParameters() {
        int position = 1;
        List<QueryParameter> parameters = new ArrayList<QueryParameter>();
        for (Restriction re : normalizedRestrictions) {
            //add custom parameters
            if (re.getParameters() != null) {
                parameters.addAll(re.getParameters());
            }
            if (re.getRestrictionType().isIgnoreParameter()) {
                continue;
            }
            if (re.getValue() != null) {
                QueryParameter parameter = null;
                if (re.getRestrictionType().equals(RestrictionType.LIKE)) {
                    if (re.getLikeType() == null || re.getLikeType().equals(LikeType.BOTH)) {
                        parameter = new QueryParameter(position, "%" + re.getValue() + "%");
                    } else if (re.getLikeType().equals(LikeType.BEGIN)) {
                        parameter = new QueryParameter(position, re.getValue() + "%");
                    } else if (re.getLikeType().equals(LikeType.END)) {
                        parameter = new QueryParameter(position, "%" + re.getValue());
                    }
                } else {
                    if (re.getTemporalType() != null && (re.getValue() instanceof Date || re.getValue() instanceof Calendar)) {
                        parameter = new QueryParameter(position, re.getValue(), re.getTemporalType());
                    } else {
                        parameter = new QueryParameter(position, re.getValue());
                    }
                }
                parameters.add(parameter);
                position++;
            }
        }
        return parameters;
    }

    /**
     * 生成对应的select 语句，如
     * queryBuilder.selectDistinct("b").getQueryString()
     * 返回:     * 
     * SELECT DISTINCT b FROM
     * @param attributes 属性名
     * @return
     */
    public QueryBuilder selectDistinct(String attributes) {
        this.attributes = "DISTINCT " + attributes;
        return this;
    }

    /**
     * 生成对应的select 语句，如
     * queryBuilder.select("name").getQueryString()
     * 返回:
     * SELECT name1 FROM 
     * 
     * 提示，多个select无效
     * @param attributes 属性名
     * @return
     */
    public QueryBuilder select(String attributes) {
        this.attributes = attributes;
        return this;
    }

    /**
     * @return 返回值 "SELECT COUNT(*)"
     */
    public Long count() {
        type = QueryType.COUNT;
        return (Long) createQuery().getSingleResult();
    }

    /**
     * @param attributes
     * @return 返回值 "SELECT COUNT(attributes)"
     */
    public Long count(String attributes) {
        type = QueryType.COUNT;
        this.attributes = attributes;
        return (Long) createQuery().getSingleResult();
    }

    /**
     * @param attributes
     * @return 返回值 "SELECT COUNT(attributes)"
     */
    public Long countDistinct(String attributes) {
        type = QueryType.COUNT;
        this.attributes = "DISTINCT " + attributes;
        return (Long) createQuery().getSingleResult();
    }

    /**
     *
     * @param 查询属性
     * @return 返回值 "SELECT AVG(property)"
     */
    public Number avg(String property) {
        type = QueryType.AVG;
        attribute = property;
        return (Number) createQuery().getSingleResult();
    }

    /**
     *
     * @param 查询属性
     * @param valueWhenNull 当查询是结果是null时，返回设定的默认值。
     * @return 返回值 "SELECT AVG(property)"
     */
    public Number avg(String property, Number valueWhenNull) {
        Number value = avg(property);
        if (value == null) {
            return valueWhenNull;
        }
        return value;
    }

    /**
     *
     * @param 查询属性
     * @return  结果值 "SELECT SUM(property)"
     */
    public Number sum(String property) {
        type = QueryType.SUM;
        attribute = property;
        return (Number) createQuery().getSingleResult();
    }

    /**
     *
     * @param property查询属性
     * @param valueWhenNull 当查询是结果是null时，返回设定的默认值。
     * @return  结果值 "SELECT SUM(property)"
     */
    public Number sum(String property, Number valueWhenNull) {
        Number number = sum(property);
        if (number == null) {
            return valueWhenNull;
        }
        return number;
    }

    /**
     * @param 查询属性
     * @return  结果值 "SELECT max(property)"
     */
    public Object max(String property) {
        type = QueryType.MAX;
        attribute = property;
        return createQuery().getSingleResult();
    }

    /**
     * @param 查询属性
     * @param valueWhenNull 当查询是结果是null时，返回设定的默认值。
     * @return  结果值 "SELECT max(property)"
     */
    public Object max(String property, Object valueWhenNull) {
        Object value = max(property);
        if (value == null) {
            return valueWhenNull;
        }
        return value;
    }

    /**
     *
     * @param 查询属性
     * @return  结果值 "SELECT min(property)"
     */
    public Object min(String property) {
        type = QueryType.MIN;
        attribute = property;
        return createQuery().getSingleResult();
    }

    /**
     * @param 查询属性
     * @param value WhenNull 当查询是结果是null时，返回设定的默认值。
     * @return  结果值 "SELECT min(property)"
     */
    public Object min(String property, Object valueWhenNull) {
        Object value = min(property);
        if (value == null) {
            return valueWhenNull;
        }
        return value;
    }

    /**
     * Creates the JPA Query Object based on current QueryBuilder
     *
     * @return
     */
    public Query createQuery() {

        String queryString = getQueryString();

        if (debug == true) {
            logger.info( "Query String: {0}", queryString);
            logger.info( "Parameters: Max Results: {0}, First result: {1}, Order By: {2}, Restrictions: {3}, Joins: {4}", new Object[]{maxResults, firstResult, orderBy, normalizedRestrictions, joins});
        }

        Query query = entityManager.createQuery(queryString);

        List<QueryParameter> parameters = getQueryParameters();
        for (QueryParameter parameter : parameters) {
            //dates (Date and Calendar)
            if (parameter.getTemporalType() != null && (parameter.getValue() instanceof Date || parameter.getValue() instanceof Calendar)) {
                if (parameter.getValue() instanceof Date) {
                    if (parameter.getPosition() != null) {
                        query.setParameter(parameter.getPosition(), (Date) parameter.getValue(), parameter.getTemporalType());
                    } else {
                        if (parameter.getProperty() != null) {
                            query.setParameter(parameter.getProperty(), (Date) parameter.getValue(), parameter.getTemporalType());
                        }
                    }
                } else if (parameter.getValue() instanceof Calendar) {
                    if (parameter.getPosition() != null) {
                        query.setParameter(parameter.getPosition(), (Calendar) parameter.getValue(), parameter.getTemporalType());
                    } else {
                        if (parameter.getProperty() != null) {
                            query.setParameter(parameter.getProperty(), (Calendar) parameter.getValue(), parameter.getTemporalType());
                        }
                    }
                }
            } else {
                if (parameter.getPosition() != null) {
                    query.setParameter(parameter.getPosition(), parameter.getValue());
                } else {
                    if (parameter.getProperty() != null) {
                        query.setParameter(parameter.getProperty(), parameter.getValue());
                    }
                }
            }
        }

        if (maxResults != null) {
            query.setMaxResults(maxResults);
        }
        if (firstResult != null) {
            query.setFirstResult(firstResult);
        }
        return query;
    }

    /**
     * @return 返回单个实体 entityManager.getSingleResult(), 返回 null 当
     * NoResultExceptionis 异常
     */
    public Object getSingleResult() {
        try {
            type = QueryType.SELECT;
            return this.createQuery().getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    /**
     * @param maxResults max results in Query
     * @return
     */
    public QueryBuilder setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    /**
     *
     * @param firstResult min results in Query
     * @return
     */
    public QueryBuilder setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    /**
     * @param <T> Result Type
     * @return entityManager.getResultList()
     */
    public <T> List<T> getResultList() {
        return getResultList(null);
    }

    /**
     *
     * @param <T> Result Type
     * @param expectedType The expected type in result
     * @return entityManager.getResultList()
     */
    public <T> List<T> getResultList(Class<T> expectedType) {
        type = QueryType.SELECT;
        @SuppressWarnings("unchecked")
		List<T> list = this.createQuery().getResultList();
        if (list != null && attributes != null && !attributes.trim().isEmpty() && expectedType != null) {
            return getNormalizedResultList(attributes, list, expectedType);
        }
        return list;
    }

    /**
     * @param <T> Result Type
     * @param attributes Attributes to select
     * @param resultList The Query Result List
     * @param clazz The expected type in result
     * @return
     */
    public static <T> List<T> getNormalizedResultList(String attributes, List<T> resultList, Class<T> clazz) {
        if (attributes != null && attributes.split(",").length > 0) {
            List<T> result = new ArrayList<>();
            String[] fields = attributes.split(",");
            for (Object object : resultList) {
                try {
                    T entity = clazz.newInstance();
                    for (int i = 0; i < fields.length; i++) {
                        String property = fields[i].trim().replaceAll("/s", "");
                        initializeCascade(property, entity);
                        if (object instanceof Object[]) {
                            PropertyUtils.setProperty(entity, property, ((Object[]) object)[i]);
                        } else {
                            PropertyUtils.setProperty(entity, property, object);
                        }
                    }
                    result.add(entity);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            return result;
        }
        return resultList;
    }

    public static void initializeCascade(String property, Object bean) {
        int index = property.indexOf(".");
        if (index > -1) {
            try {
                String field = property.substring(0, property.indexOf("."));
                Object propertyToInitialize = PropertyUtils.getProperty(bean, field);
                if (propertyToInitialize == null) {
                    propertyToInitialize = PropertyUtils.getPropertyDescriptor(bean, field).getPropertyType().newInstance();
                    PropertyUtils.setProperty(bean, field, propertyToInitialize);
                }
                String afterField = property.substring(index + 1, property.length());
                if (afterField != null && afterField.indexOf(".") > -1) {
                    initializeCascade(afterField, propertyToInitialize);
                }
            } catch (Exception ex) {
                logger.error( null, ex);
            }
        }
    }

    public static Query createNativeQueryFromFile(EntityManager entityManager, String queryPath, Class daoClass) throws QueryFileNotFoundException {
        return createNativeQueryFromFile(entityManager, queryPath, daoClass, null);
    }

    /**
     * 通过文件创建本地查询
     * @param entityManager
     * @param queryPath
     * @param daoClass
     * @param resultClass
     * @return
     * @throws QueryFileNotFoundException
     */
    public static Query createNativeQueryFromFile(EntityManager entityManager, String queryPath, Class daoClass, Class resultClass) throws QueryFileNotFoundException {

        InputStream inputStream = daoClass.getResourceAsStream(queryPath);
        if (inputStream == null) {
            throw new QueryFileNotFoundException("Query File not found: " + queryPath + " in package: " + daoClass.getPackage());
        }
        try {
            String queryString = readInputStreamAsString(inputStream);
            if (resultClass != null) {
                return entityManager.createNativeQuery(queryString, resultClass);
            } else {
                return entityManager.createNativeQuery(queryString);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static String readInputStreamAsString(InputStream inputStream)
            throws IOException {

        BufferedInputStream bis = new BufferedInputStream(inputStream);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while (result != -1) {
            byte b = (byte) result;
            buf.write(b);
            result = bis.read();
        }
        bis.close();
        buf.close();
        return buf.toString();
    }

    public List<Restriction> getNormalizedRestrictions() {
        return normalizedRestrictions;
    }

    /**
     * 增加一个 Restriction Map
     *
     * @param Restrictions
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder add(Map<String, Object> Restrictions) {
        if (Restrictions != null) {
            for (Iterator<Entry<String, Object>> iterator = Restrictions.entrySet().iterator(); iterator.hasNext();) {
				Entry<String, Object> e = iterator.next();
				this.Restrictions.add(new Restriction(e.getKey().toString(), e.getValue()));
			}
        }
        return this;
    }

    /**
     * 增加一个 Restriction list
     *
     * @param Restrictions
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder add(List<Restriction> Restrictions) {
        if (Restrictions != null) {
            this.Restrictions.addAll(Restrictions);
        }
        return this;
    }

    /**
     * 增加一个 Restriction
     *
     * @param Restriction
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder add(Restriction Restriction) {
        if (Restriction != null) {
            this.Restrictions.add(Restriction);
        }
        return this;
    }

    /**
     * 增加一个 Restriction
     *
     * @param property
     * @param RestrictionType
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder add(String property, RestrictionType RestrictionType) {
        this.add(new Restriction(property, RestrictionType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.EQUALS
     *
     * @param property
     * @param value
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder add(String property, Object value) {
        this.add(new Restriction(property, value));
        return this;
    }

    /**
     * 增加一个 Date Restriction
     *
     * @param property
     * @param RestrictionType
     * @param value
     * @param temporalType
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder add(String property, RestrictionType RestrictionType, Date value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType, value, temporalType));
        return this;
    }

    /**
     * 增加一个 Calendar Restriction
     *
     * @param property
     * @param RestrictionType
     * @param value
     * @param temporalType
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder add(String property, RestrictionType RestrictionType, Calendar value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType, value, temporalType));
        return this;
    }

    /**
     * 增加一个 Restriction
     *
     * @param property
     * @param RestrictionType
     * @param value
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder add(String property, RestrictionType RestrictionType, Object value) {
        this.add(new Restriction(property, RestrictionType, value));
        return this;
    }

    /**
     * 增加一个 Restriction
     *
     * @param property
     * @param RestrictionType
     * @param value
     * @param likeType
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder add(String property, RestrictionType RestrictionType, Object value, LikeType likeType) {
        this.add(new Restriction(property, RestrictionType, value, likeType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.QUERY_STRING
     *
     * @param property
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder addQueryString(String property) {
        this.add(new Restriction(property, RestrictionType.QUERY_STRING));
        return this;
    }

    /**
     * 增加一个 RestrictionType.QUERY_STRING
     *
     * @param property
     * @param parameters
     * @return Current Restrictions with added Restriction
     */
    public QueryBuilder addQueryString(String property, List<QueryParameter> parameters) {
        Restriction Restriction = new Restriction(property, RestrictionType.QUERY_STRING);
        Restriction.setParameters(parameters);
        this.add(Restriction);
        return this;
    }

    /**
     * 增加一个 RestrictionType.QUERY_STRING
     *
     * @param property
     * @param parameter
     * @return Current Restrictions with added Restriction
     */
    public QueryBuilder addQueryString(String property, QueryParameter parameter) {
        Restriction Restriction = new Restriction(property, RestrictionType.QUERY_STRING);
        List<QueryParameter> parameters = new ArrayList<QueryParameter>();
        parameters.add(parameter);
        Restriction.setParameters(parameters);
        this.add(Restriction);
        return this;
    }

    /**
     * 增加一个 RestrictionType.NULL (property 'is null')
     *
     * @param property
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder isNull(String property) {
        this.add(new Restriction(property, RestrictionType.NULL));
        return this;
    }

    /**
     * 增加一个 RestrictionType.NOT_NULL (property 'is not null')
     *
     * @param property
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder isNotNull(String property) {
        this.add(new Restriction(property, RestrictionType.NOT_NULL));
        return this;
    }
    /**
     * 增加一个 RestrictionType.EMPTY (property 'is empty')
     *
     * @param property
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder isEmpty(String property) {
        this.add(new Restriction(property, RestrictionType.EMPTY));
        return this;
    }

    /**
     * 增加一个 RestrictionType.NOT_EMPTY (property 'is not empty')
     *
     * @param property
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder isNotEmpty(String property) {
        this.add(new Restriction(property, RestrictionType.NOT_EMPTY));
        return this;
    }

    /**
     * 增加一个 RestrictionType.LIKE (property 'like' value)
     *
     * @param property
     * @param value
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder like(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.LIKE, value));
        return this;
    }

    /**
     * 增加一个 RestrictionType.MEMBER_OF (value 'member of' property)
     *
     * @param value
     * @param property
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder memberOf(Object value, String property) {
        this.add(new Restriction(property, RestrictionType.MEMBER_OF, value));
        return this;
    }

    /**
     * 增加一个 RestrictionType.NOT_MEMBER_OF (value 'member of' property)
     *
     * @param value
     * @param property
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder notMemberOf(Object value, String property) {
        this.add(new Restriction(property, RestrictionType.NOT_MEMBER_OF, value));
        return this;
    }

    /**
     * 增加一个 RestrictionType.LIKE (property 'like' value)
     *
     * @param property
     * @param value
     * @param ilike
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder like(String property, Object value, boolean ilike) {
        this.add(new Restriction(property, RestrictionType.LIKE, value, ilike));
        return this;
    }

    /**
     * 增加一个 RestrictionType.LIKE (property 'like' value)
     *
     * @param property
     * @param value
     * @param likeType
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder like(String property, Object value, LikeType likeType) {
        this.add(new Restriction(property, RestrictionType.LIKE, value, likeType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.LIKE (property 'like' value)
     *
     * @param property
     * @param value
     * @param likeType
     * @param ilike
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder like(String property, Object value, LikeType likeType, boolean ilike) {
        this.add(new Restriction(property, RestrictionType.LIKE, value, likeType, ilike));
        return this;
    }

    /**
     * 增加一个 RestrictionType.NOT_LIKE (property 'not like' value)
     *
     * @param property
     * @param value
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder notLike(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.NOT_LIKE, value));
        return this;
    }

    /**
     * 增加一个 RestrictionType.NOT_LIKE (property 'not like' value)
     *
     * @param property
     * @param value
     * @param ilike
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder notLike(String property, Object value, boolean ilike) {
        this.add(new Restriction(property, RestrictionType.NOT_LIKE, value, ilike));
        return this;
    }

    /**
     * 增加一个 RestrictionType.NOT_LIKE (property 'not like' value)
     *
     * @param property
     * @param value
     * @param likeType
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder notLike(String property, Object value, LikeType likeType) {
        this.add(new Restriction(property, RestrictionType.NOT_LIKE, value, likeType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.NOT_LIKE (property 'not like' value)
     *
     * @param property
     * @param value
     * @param likeType
     * @param ilike
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder notLike(String property, Object value, LikeType likeType, boolean ilike) {
        this.add(new Restriction(property, RestrictionType.NOT_LIKE, value, likeType, ilike));
        return this;
    }

    /**
     * 增加一个 RestrictionType.GREATER_THAN (property '>' value)
     *
     * @param property
     * @param value
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder greaterThan(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.GREATER_THAN, value));
        return this;
    }

    /**
     * 增加一个 Date RestrictionType.GREATER_THAN (property '>' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder greaterThan(String property, Date value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.GREATER_THAN, value, temporalType));
        return this;
    }

    /**
     * 增加一个 Calendar RestrictionType.GREATER_THAN (property '>' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder greaterThan(String property, Calendar value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.GREATER_THAN, value, temporalType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.GREATER_EQUALS_THAN (property '>=' value)
     *
     * @param property
     * @param value
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder greaterEqualsThan(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.GREATER_EQUALS_THAN, value));
        return this;
    }

    /**
     * 增加一个 Date RestrictionType.GREATER_EQUALS_THAN (property '>=' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder greaterEqualsThan(String property, Date value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.GREATER_EQUALS_THAN, value, temporalType));
        return this;
    }

    /**
     * 增加一个 Calendar RestrictionType.GREATER_EQUALS_THAN (property '>=' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder greaterEqualsThan(String property, Calendar value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.GREATER_EQUALS_THAN, value, temporalType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.LESS_THAN (property '&lt' value)
     *
     * @param property
     * @param value
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder lessThan(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.LESS_THAN, value));
        return this;
    }

    /**
     * 增加一个 Date RestrictionType.LESS_THAN (property '&lt' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder lessThan(String property, Date value, TemporalType temporalType) {
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
    public QueryBuilder lessThan(String property, Calendar value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.LESS_THAN, value, temporalType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.LESS_EQUALS_THAN (property '&lt=' value)
     *
     * @param property
     * @param value
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder lessEqualsThan(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.LESS_EQUALS_THAN, value));
        return this;
    }

    /**
     * 增加一个 Date RestrictionType.LESS_EQUALS_THAN (property '&lt=' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder lessEqualsThan(String property, Date value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.LESS_EQUALS_THAN, value, temporalType));
        return this;
    }

    /**
     * 增加一个 Calendar RestrictionType.LESS_EQUALS_THAN (property '&lt=' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder lessEqualsThan(String property, Calendar value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.LESS_EQUALS_THAN, value, temporalType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.IN (property 'in' value)
     *
     * @param property
     * @param value
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder in(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.IN, value));
        return this;
    }

    /**
     * 增加一个 RestrictionType.NOT_IN (property 'not in' value)
     *
     * @param property
     * @param value
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder notIn(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.NOT_IN, value));
        return this;
    }

    /**
     * 增加一个 RestrictionType.EQUALS (property '=' value)
     *
     * @param property
     * @param value
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder equals(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.EQUALS, value));
        return this;
    }

    /**
     * 增加一个 Date RestrictionType.EQUALS (property '=' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder equals(String property, Date value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.EQUALS, value, temporalType));
        return this;
    }

    /**
     * 增加一个 Calendar RestrictionType.EQUALS (property '=' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder equals(String property, Calendar value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.EQUALS, value, temporalType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.NOT_EQUALS (property '!=' value)
     *
     * @param property
     * @param value
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder notEquals(String property, Object value) {
        this.add(new Restriction(property, RestrictionType.NOT_EQUALS, value));
        return this;
    }

    /**
     * 增加一个 Date RestrictionType.NOT_EQUALS (property '!=' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder notEquals(String property, Date value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.NOT_EQUALS, value, temporalType));
        return this;
    }

    /**
     * 增加一个 Calendar RestrictionType.NOT_EQUALS (property '!=' value)
     *
     * @param property
     * @param value
     * @param temporalType
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder notEquals(String property, Calendar value, TemporalType temporalType) {
        this.add(new Restriction(property, RestrictionType.NOT_EQUALS, value, temporalType));
        return this;
    }

    /**
     * 增加一个 RestrictionType.OR
     *
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder or() {
        this.add(new Restriction(RestrictionType.OR));
        return this;
    }

    /**
     * 增加一个 RestrictionType.START_GROUP
     *
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder startGroup() {
        this.add(new Restriction(RestrictionType.START_GROUP));
        return this;
    }

    /**
     * 增加一个 RestrictionType.END_GROUP
     *
     * @return Current QueryBuilder with added Restriction
     */
    public QueryBuilder endGroup() {
        this.add(new Restriction(RestrictionType.END_GROUP));
        return this;
    }

    public QueryBuilder join(JoinBuilder joinBuilder) {
        if (joinBuilder != null) {
            joins.addAll(joinBuilder);
        }
        return this;
    }

    public QueryBuilder leftJoin(String join) {
        joins.leftJoin(join);
        return this;
    }

    public QueryBuilder leftJoin(String join, String alias) {
        joins.leftJoin(join, alias);
        return this;
    }

    public QueryBuilder leftJoinFetch(String join) {
        joins.leftJoinFetch(join);
        return this;
    }

    public QueryBuilder leftJoinFetch(String join, String alias) {
        joins.leftJoinFetch(join, alias);
        return this;
    }

    public QueryBuilder innerJoin(String join) {
        joins.innerJoin(join);
        return this;
    }

    public QueryBuilder innerJoin(String join, String alias) {
        joins.innerJoin(join, alias);
        return this;
    }

    public QueryBuilder innerJoinFetch(String join) {
        joins.innerJoinFetch(join);
        return this;
    }

    public QueryBuilder innerJoinFetch(String join, String alias) {
        joins.innerJoinFetch(join, alias);
        return this;
    }

    public QueryBuilder join(String join) {
        joins.join(join);
        return this;
    }

    public QueryBuilder join(String join, String alias) {
        joins.join(join, alias);
        return this;
    }

    public QueryBuilder joinFetch(String join) {
        joins.join(join);
        return this;
    }

    public QueryBuilder joinFetch(String join, String alias) {
        joins.joinFetch(join, alias);
        return this;
    }

    public QueryBuilder rightJoin(String join) {
        joins.rightJoin(join);
        return this;
    }

    public QueryBuilder rightJoin(String join, String alias) {
        joins.rightJoin(join, alias);
        return this;
    }

    public QueryBuilder rightJoinFetch(String join) {
        joins.rightJoinFetch(join);
        return this;
    }

    public QueryBuilder rightJoinFetch(String join, String alias) {
        joins.rightJoinFetch(join, alias);
        return this;
    }

}
