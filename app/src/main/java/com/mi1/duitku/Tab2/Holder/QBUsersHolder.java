package com.mi1.duitku.Tab2.Holder;

import android.util.SparseArray;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by owner on 3/31/2017.
 */

public class QBUsersHolder {

    private static QBUsersHolder instance;
    private SparseArray<QBUser> qbUserSparseArray;

    public static synchronized QBUsersHolder getInstance() {
        synchronized (QBUsersHolder.class) {
            if (instance == null) {
                instance = new QBUsersHolder();
            }
        }
        return instance;
    }

    public QBUsersHolder() {
        qbUserSparseArray = new SparseArray<>();
    }

    public void putUsers(ArrayList<QBUser> users){
        for(QBUser user:users){
            putUser(user);
        }
    }

    private void putUser(QBUser user) {
        qbUserSparseArray.put(user.getId(), user);
    }

    public QBUser getUserById(int id){
        return qbUserSparseArray.get(id);
    }

    public ArrayList<QBUser> getUsersByIds(ArrayList<Integer> ids){
        ArrayList<QBUser> qbUser = new ArrayList<>();
        for(Integer id:ids){
            QBUser user = getUserById(id);
            if(user != null) {
                qbUser.add(user);
            }
        }

        return qbUser;
    }

    public ArrayList<QBUser> getAllUsers(){
        ArrayList<QBUser> qbUsers = new ArrayList<>();
        for(int i = 0; i < qbUserSparseArray.size(); i++) {
            int key = qbUserSparseArray.keyAt(i);
            qbUsers.add(qbUserSparseArray.get(key));
        }

        return qbUsers;
    }
}
