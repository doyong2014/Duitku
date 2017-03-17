package com.mi1.duitku.Tab1.Common;

import com.mi1.duitku.Tab1.Common.DataModel;

import java.util.ArrayList;

/**
 * Created by owner on 3/8/2017.
 */

public class GlobalData {

    public static ArrayList<DataModel.Post> _newsData = null;
    public static datasInfo _newsInfo = null;

    public static ArrayList<DataModel.Post> _promovData = null;
    public static datasInfo _promovInfo = null;

    public static ArrayList<DataModel.Post> _eventsData = null;
    public static datasInfo _eventsInfo = null;

    public GlobalData(){

        _newsData = new ArrayList<DataModel.Post>();
        _newsInfo = new datasInfo();

        _promovData = new ArrayList<DataModel.Post>();
        _promovInfo = new datasInfo();

        _eventsData = new ArrayList<DataModel.Post>();
        _eventsInfo = new datasInfo();

    }

    class datasInfo {
        public int pagesNum;
        public int curPage;
        public int itemsNum;
        public int refreshItemNum = 5;
    }
}
