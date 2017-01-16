package org.nature.platform.utils;

/**
 * A class to convert numbers to Base62. Default charset: 0..9a..zA..Z Alternate
 * character sets can be specified when constructing the object.
 * 
 * @author Andreas Holley
 */
public class Base62 {
	private String characters;

	/**
	 * Constructs a Base62 object with the default charset (0..9a..zA..Z).
	 */
	public Base62() {
		this("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}

	/**
	 * Constructs a Base62 object with a custom charset.
	 * 
	 * @param characters
	 *            the charset to use. Must be 62 characters.
	 * @throws <code>IllegalArgumentException<code> if the supplied charset is not 62 characters long.
	 */
	public Base62(String characters) {
		if (!(characters.length() == 62)) {
			throw new IllegalArgumentException("Invalid string length, must be 62.");
		}
		this.characters = characters;
	}

	/**
	 * Encodes a decimal value to a Base62 <code>String</code>.
	 * 
	 * @param b10
	 *            the decimal value to encode, must be nonnegative.
	 * @return the number encoded as a Base62 <code>String</code>.
	 */
	public String encodeBase10(long b10) {
		if (b10 < 0) {
			throw new IllegalArgumentException("b10 must be nonnegative");
		}
		String ret = "";
		while (b10 > 0) {
			ret = characters.charAt((int) (b10 % 62)) + ret;
			b10 /= 62;
		}
		ret = ret.length() == 0 ? "0" : ret;
		return ret;

	}
	
	
	/**
	 * 
	 * @param b10
	 * @param length left fill 0 if the length less than Specified number of digits
	 * @return
	 */
	public String encodeBase10(long b10 , int length) {
		
		String result = encodeBase10( b10 );
		
		int result_length = result.length();    
        StringBuilder temp0 = new StringBuilder();    
        for(int i = 0; i < length - result_length; i++){    
            temp0.append('0');    
        }    
        return temp0.toString() + result.toString();  
	}

	/**
	 * Decodes a Base62 <code>String</code> returning a <code>long</code>.
	 * 
	 * @param b62
	 *            the Base62 <code>String</code> to decode.
	 * @return the decoded number as a <code>long</code>.
	 * @throws IllegalArgumentException
	 *             if the given <code>String</code> contains characters not
	 *             specified in the constructor.
	 */
	public long decodeBase62(String b62) {
		for (char character : b62.toCharArray()) {
			if (!characters.contains(String.valueOf(character))) {
				throw new IllegalArgumentException("Invalid character(s) in string: " + character);
			}
		}
		long ret = 0;
		b62 = new StringBuffer(b62).reverse().toString();
		long count = 1;
		for (char character : b62.toCharArray()) {
			ret += characters.indexOf(character) * count;
			count *= 62;
		}
		return ret;
	}

	public static void main(String[] args) throws InterruptedException {
		Base62 base62 = new Base62();
		System.gc();
		
		System.out.println(base62.encodeBase10(10 , 15));
		System.out.println(base62.encodeBase10(0 , 15 ) );
		System.out.println(base62.decodeBase62("Z"));
		System.out.println(base62.decodeBase62("ZZ"));
		
		for (int i = 0; i < 100 ; i++) {
			System.out.println( base62.encodeBase10( i , 15) );
		}
	}
}
