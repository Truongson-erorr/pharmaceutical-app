package com.example.suggested_food

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import com.example.suggested_food.authentication.LoginScreen
import com.example.suggested_food.authentication.RegisterScreen
import com.example.suggested_food.screens.AllCategoriesScreen
import com.example.suggested_food.screens.CartContent
import com.example.suggested_food.screens.MainScreen
import com.example.suggested_food.screens.ProfileContent
import com.example.suggested_food.screens.SearchScreen
import com.example.suggested_food.ui.theme.Suggested_FoodTheme
import com.example.suggested_food.viewmodels.AuthViewModel
import com.example.suggested_food.viewmodels.CategoryViewModel
import com.example.suggested_food.viewmodels.ProductViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Suggested_FoodTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel(),
    categoryViewModel: CategoryViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel()
) {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = "MainScreen",
        enterTransition = {
            slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(300))
        }
    ) {
        composable("MainScreen") {
            MainScreen(
                navController = navController,
                authViewModel = authViewModel,
            )
        }
        composable("login") {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable("ProfileContent") {
            ProfileContent(navController = navController)
        }
        composable("SearchScreen") {
            SearchScreen(navController = navController)
        }
        composable("CartContent") {
            CartContent(navController = navController)
        }
        composable("AllCategoriesScreen") {
            AllCategoriesScreen(navController = navController)
        }
    }
}