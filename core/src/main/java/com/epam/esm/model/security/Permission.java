package com.epam.esm.model.security;

public enum Permission {
    TAG_GET("tag:get"),
    TAG_CREATE("tag:create"),
    TAG_UPDATE("tag:update"),
    TAG_DELETE("tag:delete"),
    BEST_TAG_GET("besttag:get"),
    ORDER_GET("order:get"),
    ORDER_CREATE("order:create"),
    USER_GET("user:get"),
    USER_CREATE("user:create"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete"),
    CERTIFICATE_GET("certificate:get"),
    CERTIFICATE_CREATE("certificate:create"),
    CERTIFICATE_UPDATE("certificate:update"),
    CERTIFICATE_DELETE("certificate:delete"),
    LOGIN("login"), LOGOUT("logout"), SIGNUP("signup"), CHANGE_PASSWORD("password:update");

    private final String permission;

    Permission(String permission){
        this.permission = permission;
    }

    public String getPermission(){
        return permission;
    }
}
