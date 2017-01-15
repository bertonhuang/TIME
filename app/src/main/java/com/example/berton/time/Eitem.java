package com.example.berton.time;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Created by Berton on 8/6/2016.
 */

public class Eitem extends SugarRecord{
    String name,desc,start,finish;
    public Eitem(){
        }
    public Eitem(String name, String desc, String start, String finish){
        this.name = name;
        this.desc = desc;
        this.start = start;
        this.finish = finish;

    }
}

