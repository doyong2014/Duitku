package com.mi1.duitku.Tab2.Common;

import com.quickblox.users.model.QBUser;

/**
 * Created by owner on 4/21/2017.
 */

public class AvailableQBUser {
    public QBUser qbUser;
    public boolean isSelected;

    public AvailableQBUser(QBUser qbUser, boolean isSelected) {
        this.qbUser = qbUser;
        this.isSelected = isSelected;
    }
}
