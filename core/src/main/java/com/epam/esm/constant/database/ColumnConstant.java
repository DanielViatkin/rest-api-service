package com.epam.esm.constant.database;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ColumnConstant {
    //TODO delete CERTIFICATE_ID & TAG_ID fields
    public static final String CERTIFICATE_ID= "gift_certificate_id";
    public static final String CERTIFICATE_NAME= "name";
    public static final String CERTIFICATE_DESCRIPTION= "description";
    public static final String CERTIFICATE_PRICE= "price";
    public static final String CERTIFICATE_DURATION= "duration";
    public static final String CERTIFICATE_CREATE_DATE= "create_date";
    public static final String CERTIFICATE_LAST_UPDATE_DATE= "last_update_date";

    public static final String TAG_ID = "tag_id";
    public static final String TAG_NAME = "name";

    public static final String USER_LOGIN = "login";
    public static final String USER_ID = "user_id";
    public static final String USER_PASSWORD = "password";
    public static final String USER_STATUS = "status";

    public static final String ORDER_ID = "order_id";
    public static final String ORDER_COST = "order_cost";
    public static final String ORDER_PURCHASE_DATE = "purchase_date";
    public static final String ORDER_USER_ID = "user_id";
    public static final String ORDER_GIFT_CERTIFICATE_ID = "gift_certificate_id";

    public static final String ROLE_ID = "role_id";
    public static final String ROLE_NAME = "name";

    public static final String ID = "id";

    public static final String CERTIFICATE_ENTITY_LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String CERTIFICATE_ENTITY_CREATE_DATE = "createDate";

    public static Set<String> getCertificateColumnAsSet(){
        Set<String> allAsSet = new HashSet<>();
        allAsSet.add(ID);
        allAsSet.add(CERTIFICATE_NAME);
        allAsSet.add(CERTIFICATE_DESCRIPTION);
        allAsSet.add(CERTIFICATE_PRICE);
        allAsSet.add(CERTIFICATE_DURATION);
        allAsSet.add(CERTIFICATE_ENTITY_CREATE_DATE);
        allAsSet.add(CERTIFICATE_ENTITY_LAST_UPDATE_DATE);
        return allAsSet;
    }

}
