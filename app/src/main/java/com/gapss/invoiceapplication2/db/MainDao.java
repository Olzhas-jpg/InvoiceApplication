package com.gapss.invoiceapplication2.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;
@Dao
public interface MainDao {
    @Insert(onConflict =  REPLACE)
    void insert(MainData mainData);

    @Delete
    void delete(MainData mainData);

    @Query("UPDATE table_name SET text = :sText WHERE ID = :sID")
    void update(int sID, String sText);

    @Query("SELECT * FROM table_name")
    List<MainData> getAll();
}
