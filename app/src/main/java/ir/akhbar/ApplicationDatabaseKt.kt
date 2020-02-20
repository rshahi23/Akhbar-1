package ir.akhbar

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [NewsTable::class])
abstract class ApplicationDatabaseKt : RoomDatabase() {

    abstract fun getNewsDao(): NewsDao
}