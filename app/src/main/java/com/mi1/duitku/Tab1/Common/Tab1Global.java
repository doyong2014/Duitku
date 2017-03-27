package com.mi1.duitku.Tab1.Common;

import com.mi1.duitku.Tab1.Common.DataModel;

import java.util.ArrayList;

/**
 * Created by owner on 3/8/2017.
 */

public class Tab1Global {

    public static ArrayList<DataModel.Post> _newsData = null;
    public static DataInfo _newsInfo = null;

    public static ArrayList<DataModel.Post> _promovData = null;
    public static DataInfo _promovInfo = null;

    public static ArrayList<DataModel.Post> _eventsData = null;
    public static DataInfo _eventsInfo = null;

    public static ArrayList<DataModel.Post> _searchData = null;

    public static void initData(){

        _newsData = new ArrayList<DataModel.Post>();
        _newsInfo = new DataInfo();

        _promovData = new ArrayList<DataModel.Post>();
        _promovInfo = new DataInfo();

        _eventsData = new ArrayList<DataModel.Post>();
        _eventsInfo = new DataInfo();

        _searchData = new ArrayList<DataModel.Post>();
    }

}
