package com.novianto.challange6.util;

import org.springframework.stereotype.Component;

@Component
public class ConfigValidation {

    public static Integer  STATUS_CODE_NOT_FOUND =404;
    public static Integer STATUS_CODE_SUCCESS = 200;
    public static Integer STATUS_CODE_BAD_REQUEST = 400;
    public static Integer STATUS_CODE_INTERNAL_SERVER_ERROR = 500;
    public static String MERCHANT_REQUIRED = "Merchant is Required";
    public static String ID_USER_REQUIRED = "Id User is Required";
    public static String ID_MERCHANT_REQUIRED = "Id Merchant is Required";
    public static String ID_ORDER_REQUIRED = "Id Order is Required";
    public static String ID_PRODUCT_REQUIRED = "Id Product is Required";
    public static String ID_ORDER_DETAIL_REQUIRED = "Id Order Detail is Required";
    public static String USER_REQUIRED = "User is Required";
    public static String ORDER_REQUIRED = "Order is Required";
    public static String PRODUCT_REQUIRED = "Product is Required";
    public static String ID_USER_NOT_FOUND = "Id User Tidak Ditemukan";
    public static String ID_MERCHANT_NOT_FOUND = "Id Merchant Tidak Ditemukan";
    public static String ID_PRODUCT_NOT_FOUND = "Id Product Tidak Ditemukan";
    public static String ID_ORDER_NOT_FOUND = "Id Order Tidak Ditemukan";
    public static String ID_ORDER_DETAIL_NOT_FOUND = "Id Order Detail Tidak Ditemukan";
    public  static  String MERCHANT_NAME_NOT_VALID = "Merchant Name Not Valid.";
    public  static  String USERNAME_NOT_VALID = "Username Not Valid.";
    public  static  String EMAIL_NOT_VALID = "Email Address Not Valid.";
    public  static  String USER_DATA_INVALID = "User Data is Invalid.";
    public  static  String QUANTITY_NOT_VALID = "Quantity Data is Invalid.";
    public  static  String MERCHANT_DATA_INVALID = "Merchant Data is Invalid.";
    public  static  String PRODUCT_DATA_INVALID = "Product Data is Invalid.";
    public  static  String ORDER_DATA_INVALID = "Order Data is Invalid.";
    public  static  String ORDER_DETAIL_DATA_INVALID = "Order Detail Data is Invalid.";
    public  static  String PRODUCT_NAME_NOT_VALID = "Product Name Not Invalid.";
    public  static  String MERCHANT_ID_INVALID = "Id Merchant Not Invalid.";
    public  static  String USER_ID_INVALID = "Id User Not Invalid.";
    public  static  String PRICE_NOT_VALID = "Product Price Not Invalid.";
    public  static  String ORDER_TIME_NOT_VALID = "Order Time Not Invalid.";
    public  static  String ORDER_COMPLETED_NOT_VALID = "Order Completed Not Invalid.";
    public  static  String MERCHANT_NAME_EMPTY = "Merchant Name Must Have Value.";
    public  static  String MERCHANT_LOCATION_EMPTY = "Merchant Location Must Have Value.";
    public  static  String PRODUCT_NAME_EMPTY = "Product Name Must Have Value.";
    public  static  String PRICE_EMPTY = "Price Must Have Value.";
    public  static  String USERNAME_EMPTY = "Username Must Have Value.";
    public  static  String MERCHANT_ID_EMPTY = "Id Merchant Must Have Value.";
    public  static  String EMAIL_EMPTY = "Email Must Have Value.";
    public  static  String PASSWORD_EMPTY = "Password Must Have Value.";
    public  static  String ORDER_TIME_EMPTY = "Order Time Must Have Value.";
    public  static  String DESTINATION_ADDRESS_EMPTY = "Destination Order Must Have Value.";
    public  static  String EMAIL_ALREADY_EXISTS = "Email Is Already Exist.";
    public  static  String PRODUCT_NAME_ALREADY_EXISTS = "Product Name Already Exist.";
    public  static  String SUCCESS = "Success.";

}
