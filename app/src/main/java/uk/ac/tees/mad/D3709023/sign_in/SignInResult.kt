package uk.ac.tees.mad.D3709023.sign_in

import android.service.autofill.UserData

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class USerData(
    val userId: String,
    val username:String?,
    val profilePictureUrl: String?
)
