package uk.ac.tees.mad.D3709023.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.storage.FirebaseStorage
import uk.ac.tees.mad.D3709023.sign_in.UserData
import java.io.ByteArrayOutputStream


@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
    updateProfilePicture: (String) -> Unit,
    paddingValues: PaddingValues
) {
    var imageUri by remember {
        mutableStateOf<String?>(null)
    }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it.toString()
            uploadImageToFirebase(it, userData?.userId ?: "", updateProfilePicture)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (userData?.profilePictureUrl != null) {
            AsyncImage(

                model = imageUri,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (userData?.username != null) {
            Text(
                text = userData.username,
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
//
        Button(onClick = {
            imagePickerLauncher.launch("image/*")
        }) {
            Text(text = "Change Profile Picture")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onSignOut) {
            Text(text = "Sign Out")

        }
    }

}

//fun uploadImageToFirebase(bitmap: Bitmap, context: ComponentActivity, callback: (Boolean) -> Unit) {
//    val storageRef = Firebase.storage.reference
//    val imageRef = storageRef.child("images/${bitmap}")
//
//    val baos = ByteArrayOutputStream()
//    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//
//    val imageData = baos.toByteArray()
//    imageRef.putBytes(imageData).addOnSuccessListener {
//        callback(true)
//    }.addOnFailureListener {
//        callback(false)
//    }
//}


fun uploadImageToFirebase(imageUri: Uri, userId: String, updateProfilePicture: (String) -> Unit) {
    val storageRef = FirebaseStorage.getInstance().reference.child("profileImages/$userId.jpg")
    storageRef.putFile(imageUri).addOnSuccessListener {
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()
            updateProfilePicture(imageUrl)
        }
    }
}