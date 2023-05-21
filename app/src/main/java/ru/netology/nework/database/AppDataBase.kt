package ru.netology.nework.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.netology.nework.dao.PostDao
import ru.netology.nework.entity.PostEntity

@Database(entities = [PostEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun postDao(): PostDao
}