package com.example.mvvm_news_app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mvvm_news_app.modules.Article

@TypeConverters(Converters::class)
@Database(entities = [Article::class], version = 1, exportSchema = false)
abstract class ArticleDatabase: RoomDatabase() {
    abstract fun getArticleDao(): ArticleDao

    companion object{
        @Volatile
        private var INSTANCE: ArticleDatabase? = null

        fun getInstance(context: Context): ArticleDatabase{
            return INSTANCE ?: synchronized(this){
                val instance =
                    Room.databaseBuilder(context, ArticleDatabase::class.java,"articles_database")
                        .build()
                INSTANCE = instance
                instance
            }
        }

    }
}