package uk.ac.tees.mad.D3709023

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import uk.ac.tees.mad.D3709023.profile.ProfileScreen
import uk.ac.tees.mad.D3709023.sign_in.GoogleAuthUIClient
import uk.ac.tees.mad.D3709023.sign_in.SignInScreen
import uk.ac.tees.mad.D3709023.sign_in.SignInViewModel
import uk.ac.tees.mad.D3709023.ui.theme.MusicPlayerTheme


class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private val googleAuthUIClient by lazy {
        GoogleAuthUIClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(
                applicationContext
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val musicViewModel: MusicViewModel = viewModel()

            MusicPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    Scaffold(
                        topBar = {
                            TopNavigationBar(navController) {
                                // Callback for sign out click
                                lifecycleScope.launch {
                                    googleAuthUIClient.signOut()
                                    Toast.makeText(
                                        applicationContext,
                                        "Signed Out",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.popBackStack("sign_in", inclusive = true)
                                }
                            }
                        }
                    ) {PaddingValues->
                        Column(modifier = Modifier.padding(top = 56.dp)) {
                            NavHost(
                                navController = navController,
                                startDestination = "sign_in",
                                modifier = Modifier.padding(top = if (navController.currentDestination?.route == "music_display") 56.dp else 0.dp)
                            ) {

                                composable("sign_in") {
                                    // Sign-in logic, navigates to "music_display" on success
                                    val viewModel = viewModel<SignInViewModel>()
                                    val state by viewModel.state.collectAsStateWithLifecycle()

                                    LaunchedEffect(key1 = Unit) {
                                        if (googleAuthUIClient.getSignedInUser() != null) {
                                            navController.navigate("music_display")
                                        }
                                    }

                                    val launcher = rememberLauncherForActivityResult(
                                        contract = ActivityResultContracts.StartIntentSenderForResult(),


                                        onResult = { result ->
                                            if (result.resultCode == RESULT_OK) {
                                                lifecycleScope.launch {
                                                    val signInResult =
                                                        googleAuthUIClient.signInWithIntent(
                                                            intent = result.data ?: return@launch
                                                        )
                                                    viewModel.onSignInResult(signInResult)
                                                }
                                            }
                                        }
                                    )

                                    LaunchedEffect(key1 = state.isSignInSuccessful) {
                                        if (state.isSignInSuccessful) {
                                            Toast.makeText(
                                                applicationContext,
                                                "Sign In Successfully",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            navController.navigate("music_display") {
                                            }
                                            viewModel.resetState()
                                        }
                                    }

                                    SignInScreen(state = state,
                                        onSignInClick = {
                                            lifecycleScope.launch {
                                                val signInIntentSender = googleAuthUIClient.signIn()
                                                launcher.launch(
                                                    IntentSenderRequest.Builder(
                                                        signInIntentSender ?: return@launch
                                                    ).build()
                                                )
                                            }
                                        }
                                    )
                                }

                                composable("profile") {
                                    // Profile display logic
                                    ProfileScreen(

                                        userData = googleAuthUIClient.getSignedInUser(),
                                        onSignOut = {
                                            lifecycleScope.launch {
                                                googleAuthUIClient.signOut()
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Signed Out",
                                                    Toast.LENGTH_LONG
                                                ).show()

//                                                navController.popBackStack()
                                                navController.navigate("sign_in")
                                            }
                                        }, updateProfilePicture = {},
                                        PaddingValues
                                    )
                                }
                                composable("music_display") {
                                    // Music data display logic
                                    MusicDisplayScreen(musicViewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MusicDisplayScreen(musicViewModel: MusicViewModel) {
    val music by musicViewModel.music.observeAsState()

    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(true) {
        musicViewModel.fetchMusic()
        isLoading.value = false
    }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        music?.data?.forEach { data ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        data.album?.let { album ->
                            Image(
                                painter = rememberImagePainter(album.cover),
                                contentDescription = data.title,
                                modifier = Modifier.size(80.dp)
                            )
                        }

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = data.title,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 2.dp)
                            )
                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                Button(
                                    onClick = { musicViewModel.initAndPlayMusic(data.preview) },
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .fillMaxWidth()
                                        .weight(1F)
                                ) {
                                    Text("Play")
                                }
                                Button(
                                    onClick = { musicViewModel.pauseMusic() },
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .fillMaxWidth()
                                        .weight(1F)
                                ) {
                                    Text("Pause")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(navController: NavController, onSignOutClick: () -> Unit) {

    TopAppBar(
        title = { Text(text = "Music Player") },
        navigationIcon = {
            IconButton(onClick = { navController.navigate("profile") }) {
                Icon(Icons.Filled.AccountCircle, contentDescription = "Menu")
            }
        }
    )
}

@Preview
@Composable
fun MainActivityPreview() {
    MusicPlayerTheme {
        val navController = rememberNavController()
        Scaffold(
            topBar = { TopNavigationBar(navController, onSignOutClick = {}) }
        ) { paddingValues ->
            Surface {
                Modifier.padding(paddingValues)
            }
            MainActivity()
        }
    }
}



