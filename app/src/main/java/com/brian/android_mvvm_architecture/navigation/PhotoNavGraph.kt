package com.brian.android_mvvm_architecture.navigation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.brian.android_mvvm_architecture.ui.photo_detail.PhotoDetailScreen
import com.brian.android_mvvm_architecture.ui.photo_detail.PhotoDetailUiState
import com.brian.android_mvvm_architecture.ui.photo_detail.PhotoDetailViewModel
import com.brian.android_mvvm_architecture.ui.photos.PhotosScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@Composable
fun PhotoNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PhotoList.route,
        modifier = modifier
    ) {
        composable(Screen.PhotoList.route) {
            PhotosScreen(
                onPhotoClick = { photoId ->
                    navController.navigate(Screen.PhotoDetail.createRoute(photoId))
                },
            )
        }
        composable(
            route = Screen.PhotoDetail.route,
            arguments = listOf(navArgument(PHOTO_ID) { type = NavType.IntType })
        ) {
            val viewModel: PhotoDetailViewModel = hiltViewModel()
            val uiState = viewModel.uiState.collectAsState()
            val photoId = it.arguments?.getInt(PHOTO_ID)

            photoId?.let {
                LaunchedEffect(photoId) {
                    viewModel.getPhoto(photoId)
                }
                when (val state = uiState.value) {
                    is PhotoDetailUiState.Success -> {
                        PhotoDetailScreen(
                            photoUi = state.photo,
                            navigateUp = { navController.navigateUp() }
                        )
                    }

                    is PhotoDetailUiState.Error -> {
                        Text(text = state.message)
                    }

                    is PhotoDetailUiState.Loading -> {
                        CircularProgressIndicator()
                    }
                }
            }

        }
    }
}