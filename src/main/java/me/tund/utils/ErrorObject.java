package me.tund.utils;

public class ErrorObject {

    private String message;
    private boolean success;

    public ErrorObject() {
        this.success = false;
        this.message = "Ein unbekannter Fehler ist aufgetreten.";
    }

    public ErrorObject(String message, boolean success) {
        this.message = message;
        this.success = success;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
