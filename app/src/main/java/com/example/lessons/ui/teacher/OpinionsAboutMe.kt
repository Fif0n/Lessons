package com.example.lessons.ui.teacher

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.lessons.models.Rating
import com.example.lessons.viewModels.teacher.OpinionsAboutMeViewModel

@Composable
fun OpinionsAboutMe(navController: NavController, viewModel: OpinionsAboutMeViewModel = hiltViewModel()) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                        ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.LightGray),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Opinions about me",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .weight(3f)
                                .wrapContentWidth(Alignment.CenterHorizontally),
                        )
                    }
                }
            }
        }

        val ratingItems = viewModel.ratingFlow.collectAsLazyPagingItems()
        RatingsList(ratingItems)
    }
}

@Composable
fun RatingsList(ratings: LazyPagingItems<Rating>) {
    val context = LocalContext.current
    LaunchedEffect(key1 = ratings.loadState) {
        if (ratings.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error occurred: " + (ratings.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (ratings.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items (
                    ratings.itemCount,
                    key = ratings.itemKey { it._id }
                ) { index ->
                    val rating = ratings[index]
                    if (rating != null) {
                        TeacherItem(
                            rating = rating
                        )
                    }
                }
                item {
                    if (ratings.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun TeacherItem(
    rating: Rating
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(IntrinsicSize.Max)
        ) {
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
            ) {
                val student = rating.student

                Text(
                    text = "By: ${student.getFullName()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val fullStars = rating.rate.toInt()
                    val hasHalfStar = (rating.rate - fullStars) >= 0.5
                    val emptyStars = 5 - fullStars - if (hasHalfStar) 1 else 0

                    repeat(fullStars) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Full Star",
                            tint = Color(0xFFFFD700)
                        )
                    }
                    if (hasHalfStar) {
                        Icon(
                            imageVector = Icons.Filled.StarHalf,
                            contentDescription = "Half Star",
                            tint = Color(0xFFFFD700)
                        )
                    }
                    repeat(emptyStars) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "Empty Star",
                            tint = Color.LightGray
                        )
                    }

                    Text("${rating.rate}")
                }

                Text(
                    text = "Content: ${rating.text ?: "No content provided"}",
                    fontSize = 16.sp
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "At: ${rating.timestamp}",
                        color = Color.Gray,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}