package org.nature.platform.enums;

/**
 * 状态码
 * @author hutianlong
 *
 */
public enum StatusCode {
	
	SUCCESS(0, "SUCCESS"),
    FAILED(1, "FAIL");

    private StatusCode(int value, String msg) {
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
