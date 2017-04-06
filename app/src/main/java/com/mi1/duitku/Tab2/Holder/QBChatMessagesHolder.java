package com.mi1.duitku.Tab2.Holder;

import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by owner on 4/3/2017.
 */

public class QBChatMessagesHolder {

    private static QBChatMessagesHolder instance;
    private HashMap<String, ArrayList<QBChatMessage>> qbChatMessageHashMap;

    public static synchronized QBChatMessagesHolder getInstance() {

        synchronized (QBChatMessagesHolder.class) {
            if(instance == null) {
                instance = new QBChatMessagesHolder();
            }
        }
        return instance;
    }

    private QBChatMessagesHolder() {
        this.qbChatMessageHashMap = new HashMap<>();

    }

    public void putMessages(String dialogId, ArrayList<QBChatMessage> qbChatMessages) {
        this.qbChatMessageHashMap.put(dialogId, qbChatMessages);
    }

    public void putMessage(String dialogId, QBChatMessage qbChatMessage) {
        List<QBChatMessage> lstResult = (List)this.qbChatMessageHashMap.get(dialogId);
        lstResult.add(qbChatMessage);
        ArrayList<QBChatMessage> lstAdded = new ArrayList(lstResult.size());
        lstAdded.addAll(lstResult);
        putMessages(dialogId, lstAdded);
    }

    public ArrayList<QBChatMessage> getChatMessagesByDialogId(String dialogId) {
        return (ArrayList<QBChatMessage>) this.qbChatMessageHashMap.get(dialogId);
    }
}
