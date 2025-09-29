package com.example.manizmagapp

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ValidatorTest{

    @Test
    fun trueWhenInputIsValid(){
        val amount = 150.0
        val heading = "Namirnice"
        val result = Validator.validateInput(heading, amount)

        assertTrue(result)
    }

    @Test
    fun falseWhenInputIsInvalid() {
        val amount = 0.0
        val heading = "Namirnice"
        val result = Validator.validateInput(heading, amount)

        assertFalse(result)
    }

    @Test
    fun trueWhenEmailDoesntExist(){
        val email = "newmail@gmail.com"
        val result = Validator.validateEmailExisting(email)

        assertFalse(result)
    }

    @Test
    fun falseWhenEmailExists(){
        val email = "marija.ftn@gmail.com"
        val result = Validator.validateEmailExisting(email)

        assertTrue(result)
    }

    @Test
    fun trueWhenEmailValidFormat() {
        val validEmails = listOf(
            "user@example.com",
            "user123@example.com",
            "user.name@example.com",
            "user123@example.co.in",
            "user_name@example.co.in",
            "user@subdomain.example.com",
            "user123@subdomain.example.com"
        )

        validEmails.forEach { email ->
            assertTrue("Email $email should be valid", Validator.validateEmailFormat(email))
        }
    }
    @Test
    fun falseWhenEmailInvalidFormat() {
        val invalidEmails = listOf(
            "user",
            ".com",
            "user.",
            "example.com",
            "",
            "user@exa mple.com"
        )

        invalidEmails.forEach { email ->
            assertFalse("Email $email should be invalid", Validator.validateEmailFormat(email))
        }
    }
}