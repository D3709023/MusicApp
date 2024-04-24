package uk.ac.tees.mad.D3709023

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import uk.ac.tees.mad.D3709023.apiData.Album
import uk.ac.tees.mad.D3709023.apiData.Data
import uk.ac.tees.mad.D3709023.apiData.MyData
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
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.isReady.value
            }

            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }
                zoomX.start()
                zoomY.start()


            }

        }

        setContent {
            val musicViewModel: MusicViewModel = viewModel()

            MusicPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "sign_in") {
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
                                    navController.navigate("profile") {
                                        popUpTo("sign_in") {
                                            inclusive = true
                                        }  // Clear backstack up to sign-in
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
                            ProfileScreen(userData = googleAuthUIClient.getSignedInUser(),
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUIClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed Out",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navController.popBackStack()
                                    }
                                }
                            )
                            navController.navigate("music_display")
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


@Composable
fun Box(music: MyData?, isLoading: Boolean, error: Exception?) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            music?.let { MyData ->
                MyData.data.firstOrNull()?.let { firstData ->
                    firstData.album?.let { album ->
                        Image(
                            painter = rememberImagePainter(album.cover),
                            contentDescription = firstData.title,
                            modifier = Modifier.size(80.dp)
                        )
                        itemDescription(firstData.title)
                    }
                    if (isLoading) {
                        // Show loading indicator
                        CircularProgressIndicator(modifier = Modifier.size(40.dp))
                    } else if (error != null) {
                        // Show error message
                        Text("Error: ${error.message}", modifier = Modifier.padding(start = 16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun itemDescription(title: String) {
    Column(
        modifier = Modifier.padding(start = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Row {
            Button(
                onClick = { },
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth()
                    .weight(1F),
            ) {
                Text(text = "Play")
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth()
                    .weight(1F)
            ) {
                Text(text = "Pause")
            }
        }
    }
}

@Composable
fun MusicDisplayScreen(musicViewModel: MusicViewModel) {
    LaunchedEffect(true) {
        musicViewModel.fetchMusic()
    }

    val music by musicViewModel.music.observeAsState()
    val isLoading by musicViewModel.isLoading.observeAsState()
    val error by musicViewModel.error.observeAsState()

//    Box(music, isLoading = isLoading == true, error = error)
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        music?.data?.forEach { data ->
            item {
                Box(
                    music = MyData(data = listOf(data), next = "", total = 1),
                    isLoading = isLoading == true,
                    error = error
                )
            }
        }
    }
}


@Preview
@Composable
fun BoxPreview() {
    val music = MyData(
        data = listOf(
            Data(
                album = Album("cover_image_url"),
                title = "Sample Music Title"
            )
        ),
        next = "",
        total = 1
    )
    Box(music = music, isLoading = false, error = null)
}


