package com.example.myapplication.Model

import android.os.Parcel
import android.os.Parcelable

class UserParcel : Parcelable {
    private var id: Int
    var name: String?
    var firstName: String?
    var lastName: String?
    var userName: String?
    var company: String?
    var phone: String?
    var email: String?
    var website: String?
    var address: String?
    var lat: Double
    var lng: Double

    constructor(
        id: Int,
        name: String?,
        firstName: String?,
        lastName: String?,
        userName: String?,
        phone: String?,
        email: String?,
        website: String?,
        company: String?,
        address: String?,
        lat: Double,
        lng: Double
    ) {
        this.id = id
        this.name = name
        this.firstName = firstName
        this.lastName = lastName
        this.userName = userName
        this.phone = phone
        this.email = email
        this.website = website
        this.company = company
        this.address = address
        this.lat = lat
        this.lng = lng
    }

    constructor(`in`: Parcel) {
        id = `in`.readInt()
        name = `in`.readString()
        firstName = `in`.readString()
        lastName = `in`.readString()
        userName = `in`.readString()
        phone = `in`.readString()
        email = `in`.readString()
        website = `in`.readString()
        company = `in`.readString()
        address = `in`.readString()
        lat = `in`.readDouble()
        lng = `in`.readDouble()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeString(firstName)
        dest.writeString(lastName)
        dest.writeString(userName)
        dest.writeString(phone)
        dest.writeString(email)
        dest.writeString(website)
        dest.writeString(company)
        dest.writeString(address)
        dest.writeDouble(lat)
        dest.writeDouble(lng)
    }

    companion object {
        val CREATOR: Parcelable.Creator<*> = object : Parcelable.Creator<Any?> {
            override fun createFromParcel(`in`: Parcel): UserParcel? {
                return UserParcel(`in`)
            }

            override fun newArray(size: Int): Array<UserParcel?> {
                return arrayOfNulls(size)
            }
        }
    }
}