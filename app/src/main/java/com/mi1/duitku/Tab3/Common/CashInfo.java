package com.mi1.duitku.Tab3.Common;

/**
 * Created by owner on 3/7/2017.
 */

public class CashInfo {
    
    public TransactionList transactionList[];
    public String totalPageSize;

    public CashInfo(TransactionList[] transactionList, String totalPageSize){
        this.transactionList = transactionList;
        this.totalPageSize = totalPageSize;
    }

    public static class TransactionList {
        
        public String typeTransaction;
        public String reference;
        public String descript;
        public String token;
        public String amount;
        public String balance;
        public String date;
        public boolean dc;

        public TransactionList(String typeTransaction, String reference, String descript, String token, String amount, 
                               String balance, String date, boolean dc){
            this.typeTransaction = typeTransaction;
            this.reference = reference;
            this.descript = descript;
            this.token = token;
            this.amount = amount;
            this.balance = balance;
            this.date = date;
            this.dc = dc;
        }
    }
}