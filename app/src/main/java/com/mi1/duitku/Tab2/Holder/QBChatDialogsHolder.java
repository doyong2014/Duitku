package com.mi1.duitku.Tab2.Holder;

import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by owner on 4/5/2017.
 */

public class QBChatDialogsHolder {

    private static QBChatDialogsHolder instance;
    private HashMap<String, QBChatDialog> qbChatDialogHashMap;

    public static synchronized QBChatDialogsHolder getInstance() {

        synchronized (QBChatDialogsHolder.class) {
            if (instance == null) {
                instance = new QBChatDialogsHolder();
            }
        }
        return instance;
    }

    public QBChatDialogsHolder() {
        this.qbChatDialogHashMap = new HashMap<>();
    }

    public void putDialogs(ArrayList<QBChatDialog> dialogs) {
        for (QBChatDialog qbChatDialog:dialogs){
            putDialog(qbChatDialog);
        }
    }

    public void putDialog(QBChatDialog qbChatDialog) {
        this.qbChatDialogHashMap.put(qbChatDialog.getDialogId(), qbChatDialog);
    }

    public QBChatDialog getChatDialogById(String dialogId) {
        return (QBChatDialog)qbChatDialogHashMap.get(dialogId);
    }

    public ArrayList<QBChatDialog> getChatDialogsByIds(ArrayList<String> dialogIds) {
        ArrayList<QBChatDialog> chatDialogs = new ArrayList<>();
        for (String id : dialogIds) {
            QBChatDialog chatDialog = getChatDialogById(id);
            if (chatDialog != null) {
                chatDialogs.add(chatDialog);
            }
        }
        return chatDialogs;
    }

    public ArrayList<QBChatDialog> getAllChatDialogs() {
        ArrayList<QBChatDialog> chatDialogs = new ArrayList<>();
        for (String key:qbChatDialogHashMap.keySet()) {
            chatDialogs.add(qbChatDialogHashMap.get(key));
        }
        return chatDialogs;
    }

}
