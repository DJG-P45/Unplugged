package com.example.unplugged.data.datasource;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.unplugged.data.entity.ObservedAreaEntity;

import java.util.List;

@Dao
public interface ObservedAreaDao {

        @Insert // If you use the provided convenience annotations, Room takes care of thread management for you.
        void insert(ObservedAreaEntity observedArea);

        @Query("DELETE FROM observed_area") // Not a convenience annotation, need to provide own thread management.
        void deleteAll();

        @Query("DELETE FROM observed_area WHERE id = :id") // Not a convenience annotation, need to provide own thread management.
        void delete(String id);

        @Query("SELECT * from observed_area") // Not a convenience annotation, need to provide own thread management.
        LiveData<List<ObservedAreaEntity>> getAllObservedAreas(); // The use of LiveData pushes this onto it's own thread.
}
