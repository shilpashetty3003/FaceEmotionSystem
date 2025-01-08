package com.example.fermusicsystem.screen.musicsuggestion

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.NonNull
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.fermusicsystem.common.ResponseResult
import com.example.fermusicsystem.data.model.Item
import com.example.fermusicsystem.data.model.Music
import com.example.fermusicsystem.ui.theme.FERMusicSystemTheme
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicSuggestScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val emotion = intent.getStringExtra("data")
            val viewModel: MusicSuggestionViewModel = hiltViewModel()
            viewModel.addEmotionToLocal(emotion = emotion.toString())
            viewModel.getVideoDetails(getSuggestion(emotion.toString()))
            FERMusicSystemTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Log.d(TAG, "Musicccc")
                    val response by viewModel.musicData.observeAsState(initial = ResponseResult.Loading)

                    when (response) {
                        is ResponseResult.Loading -> {
                            Log.d(TAG, "ResponseLoading ")
//                            showLoading.value = true

                        }

                        is ResponseResult.Success -> {
                            val musicDetails = (response as ResponseResult.Success<Music>).data
                            Log.d(TAG, "ResponseSuccess:  ${musicDetails}")
                            val context = LocalContext.current
                            ShowVideos(videos = musicDetails.items,context)

                        }

                        is ResponseResult.Failure -> {
                            val error = (response as ResponseResult.Failure).error
                            Log.d(TAG, "ResponseFailure  ${error}")
                        }
                    }

                }
            }
        }
    }


    private fun getSuggestion(emotion: String): String {
        return when (emotion) {
            "neutral" -> "Trending music in bollywood"
            "happy" -> "Songs & movies for a Happy Mood in hindi"
            "surprised" -> "Bollywood Movies with Surprising Elements"
            "sad" -> "Bollywood Movies & songs  with Emotional Themes"
            "anger" -> "Bollywood Movies & songs  with anger Themes"
            "disgust" -> "Songs & movies for a Happy Mood in hindi"
            else -> "Mood happy songs"
        }
    }

    companion object {
        val TAG: String = javaClass.name
    }
}


@Composable
fun ShowVideos(videos: List<Item>,context: Context) {
    LazyColumn {
        items(videos) { item ->
            YouTubeCard(
                videoThumbnailUrl = item.snippet.thumbnails.high.url,
                channelImageUrl = item.snippet.thumbnails.high.url,
                videoTitle = item.snippet.title,
                channelName = item.snippet.channelTitle,
                views = "2000",
                uploadDate = item.snippet.publishTime
            ) {
                Log.d("TAG", "ShowVideos: " + "https://www.youtube.com/watch?v=\" + ${item.id.videoId}")
                context.startActivity(Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v=${item.id.videoId}")))
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YouTubeCard(
    videoThumbnailUrl: String,
    channelImageUrl: String,
    videoTitle: String,
    channelName: String,
    views: String,
    uploadDate: String,
    onVideoClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.Black),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(10.dp), onClick = onVideoClick
    ) {
        Column(Modifier.background(Color.Black)) {
            // Video Thumbnail
            Image(
                painter = rememberAsyncImagePainter(model = videoThumbnailUrl),
                contentDescription = "Video Thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))


            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Channel Image
                Image(
                    painter = rememberAsyncImagePainter(model = channelImageUrl),
                    contentDescription = "Channel Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Title and Channel Name
                Column(
                    modifier = Modifier
                        .weight(1f) // Take up remaining space
                ) {
                    Text(
                        text = videoTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = channelName,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "$views • $uploadDate",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun VideoPlayerScreen(videoId: String) {
    val activityLifecycle = LocalLifecycleOwner.current
    val context = LocalContext.current

    val youtubePlayer = remember {
        YouTubePlayerView(context).apply {
            activityLifecycle.lifecycle.addObserver(this)
            enableAutomaticInitialization = false
            initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo("zHiKFSBO_JE", 0f)
                }
            })
        }
    }

    AndroidView(
        {
            youtubePlayer
        }, modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
}






