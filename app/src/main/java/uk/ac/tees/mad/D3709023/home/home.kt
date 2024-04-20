package uk.ac.tees.mad.D3709023.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicplayer.R


@Preview
@Composable
fun PreviewBox(){
    Box(
        img=R.drawable.music_vector,
        title = "Music Name"
    )
}
@Composable
fun Box(img: Int, title: String) {
    Card(
        modifier = Modifier
            .padding(start = 32.dp, end = 32.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painterResource(id = img),
                contentDescription = "",
                modifier = Modifier
                    .size(80.dp)
                    .weight(.3f)
            )
            Column(
                modifier = Modifier.padding(start = 16.dp)
                    .weight(.7f)


            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth()

                )
                Row {
                    Button(
                        onClick = { /*TODO*/ },

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
    }
}

