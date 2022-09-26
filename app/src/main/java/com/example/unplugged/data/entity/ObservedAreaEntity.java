package com.example.unplugged.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "observed_area")
public class ObservedAreaEntity {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    public ObservedAreaEntity(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getId() {
        return id;
    }
}
