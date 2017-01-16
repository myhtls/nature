package org.nature.platform.persistence.id;

import org.nature.platform.utils.Base62;

public class NatureIdGenerator {
	
	public final static int DEFAULT_ID_LENGTH = 15;
	
	public NatureIdGenerator(){
		
	}
	
	
	/**
	 * 生成一个  15 位 ID
	 * 
	 * @return
	 */
	public static String genDefaultId() {
		return new Base62().encodeBase10(0, DEFAULT_ID_LENGTH);
	}
	
	/**
	 * 生成一个 15 位 ID 与前缀
	 * 
	 * @param prefix
	 * @return
	 */
	public static String genDefaultId( String prefix ) {
		
		if( prefix == null ) throw new IllegalArgumentException("the PREFIX cannot be null");
		
		StringBuffer result = new StringBuffer( prefix );
		result.append( genDefaultId() );
		
		return result.toString();
	}
	
	/**
	 * 基于已有的ID生成下一个ID
	 * 
	 * @param baseId
	 * @return
	 */
	public static String genNextIdWithoutPrefix( String baseId ) {
		
		if( baseId == null ) throw new IllegalArgumentException("the BaseId cannot be null");
		
		Base62 base62 = new Base62();
		
		long intSerialNumber = base62.decodeBase62( baseId );
		
		return base62.encodeBase10( intSerialNumber + 1 , DEFAULT_ID_LENGTH);
	}
	
	/**
	 * 基于已有的ID前缀生成一个ID
	 * 
	 * @param baseId
	 * @return
	 */
	public static String genNextIdAppendPrefix( String baseId , String prefix ) {
		if( prefix == null ) throw new IllegalArgumentException("the PREFIX cannot be null");
		StringBuffer result = new StringBuffer( prefix );
		result.append( genNextIdWithoutPrefix( baseId ) );
		return result.toString();
	}
	
	/**
	 * 获得基于包含前缀已有的ID一张ID
	 * @param fullId
	 * @return
	 */
	public static String genNextIdWithPrefix( String fullId  ) {
		String tmpId = fullId;
		String prefix = getPrefix( fullId );
		String baseId = removePrefix( tmpId );
		return genNextIdAppendPrefix( baseId , prefix );
	}
	
	/**
	 *删除前缀
	 * 
	 * @param baseId
	 * @return
	 */
	private static String removePrefix( String fullId ){
		if( fullId == null || fullId.length() <= DEFAULT_ID_LENGTH ) 
			throw new IllegalArgumentException( String.format("the fullId cannot be null, and the length need greater than %s", DEFAULT_ID_LENGTH) );
		
		int baseId_length = fullId.length();
		
		int prefix_length = baseId_length > DEFAULT_ID_LENGTH ? (baseId_length - DEFAULT_ID_LENGTH) : 0;
		
		return fullId.substring(prefix_length);
	}
	
	/**
	 * 获得前缀名
	 * 
	 * @param baseId
	 * @return
	 */
	public static String getPrefix( String fullId  ){
		
		if( fullId == null || fullId.length() <= DEFAULT_ID_LENGTH ) 
			throw new IllegalArgumentException("the fullId cannot be null, and the length need greater than " + DEFAULT_ID_LENGTH);
		
		int baseId_length = fullId.length();
		
		int prefix_length = baseId_length > DEFAULT_ID_LENGTH ? (baseId_length - DEFAULT_ID_LENGTH) : 0;
		
		return fullId.substring( 0, prefix_length );
	}
	
}
