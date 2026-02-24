package com.StudentManageApp.StudentManagment.entity.Enum;

public enum AppRole {
    ROLE_USER,
    ROLE_ADMIN;

    public static AppRole makeByString(String s){
        if (s.equals(AppRole.ROLE_ADMIN.toString()))return AppRole.ROLE_ADMIN;
        else if (s.equals(AppRole.ROLE_USER.toString())) return AppRole.ROLE_USER;
        return null;
    }
}
