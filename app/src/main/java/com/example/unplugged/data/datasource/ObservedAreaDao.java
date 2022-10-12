package com.example.unplugged.data.datasource;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.unplugged.data.entity.ObservedAreaEntity;

import java.util.List;

/**
 * The "Query" annotation is not a convenience annotation, need to provide own thread management.
 * The "Insert" annotation is a convenience annotation, Room takes care of thread management for you.
 */

@Dao
public interface ObservedAreaDao {

        @Insert
        Long insert(ObservedAreaEntity observedArea);

        @Query("DELETE FROM observed_area")
        void deleteAll();

        @Query("DELETE FROM observed_area WHERE id = :id")
        void delete(String id);

        @Query("SELECT * FROM observed_area")
        List<ObservedAreaEntity> getAllObservedAreas();

        @Query("SELECT EXISTS(SELECT * FROM observed_area WHERE id = :id)")
        Boolean observedAreaExists(String id);

}
