package com.example.android.nixknewsapp.data.model

import androidx.room.TypeConverter

class Converters {

    //Convert List to String and get first position only
    @TypeConverter
    fun fromCategories(categories: List<String>?): String {
        return categories?.joinToString("#") ?: ""
    }

    //Convert String to List and remove "general" category
    @TypeConverter
    fun toCategories(category: String): List<String> {
        return category.split("#")
//        if (categories.size != 1 && categories.contains("general")) {
//            categories.remove("general")
//        }
    }
}