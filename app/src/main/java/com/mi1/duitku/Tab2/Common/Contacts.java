package com.mi1.duitku.Tab2.Common;

import java.util.ArrayList;

/**
 * Created by owner on 4/21/2017.
 */

public class Contacts {

    private static Contacts _instance;
    public ArrayList<String> listContact;

    public static Contacts getIntstace() {
        if (_instance == null) {
            _instance = new Contacts();
        }

        return _instance;
    }

    public Contacts() {
        listContact = new ArrayList<String>();
    }
}
