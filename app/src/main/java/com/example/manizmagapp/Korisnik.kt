package com.example.manizmagapp

import android.os.Parcel
import android.os.Parcelable

data class Korisnik(var korisnikId: String, val korisnickoIme: String, val imePrezime: String, val email: String, var password: String) : Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(korisnikId)
        parcel.writeString(korisnickoIme)
        parcel.writeString(imePrezime)
        parcel.writeString(email)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Korisnik> {
        override fun createFromParcel(parcel: Parcel): Korisnik {
            return Korisnik(parcel)
        }

        override fun newArray(size: Int): Array<Korisnik?> {
            return arrayOfNulls(size)
        }
    }

}