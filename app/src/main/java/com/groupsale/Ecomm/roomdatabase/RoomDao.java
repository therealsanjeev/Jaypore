package com.groupsale.Ecomm.roomdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Sanjeev on 08,June,2021
 * therealsanjeev0@gmail.com
 */

@Dao
public interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(RoomModel roomModel);

    @Update
    void update(RoomModel roomModel);

    @Delete
    void delete(RoomModel roomModel);
    //Delete all Genre from table
    @Query("DELETE FROM realtimeData")
    void deleteAllData();

    @Query("SELECT * FROM realtimeData WHERE creatorID = :customerID")
    Flowable<List<RoomModel>> myDeals(String customerID);

    @Query("SELECT * FROM realtimeData WHERE pinCode = :pincode"  )
    Flowable<List<RoomModel>> dealAroundYou(String pincode);


    @Query("SELECT DISTINCT creatorID from realtimeData")
    Flowable<List<String>> uniqueCreatorS();

    @Query("SELECT * FROM realtimeData")
    Flowable<List<RoomModel>> allData();
}
