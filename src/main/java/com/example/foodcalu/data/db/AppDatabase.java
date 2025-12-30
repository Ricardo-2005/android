package com.example.foodcalu.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.foodcalu.data.dao.DietRecordDao;
import com.example.foodcalu.data.dao.UserAccountDao;
import com.example.foodcalu.data.dao.UserProfileDao;
import com.example.foodcalu.data.dao.WorkoutCategoryDao;
import com.example.foodcalu.data.dao.WorkoutItemDao;
import com.example.foodcalu.data.dao.WorkoutLogDao;
import com.example.foodcalu.data.entity.DietRecord;
import com.example.foodcalu.data.entity.UserAccount;
import com.example.foodcalu.data.entity.UserProfile;
import com.example.foodcalu.data.entity.WorkoutCategory;
import com.example.foodcalu.data.entity.WorkoutItem;
import com.example.foodcalu.data.entity.WorkoutLog;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {UserProfile.class, UserAccount.class, WorkoutCategory.class, WorkoutItem.class, WorkoutLog.class, DietRecord.class},
        version = 5,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "foodcalu.db";
    private static volatile AppDatabase INSTANCE;
    public static final ExecutorService DB_EXECUTOR = Executors.newSingleThreadExecutor();

    public abstract UserProfileDao userProfileDao();

    public abstract UserAccountDao userAccountDao();

    public abstract DietRecordDao dietRecordDao();

    public abstract WorkoutCategoryDao workoutCategoryDao();

    public abstract WorkoutItemDao workoutItemDao();

    public abstract WorkoutLogDao workoutLogDao();

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `user_account` (`userId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `username` TEXT, `password` TEXT, `createTime` INTEGER NOT NULL)");
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_user_account_username` ON `user_account` (`username`)");
            database.execSQL("ALTER TABLE `meal_record` ADD COLUMN `unit` TEXT NOT NULL DEFAULT '克'");
            database.execSQL("ALTER TABLE `meal_record` ADD COLUMN `estimatedWeight` REAL NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE `meal_record` ADD COLUMN `estimatedCalories` REAL NOT NULL DEFAULT 0");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS `food`");
            database.execSQL("DROP TABLE IF EXISTS `meal_record`");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `diet_record` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`date` INTEGER NOT NULL, " +
                    "`raw_input` TEXT NOT NULL, " +
                    "`food_name` TEXT NOT NULL, " +
                    "`estimated_weight_g` REAL NOT NULL, " +
                    "`estimated_calories` REAL NOT NULL)");
        }
    };

    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `user_profile` ADD COLUMN `userId` INTEGER NOT NULL DEFAULT 0");
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_user_profile_userId` ON `user_profile` (`userId`)");
        }
    };

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(androidx.sqlite.db.SupportSQLiteDatabase db) {
                                    DB_EXECUTOR.execute(() -> DataSeeder.seed(INSTANCE));
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
