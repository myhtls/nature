package org.nature.platform.persistence.query.jpql;

/**
 * 模糊查询类型
 * @author hutianlong
 *
 */
public enum LikeType {
	
	/**
	 * 1.BEGIN 模糊查询开始进行模糊 '%xxx'
	 * 2.END 模糊查询结束进行模糊 'xxx%'
	 * 3.BOTH 模糊查询开始和结束都进行模糊 '%xxx%'
	 */
	BEGIN, 
	END, 
	BOTH;
	
	public static String getNameList() {
        StringBuilder builder = new StringBuilder();
        for (LikeType type : LikeType.values()) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(type.name().toLowerCase());
        }
        return builder.toString();
    }
}
