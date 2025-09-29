package com.example.manizmagapp

import java.util.regex.Pattern

object Validator {

    fun validateInput(heading: String, amount: Double): Boolean
    {
        return !(amount<=0 || heading.isEmpty())
    }

    fun validateEmailExisting(email:String):Boolean
    {
        val existing: ArrayList<String> = arrayListOf("marija.ftn@gmail.com", "test123@gmail.com")
        return (email in existing)
    }

    fun validateEmailFormat(email: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
        val pattern = Pattern.compile(emailRegex)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
}