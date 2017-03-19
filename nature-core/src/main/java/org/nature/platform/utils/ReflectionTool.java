package org.nature.platform.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;




public class ReflectionTool {
	

	    /**
	     * 读取方法的可能前缀
	     */
	    private static final String[] getterPrefixes = {"get", "is"};

	    /**
	     * 获取对象的属性的类型。
	     *
	     * @param object 属性所属的对象不能为null.
	     * @param property 属性获取类型，可以嵌套. 
	     * @return 属性的类型。 如果该对象中不存在该属性，则返回null。
	     */
	    @SuppressWarnings("rawtypes")
		public static Class getPropertyType(Object object, String property) {
	        if (object == null) {
	            throw new IllegalArgumentException("Object cannot be null.");
	        }
	        return getPropertyType(object.getClass(), property);
	    }

	    /**
	     * 获取类的属性的类型。
	     *
	     * @param Class属性所属的类不能为null。
	     * @param 属性获取类型，可以嵌套。
	     * @return 属性的类型。 如果属性不存在于类中，则返回null。
	     */
	    @SuppressWarnings("rawtypes")
		public static Class getPropertyType(Class clazz, String property) {
	        if (clazz == null) {
	            throw new IllegalArgumentException("Clazz cannot be null.");
	        }

	        if (property == null) {
	            throw new IllegalArgumentException("Property cannot be null.");
	        }

	        int dotIndex = property.lastIndexOf('.');

	        if (dotIndex == -1) {
	            Method method = getReadMethod(clazz, property);
	            return method == null ? null : method.getReturnType();
	        }

	        String deepestProperty = property.substring(dotIndex + 1);
	        String parentProperty = property.substring(0, dotIndex);
	        return getPropertyType(getPropertyType(clazz, parentProperty), deepestProperty);
	    }

	    /**
	     * 获取类中属性的读取方法。
	     *
	     * for example:      <code>
	     * class Foo {
	     *      public String getBar() { return "bar"; }
	     *      public Boolean isBaz() { return false; }
	     * }
	     *
	     * BeanUtils.getReadMethod(Foo.class, "bar"); // return Foo#getBar()
	     * BeanUtils.getReadMethod(Foo.class, "baz"); // return Foo#isBaz()
	     * BeanUtils.getReadMethod(Foo.class, "baa"); // return null
	     * </code>
	     *
	     * @param clazz The class to get read method.
	     * @param property The property to get read method for, can NOT be nested.
	     * @return The read method (getter) for the property, if there is no read
	     * method for the property, returns null.
	     */
	    @SuppressWarnings("unchecked")
		public static Method getReadMethod(@SuppressWarnings("rawtypes") Class clazz, String property) {
	        // Capitalize the property 
	        StringBuilder buf = new StringBuilder();
	        buf.append(property.substring(0, 1).toUpperCase());
	        if (property.length() > 1) {
	            buf.append(property.substring(1));
	        }

	        Method method = null;
	        for (String prefix : getterPrefixes) {
	            String methodName = prefix + buf.toString();
	            try {
	                method = clazz.getMethod(methodName);

	                // Once get method successfully, jump out the loop. 
	                break;
	            } catch (NoSuchMethodException e) {
	                // do nothing
	            } catch (SecurityException e) {
	               
	            }
	        }

	        return method;
	    }

	    /**
	     * 从类或超类中获取声明的字段
	     *
	     * @param clazz
	     * @param fieldName
	     * @return
	     */
	    @SuppressWarnings("rawtypes")
		public static Field getDeclaredField(Class clazz, String fieldName) {
	        Field field = null;

	        try {
	            field = clazz.getDeclaredField(fieldName);
	        } catch (NoSuchFieldException ex) {
	            if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
	                return getDeclaredField(clazz.getSuperclass(), fieldName);
	            }
	        }

	        return field;
	    }

}
