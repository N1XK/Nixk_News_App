package com.example.android.nixknewsapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.nixknewsapp.data.model.Article
import com.example.android.nixknewsapp.data.model.Converters
import com.example.android.nixknewsapp.data.model.ExploreNews
import com.example.android.nixknewsapp.data.model.RemoteKeys
import com.example.android.nixknewsapp.data.model.TopStory
import com.example.android.nixknewsapp.data.model.TrendingNews

@Database(
    entities = [
        Article::class,
        RemoteKeys::class,
        TopStory::class,
        TrendingNews::class,
        ExploreNews::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}