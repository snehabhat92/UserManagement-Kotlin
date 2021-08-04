package com.example.myapplication.Model

import android.text.TextUtils
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "User")
data class User(
    @PrimaryKey
    var id: Int,
    var name: String?,
    var username: String,
    var email: String,
    var phone: String,
    var website: String,
    var company: Company,
    var address: Address

) {

    var firstname: String = ""
        get() {
                initialise()

            return field
        }
    var lastname: String = ""
        get() {
            initialise()

            return field
        }

    override fun toString(): String {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userName='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", company=" + company +
                ", address=" + address +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}'
    }

   data class Company(var name: String) {

        override fun toString(): String {
            return "Company{" +
                    "name='" + name + '\'' +
                    '}'
        }

    }

    data class Address(var street : String, var suite: String, var city: String, var zipcode: String, var geo: Geo) {

        override fun toString(): String {
            return "Address{" +
                    "street='" + street + '\'' +
                    ", suite='" + suite + '\'' +
                    ", city='" + city + '\'' +
                    ", zipcode='" + zipcode + '\'' +
                    ", geo=" + geo +
                    '}'
        }
    }

   data class Geo(var lat : Double, var lng: Double) {
        override fun toString(): String {
            return "Geo{" +
                    "lat=" + lat +
                    ", lng=" + lng +
                    '}'
        }
    }

    class SortByName : Comparator<User?> {
        override fun compare(user1: User?, user2: User?): Int {
            return if (user1 != null && user2 != null && !TextUtils.isEmpty(user1.firstname) && !TextUtils.isEmpty(
                    user2.firstname
                )
            ) {
                user1.firstname.compareTo(user2.firstname)
            } else -1
        }
    }

    fun initialise() {
        if (name != null) {
            val nameArray = name?.split(" ".toRegex())?.toTypedArray()
            if (nameArray?.size!! >= 2) {
                this.lastname = nameArray[1]
            } else {
                this.lastname = ""
            }
            this.firstname = nameArray[0]
        }
    }
}