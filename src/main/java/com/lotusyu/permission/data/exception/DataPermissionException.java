//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lotusyu.permission.data.exception;

public class DataPermissionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DataPermissionException(String message) {
        super(message);
    }

    public DataPermissionException(Throwable throwable) {
        super(throwable);
    }

    public DataPermissionException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public static DataPermissionException exception(String msg, Throwable t, Object... params) {
        return new DataPermissionException(String.format(msg, params), t);
    }

    public static DataPermissionException exception(String msg, Object... params) {
        return new DataPermissionException(String.format(msg, params));
    }

}
