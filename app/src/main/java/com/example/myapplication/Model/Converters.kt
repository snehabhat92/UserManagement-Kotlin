package com.example.myapplication.Model

import android.text.TextUtils
import androidx.room.TypeConverter
import com.example.myapplication.Model.User.Company
import com.example.myapplication.Model.User.Geo
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromString(companyName: String?): Company? {
        return if (!TextUtils.isEmpty(companyName)) Company(companyName!!) else null
    }

    @TypeConverter
    fun fromCompany(company: Company?): String? {
        return company?.name ?: ""
    }

    @TypeConverter
    fun fromAddressString(companyName: String?): User.Address? {
        return Gson().fromJson(
            companyName,
            User.Address::class.java
        )
    }

    @TypeConverter
    fun fromAddress(address: User.Address?): String? {
        return Gson().toJson(address)
    }

    @TypeConverter
    fun fromGeoString(geo: String?): Geo? {
        return Gson().fromJson(geo, Geo::class.java)
    }

    @TypeConverter
    fun fromGeo(geo: Geo?): String? {
        return Gson().toJson(geo)
    }
}