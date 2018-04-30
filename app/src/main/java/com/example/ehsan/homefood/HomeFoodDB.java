package com.example.ehsan.homefood;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ehsan on 29-04-2018.
 */
@Database(entities = {Dish.class},version = 1)
public abstract class HomeFoodDB extends RoomDatabase {
        private static HomeFoodDB INSTANCE;

        public abstract DishDao dishDao();

        public static HomeFoodDB getAppDatabase(Context context) {
            if (INSTANCE == null) {
                INSTANCE =
                        Room.databaseBuilder(getApplicationContext(), HomeFoodDB.class, "homefood-database").allowMainThreadQueries().build();
            }
            return INSTANCE;
        }
        public static void destroyInstance() {
            INSTANCE = null;
        }

}
