package com.example.amit.remind_it.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.amit.remind_it.model.ItemModel;

import java.util.List;

/**
 * Created by meeera on 14/6/18.
 */

@Dao
public interface DaoAccess {
    @Insert
    void insertOnlySingleRecord(ItemModel itemModel);

    @Query("SELECT * FROM ItemModel")
    List<ItemModel> fetchAllData();

    @Query("DELETE FROM ItemModel WHERE id = :id")
    void deleteOnlySingleItem(long id);
}
