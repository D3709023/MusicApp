//package uk.ac.tees.mad.D3709023.home
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import coil.compose.rememberAsyncImagePainter
//import coil.compose.rememberImagePainter
//import com.example.musicplayer.R
//import uk.ac.tees.mad.D3709023.SongViewModel
//import uk.ac.tees.mad.D3709023.apiData.Data
//import androidx.compose.runtime.*
//
//
//
////@Preview
////@Composable
////fun PreviewBox(){
////    Box(
////        img=R.drawable.music_vector,
////        title = "Music Name"
////    )
////}
////@Composable
////fun Box(img: Int, title: String) {
////    Card(
////        modifier = Modifier
//////            .padding(start = 32.dp, end = 32.dp)
////            .fillMaxWidth()
////    ) {
////        Row(
////            verticalAlignment = Alignment.CenterVertically,
////            modifier = Modifier.padding(8.dp)
////        ) {
////            Image(
////                painterResource(id = img),
////                contentDescription = "",
////                modifier = Modifier
////                    .size(80.dp)
//////                    .weight(.3f)
////            )
////            itemDescription(title,modifier=Modifier)
////        }
////    }
////}
////
//@Composable
//private fun itemDescription(title: String) {
//    Column(
//        modifier = Modifier
////            .weight(0.7f, true)
//            .padding(start = 16.dp)
//
//    ) {
//        Text(
//            text = title,
//            style = MaterialTheme.typography.headlineMedium,
//            fontWeight = FontWeight.Bold,
//            textAlign = TextAlign.Center,
//            fontSize = 20.sp,
//            modifier = Modifier.fillMaxWidth()
//
//        )
//        Row {
//            Button(
//                onClick = { /*TODO*/ },
//
//                modifier = Modifier
//                    .padding(2.dp)
//                    .fillMaxWidth()
//                    .weight(1F),
//            ) {
//                Text(text = "Play")
//
//            }
//            Button(
//                onClick = { /*TODO*/ },
//                modifier = Modifier
//                    .padding(2.dp)
//                    .fillMaxWidth()
//                    .weight(1F)
//            ) {
//                Text(text = "Pause")
//            }
//        }
//
//    }
//}
//
//
//@Composable
//fun Box(music: Data) {
//    Card(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(8.dp)
//        ) {
//            Image(
//                painter = rememberAsyncImagePainter(music.album.cover),
//                contentDescription = music.title,
//                modifier = Modifier.size(80.dp)
//
//            )
//            itemDescription(music.title)
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MusicApp() {
//    val musicViewModel: SongViewModel = viewModel()
////    val musicState by musicViewModel.musicState.collectAsState()
//
//    LaunchedEffect(true) {
//        musicViewModel.fetchMusic()
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text(text = "Music Player") })
//        },
////        content = {padding->
////            Column(
////                modifier = Modifier.padding(padding)
////            ) {
////                musicState?.let { Box(it) }
////            }
////        }
//        content = {
//            itemDescription(musicViewModel = musicViewModel)
//        }
//    )
//}
//
////@Composable
////private fun MusicContent(musicViewModel: SongViewModel) {
////    val music by musicViewModel.music.observeAsState()
////    val isLoading by musicViewModel.isLoading.observeAsState()
////    val error by musicViewModel.error.observeAsState()
////
////    if (isLoading == true) {
////        // Show loading indicator
////        Text("Loading...", modifier = Modifier.padding(16.dp))
////    } else if (error != null) {
////        // Show error message
////        Text("Error: ${error.message}", modifier = Modifier.padding(16.dp))
////    } else {
////        // Show music data
////        music?.let {
////            Box(it)
////        }
////    }
////}
//
