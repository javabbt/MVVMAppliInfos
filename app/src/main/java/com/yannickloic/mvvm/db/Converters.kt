package com.yannickloic.mvvm.db

import androidx.room.TypeConverter
import com.yannickloic.mvvm.models.Source

class Converters {

    //Convertir Source a String
    @TypeConverter
    fun fromSource(source: Source): String{
    return source.name
    }


    //Convertir string a Source
    @TypeConverter
    fun toSource(name: String): Source{
    return Source(name, name)
    }
}