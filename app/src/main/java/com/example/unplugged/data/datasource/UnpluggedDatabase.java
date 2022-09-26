package com.example.unplugged.data.datasource;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.unplugged.data.entity.ObservedAreaEntity;

@Database(entities = {ObservedAreaEntity.class}, version = 1, exportSchema = false)
public abstract class UnpluggedDatabase extends RoomDatabase {

    private static UnpluggedDatabase INSTANCE;

    public static UnpluggedDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UnpluggedDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            UnpluggedDatabase.class, "unplugged_database"
                    ).fallbackToDestructiveMigration().addCallback(sRoomDatabaseCallback).build();
                    // Wipes and rebuilds instead of migrating
                    // if no Migration object.
                    // Migration is not part of this practical.
                }
            }
        }
        return INSTANCE;
    }

    public abstract ObservedAreaDao observedAreaDao();

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    db.execSQL("DELETE FROM observed_area");
                }
            };
}
