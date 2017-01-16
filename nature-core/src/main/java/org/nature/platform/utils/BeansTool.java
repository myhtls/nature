package org.nature.platform.utils;

import java.util.Collection;

/**
 * beans工具
 * @author hutianlong
 *
 */
public class BeansTool {
	
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object value) {

        if (value == null) {
            return true;
        }

        //验证string是否为空
        if (value instanceof String && ((String) value).isEmpty()) {
            return true;
        }
        //验证数组length
        if (value instanceof Object[] && ((Object[]) value).length == 0) {
            return true;
        }
        //验证集合是否为空
        if (value instanceof Collection && ((Collection) value).isEmpty()) {
            return true;
        }

        return false;
    }

}
