package org.nature.platform.persistence.utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;

import org.hibernate.proxy.HibernateProxy;
import org.nature.platform.utils.StringTool;

/**
 * 实体类工具类
 * 
 * @author hutianlong
 *
 */
public class EntityUtils {

	@SuppressWarnings({ "rawtypes", "unused" })
	private static final Map<Class, String> ID_NAME_MAP = new HashMap<>();
	@SuppressWarnings("rawtypes")
	private static final Map<Class, AccessibleObject> ID_ACCESSIBLE_MAP = new HashMap<>();
	@SuppressWarnings("rawtypes")
	private static final Map<Class, Class> ID_TYPE_MAP = new HashMap<>();

	/**
	 * 转换 一个 string 到 一个映射 id类型.
	 * 
	 * Example:
	 * 
	 * <pre>
	 * <code>
	 * &#064;Id
	 * private Integer id;
	 * </code>
	 * </pre>
	 * 
	 * 使用字符串“10”返回一个整数值10
	 * 
	 * @param id
	 * @param entityClass
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object getIdFromString(String id, Class entityClass) {
		if (id == null || id.isEmpty()) {
			return null;
		}
		try {
			Class idType = EntityUtils.getIdType(entityClass);
			if (idType.equals(Long.class) || idType.equals(long.class)) {
				return Long.parseLong(StringTool.getOnlyIntegerNumbers(id));
			} else if (idType.equals(Integer.class) || idType.equals(int.class)) {
				return Integer.parseInt(StringTool.getOnlyIntegerNumbers(id));
			} else if (idType.equals(BigInteger.class)) {
				return new BigInteger(StringTool.getOnlyIntegerNumbers(id));
			} else if (idType.equals(Short.class) || idType.equals(short.class)) {
				return Short.parseShort(StringTool.getOnlyIntegerNumbers(id));
			} else if (idType.equals(BigDecimal.class)) {
				return new BigDecimal(StringTool.getOnlyIntegerNumbers(id));
			} else if (idType.equals(String.class)) {
				return id;
			} else if (idType.equals(UUID.class)) {
				return UUID.fromString(id);
			} else {
				throw new IllegalArgumentException(
						"Type " + idType.getName() + " from entity " + entityClass.getName() + " cannot be converted");
			}
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	/**
	 * 返回类型 @Id/@EmbeddedId 到 这个实体. Example: Integer.class, Long.class
	 *
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Class getIdType(Class clazz) {
		Class type = ID_TYPE_MAP.get(clazz);
		if (type != null) {
			return type;
		}
		AccessibleObject accessibleObject = getIdAccessibleObject(clazz);
		if (accessibleObject instanceof Field) {
			type = ((Field) accessibleObject).getType();
			ID_TYPE_MAP.put(clazz, type);
			return type;
		}

		if (accessibleObject instanceof Method) {
			type = ((Method) accessibleObject).getReturnType();
			ID_TYPE_MAP.put(clazz, type);
			return type;
		}
		return null;
	}

	/**
	 * 返回 field/method 关于 注解 @Id/@EmbeddedId 到一个 实体类. AccessibleObject 是超类 Field
	 * and Method.
	 *
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static AccessibleObject getIdAccessibleObject(Class clazz) {

		AccessibleObject accessibleObjectFromMap = ID_ACCESSIBLE_MAP.get(clazz);
		if (accessibleObjectFromMap != null) {
			return accessibleObjectFromMap;
		}

		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
				ID_ACCESSIBLE_MAP.put(clazz, field);
				return field;
			}
		}
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(Id.class) || method.isAnnotationPresent(EmbeddedId.class)) {
				ID_ACCESSIBLE_MAP.put(clazz, method);
				return method;
			}
		}
		if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
			return getIdAccessibleObject(clazz.getSuperclass());
		}
		return null;
	}

	/**
	 * 方法返回 id (field/method with annotation @Id or
	 *
	 * @EmbeddedId)
	 *
	 * @param object
	 * @return
	 */
	public static Object getId(Object object) {
		if (object == null) {
			return null;
		}
		return getId(object, object.getClass());
	}

	/**
	 * 如果返回true，表示当前不是空 @Id/ @EmbeddedId
	 *
	 * @param object
	 * @return
	 */
	public static boolean isPersisted(Object object) {
		return getId(object) != null;
	}

	/**
	 * 方法抬实体 id (field/method with annotation @Id or
	 *
	 * @EmbeddedId)
	 *
	 * @param object
	 * @param clazz
	 *            current class
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object getId(Object object, Class clazz) {

		try {
			if (object == null) {
				return null;
			}

			if (object instanceof HibernateProxy) {
				return ((HibernateProxy) object).getHibernateLazyInitializer().getIdentifier();
			}

			AccessibleObject accessibleObject = getIdAccessibleObject(clazz);
			if (accessibleObject instanceof Field) {
				Field field = (Field) accessibleObject;
				field.setAccessible(true);
				return field.get(object);
			}

			if (accessibleObject instanceof Method) {
				Method method = (Method) accessibleObject;
				return method.invoke(object);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
