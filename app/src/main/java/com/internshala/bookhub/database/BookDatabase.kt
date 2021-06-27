package com.internshala.bookhub.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BookEntitiy::class],version = 1)
abstract class BookDatabase:RoomDatabase() {
    abstract fun bookDao():BookDao
}