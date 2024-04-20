package uk.ac.tees.mad.D3709023.sign_in

import java.lang.Error

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)