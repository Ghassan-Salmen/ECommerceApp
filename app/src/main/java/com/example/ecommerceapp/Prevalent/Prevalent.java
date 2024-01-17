package com.example.ecommerceapp.Prevalent;

import com.example.ecommerceapp.Model.Users;


//the use of prevalent class is to store and access shared variables or constants in an application
// to make it easy everything is public
public class Prevalent {

    public static Users currentOnlineUser;

    public static final  String UserPhoneKey = "UserPhone";
    public static final  String UserPasswordKey = "UserPassword";
}
