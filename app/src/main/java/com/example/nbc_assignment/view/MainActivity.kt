package com.example.nbc_assignment.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.nbc_assignment.R
import com.example.nbc_assignment.model.Item
import com.example.nbc_assignment.model.Shelf
import com.example.nbc_assignment.repository.DataResult
import com.example.nbc_assignment.ui.theme.Nbc_AssignmentTheme
import com.example.nbc_assignment.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Nbc_AssignmentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Box {
                        HomepageScreen()
                    }
                }
            }
        }
    }

    @Composable
    fun HomepageScreen(viewModel: HomeViewModel = hiltViewModel()) {
        val homepageData = viewModel.homepageData.collectAsState()

        when (val result = homepageData.value) {
            is DataResult.Success -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF0089A8), Color(0xFF500053))
                            )
                        )
                ) {
                    result.let { homepage ->
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            items(homepage.data.shelves) { shelf ->
                                ShelfView(shelf)
                            }
                        }
                    }

                }
            }

            is DataResult.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error loading data ${result.exception}", color = Color.Black)
                }
            }
            is DataResult.Loading ->{
                Timber.tag("LoadingScreen").d("Displaying loading screen")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            null -> {
                Timber.tag("LoadingScreen").d("Displaying loading screen")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    @Composable
    fun ShelfView(shelf: Shelf) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = shelf.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start =8.dp),
                color = Color.White
            )


            LazyRow {
                items(shelf.items) { item ->
                    ItemView(item)
                }
            }
        }
    }

    @Composable
    fun ItemView(item: Item?) {
        item?.let{
            when (item) {
                is Item.Live -> {
                    ContinueWatchingItemView(item)
                }

                is Item.Show -> {
                    TrendingItemView(item)
                }

                is Item.Episode -> {
                    LatestEpisodeItemView(item)
                }
            }
        }

    }
    @Composable
    fun TrendingItemView(item: Item.Show) {
        Column(
            modifier = Modifier
                .padding(vertical = 0.dp, horizontal = 8.dp )
                .width(180.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxHeight()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = item.image),
                    contentDescription = "",
                    modifier = Modifier.size(240.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2

                )
                Spacer(modifier = Modifier.width(12.dp))

                Icon(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = "",
                    modifier = Modifier.size(26.dp).weight(.3f),
                    tint = Color.White,

                    )
            }
        }
    }

    @Composable
    fun ContinueWatchingItemView(item: Item.Live) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 0.dp)
                .height(200.dp)
                .wrapContentSize()
        ) {
            Box(modifier = Modifier.fillMaxHeight(0.8f)) {
                Image(
                    painter = rememberAsyncImagePainter(model = item.image),
                    contentDescription = "",
                    modifier = Modifier
                        .size(250.dp)
                        .width(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Row(
                modifier = Modifier
                    .width(255.dp)
                    .height(200.dp).padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = item.title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Icon(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = "",
                    modifier = Modifier.size(26.dp).weight(.3f),
                    tint = Color.White,
                )
            }
        }
    }


    @Composable
    fun LatestEpisodeItemView(item: Item.Episode) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 0.dp)
                .height(200.dp)
                .wrapContentSize()
        ) {
            Box(
                modifier = Modifier.fillMaxHeight(0.8f)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = item.image),
                    contentDescription = "",
                    modifier = Modifier
                        .size(250.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop

                )
                if (item.labelBadge != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .background(Color.White)
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = item.labelBadge,
                            color = Color.Black,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .width(255.dp)
                    .height(200.dp).padding(top =8.dp)
            ) {
                Text(
                    text = item.title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Icon(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = "",
                    modifier = Modifier.size(26.dp).weight(.3f),
                    tint = Color.White,
                )
            }
        }
    }
}

