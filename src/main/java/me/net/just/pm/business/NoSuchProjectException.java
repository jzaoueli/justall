package me.net.just.pm.business;


public class NoSuchProjectException extends Exception {

    public NoSuchProjectException(String msg) {
        super(msg);
    }

    public NoSuchProjectException() {
        super("project doesn't exist");
    }
}
