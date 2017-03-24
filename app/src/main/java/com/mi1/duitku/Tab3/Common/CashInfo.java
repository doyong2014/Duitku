package com.mi1.duitku.Tab3.Common;

/**
 * Created by owner on 3/7/2017.
 */

public class CashInfo {
    
    public TransactionList transactionList[];
    public String totalPageSize;

    public static class TransactionList {
        
        public String typeTransaction;
        public String reference;
        public String descript;
        public String token;
        public String amount;
        public String balance;
        public String date;
        public boolean dc;

    }
}