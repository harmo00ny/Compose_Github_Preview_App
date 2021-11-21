package com.example.compose_github_preview_app.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.compose_github_preview_app.model.Repository
import com.example.compose_github_preview_app.model.User
import com.example.compose_github_preview_app.ui.compose.Center
import com.example.compose_github_preview_app.ui.theme.Compose_GitHub_Preview_AppTheme
import com.google.accompanist.glide.rememberGlidePainter
import com.orhanobut.logger.Logger

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(),
) {
    val user = homeViewModel.user.collectAsState().value
    val repositories = homeViewModel.repositories.collectAsState().value
    val index = homeViewModel.selectedIndex.collectAsState().value

    Logger.d("ボトムナビゲーション$index")


    if (index == 0) {
        Scaffold(
            topBar = { TopAppBar() },
            bottomBar = { BottomBar(homeViewModel = homeViewModel, index = index) }
        ) {
            HomeScreenBody(
                user = user,
                repositories = repositories,
                onClickUserCard = {
                    navController.navigate("web-view/?url=${homeViewModel.userPageUrl}")
                },
                onClickRepository = {
                    navController.navigate("web-view/?url=${homeViewModel.getRepositoryPageUrl(it)}")
                }
            )
        }
    }
    if (index == 1) {
        Scaffold(
            topBar = { TopAppBar() },
            bottomBar = { BottomBar(homeViewModel = homeViewModel, index = index) }
        ) {

        }
    }
}

@Composable
fun TopAppBar() {
    TopAppBar(
        title = { Text("Github Preview")}
    )
}
@Composable
fun BottomBar(homeViewModel: HomeViewModel, index: Int) {
    BottomNavigation(elevation = 10.dp) {
        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Home,"")
        },
            label = { Text(text = "Home") },
            selected = (index == 0),
            onClick = {
                homeViewModel.setIndex(0)
            })

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Favorite,"")
        },
            label = { Text(text = "Favorite") },
            selected = (index == 1),
            onClick = {
                homeViewModel.setIndex(1)
            })

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Person,"")
        },
            label = { Text(text = "Profile") },
            selected = (index == 2),
            onClick = {
                homeViewModel.setIndex(2)
            })
    }
}

@Composable
fun HomeScreenBody(
    user: User?,
    repositories: List<Repository>,
    onClickUserCard: () -> Unit,
    onClickRepository: (repository: Repository) -> Unit,
) {
    if (user == null) {
        Center {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            Modifier
                .fillMaxSize()
        ) {
            item { UserCard(user = user, onClick = onClickUserCard) }

            item { Spacer(modifier = Modifier.size(16.dp)) }

            if (repositories.isEmpty()) {
                item { Center(modifier = Modifier.padding(top = 16.dp)) { CircularProgressIndicator() } }
            } else {
                repositories.forEach { repository ->
                    item { RepositoryItem(repository = repository, onClick = onClickRepository) }
                    item { Divider(Modifier.padding(start = 16.dp)) }
                }
            }
        }
    }
}

@Composable
private fun UserCard(
    user: User,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = onClick),
        elevation = 4.dp,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .padding(end = 8.dp)
                    .size(80.dp),
                shape = CircleShape,
                elevation = 4.dp,
            ) {
                Image(
                    painter = rememberGlidePainter(request = user.avatarUrl),
                    contentDescription = "avatar url"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(text = user.name, style = TextStyle(fontSize = 20.sp))
                user.bio?.let {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = it,
                        style = TextStyle(fontSize = 12.sp)
                    )
                }
                Spacer(modifier = Modifier.size(4.dp))
                user.company?.let {
                    Text(text = it, style = TextStyle(fontSize = 12.sp))
                }
//                Text(text = user.location, style = TextStyle(fontSize = 12.sp))
            }
        }
    }
}

@Composable
fun RepositoryItem(
    repository: Repository,
    onClick: (repository: Repository) -> Unit,
) {
    Surface(
        Modifier
            .fillMaxWidth()
            .clickable { onClick(repository) }
            .padding(horizontal = 16.dp)
            .height(60.dp)
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(repository.name, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
            Text(
                repository.description ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(fontSize = 12.sp, color = Color.Gray),
            )
        }
    }
}

@Preview
@Composable
fun UserCardPreview() {
    val user = User(
        name = "Name",
        avatarUrl = "https://placehold.jp/150x150.png",
        company = "@company",
        blog = "@blog",
        location = "tokyo/japan",
        bio = "description",
        twitterUserName = "@example"
    )
    Compose_GitHub_Preview_AppTheme() {
        Scaffold() {
            UserCard(user = user, onClick = {})
        }
    }
}