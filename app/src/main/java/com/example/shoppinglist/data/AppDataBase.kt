package com.example.shoppinglist.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//Базу данных (БД) помечяем @Database
//entities = [ShopItemDbModel::class] - В [] перечисляем все классы, которые являются таблицами нашей БД
//В нашем случае только один класс Note
//version = 1 - значение по умолчанию, но каждый раз увеличиваем на один при каких-либо изменениях
//Например, при удалении/добавлении столбцов в таблице БД, меняем значение на version = 2
@Database(entities = [ShopItemDbModel::class], version = 1)
//Делаем класс abstract и наследуемся от RoomDatabase
abstract class AppDatabase : RoomDatabase() {

    //Добавляем Dao в БД
    abstract fun shopListDao(): ShopListDao

    //Реализуем паттерн Синглтон
    companion object {
        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "shop_item.db"

        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }
    }
}