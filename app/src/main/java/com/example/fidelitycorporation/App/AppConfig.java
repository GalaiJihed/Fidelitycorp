package com.example.fidelitycorporation.App;

public class AppConfig {
    private static String ip="10.0.2.2:3000";
    public static String    URL_LOGIN = "http://"+ip+"/auth/login";
    public static String    URL_GET_MANAGER = "http://"+ip+"/user/managers/getwithphone";
    public static String    URL_GET_ALL_PRODUCT = "http://"+ip+"/product/all";
    public static String    URL_GET_CLIENT_BY_ID = "http://"+ip+"/user/clients/getwithid";
    public static String    URL_GET_PRODUCTS_BYSTORE = "http://"+ip+"/product/getByStoreId";
    public static String    URL_ADD_PRODUCT = "http://"+ip+"/product/new";
    public static String    URL_EDIT_PRODUCT = "http://"+ip+"/product/edit";
    public static String    URL_DELETE_PRODUCT = "http://"+ip+"/product/delete/";
    public static String    URL_ADD_NEW_Client = "http://"+ip+"/auth/newclientandroid";
    public static String    URL_GET_CURRENT_MANAGER = "http://"+ip+"/user/managers/me";
    public static String    URL_CHECK_LOGIN_COSTUMER = "http://"+ip+"/store/checkphone";
    public static String    URL_CONTACT_SEND_MESSAGE = "http://"+ip+"/store/contact";
    public static String    URL_UPLOAD_FILE_PRODUCT = "http://"+ip+"/upload";
    public static String    URL_CHECK_REQUEST_CODE = "http://"+ip+"/store/verifycode";
    public static String    URL_GET_ALL_STORECOSTUMERS = "http://"+ip+"/store/getClientsByStoreId";
    public static String    URL_CREATE_NEW_EVENT = "http://"+ip+"/store/createEvent";
    public static String    URL_GET_STORE_EVENTS = "http://"+ip+"/store/getEvents";
    public static String    URL_GET_STATS = "http://"+ip+"/store/stats";
    public static String    URL_GET_STORE_EVENT_DETAIL = "http://"+ip+"/store/getEvent";
    public static String    URL_EDIT_PROFILE = "http://"+ip+"/user/managers/edit";
    public static String    URL_GET_IMAGE= "http://"+ip+"/images/";
    public static String    URL_ADD_REDUCTION_PRODUCT = "http://"+ip+"/product/update";
    public static String    URL_BIRTHDATE_POINTS = "http://"+ip+"/store/birthdaypoints";
    public static String    URL_NEW_ORDER = "http://"+ip+"/order/new";
    public static final int STORAGE_PERMISSION_CODE = 123;
    public static  final  int CHOOSE_FILE_STAFF = 1;
}
