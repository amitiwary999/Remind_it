package com.example.amit.remind_it.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.amit.remind_it.model.ItemModel;

/**
 * Created by meeera on 14/6/18.
 */

@Database(entities = {ItemModel.class}, version = 1)
public abstract class SampleDataBase extends RoomDatabase{
    public abstract DaoAccess daoAccess();
}
