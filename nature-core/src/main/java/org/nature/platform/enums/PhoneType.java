package org.nature.platform.enums;

/**
 * 电话类型
 * @author hutianlong
 *
 */
public enum PhoneType {

	PHONE(0,"手机"),LANDLINE(1,"座机");
	
	
	private PhoneType(int value, String msg) {
        this.value = value;
        this.message = msg;
    }
	

    private int value;
    private String message;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }	
	
}
