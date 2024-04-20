package uk.ac.tees.mad.D3709023.sign_in

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username:String?,
    val profilePictureUrl: String?
)
