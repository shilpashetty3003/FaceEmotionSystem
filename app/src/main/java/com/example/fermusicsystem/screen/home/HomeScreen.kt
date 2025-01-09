package com.example.fermusicsystem.screen.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fermusicsystem.R
import com.example.fermusicsystem.ui.theme.FERMusicSystemTheme


class HomeScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FERMusicSystemTheme {
                Surface {
                    HomeScreenUI()

                }
            }
        }
    }
}

@Composable
fun HomeScreenUI() {

    val backgroundColor = Color(0xFF9BCBC3)
    val accentColor = Color(0xFF62E3DB)
    val gradientColors = listOf(
        Color(0xFF62E3DB), // Top color
        Color(0xFF9BCBC3)  // Bottom color
    )

    val buttonColor = Color(0xFF30A8A3)
    val textColor = Color.White

    // Main Screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset(0f, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_face_ai), // Replace with actual drawable
                contentDescription = "Face Detection",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()

            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    // TODO: Add camera functionality here
                },
                colors = ButtonDefaults.buttonColors(contentColor = Color(0xFF30A8A3)),
                modifier = Modifier
                    .size(width = 200.dp, height = 50.dp)
            ) {
                Text(
                    text = "Start Camera",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(10) { // Replace with your dynamic list
                    ProfileCard(
                        image = painterResource(R.drawable.ic_face_placeholder), // Replace with actual drawable
                        emotion = if (it % 2 == 0) "Happy" else "Sad"
                    )
                }
            }
        }
    }
}

@Composable
fun CircularButton(label: String, backgroundColor: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(80.dp)
            .background(backgroundColor, shape = CircleShape)
            .clickable { /* Handle Click */ }
    ) {
        Text(
            text = label,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ProfileCard(image: Painter, emotion: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .padding(8.dp)
    ) {
        Image(
            painter = image,
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = emotion,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenUIPreview() {
    HomeScreenUI()
}