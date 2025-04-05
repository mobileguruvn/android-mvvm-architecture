package com.brian.android_mvvm_architecture.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brian.android_mvvm_architecture.data.local.database.dao.PhotoDao
import com.brian.android_mvvm_architecture.data.local.database.entity.PhotoEntity

@Database(
    entities = [PhotoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}