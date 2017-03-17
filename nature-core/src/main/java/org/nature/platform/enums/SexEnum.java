package org.nature.platform.enums;

public enum SexEnum {
	
	MAN(1,"男"),
	WOMAN(2,"女人"),
	OTHER(3,"其他");
	
	private SexEnum(int value, String msg) {
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
