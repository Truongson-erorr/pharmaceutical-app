package com.example.suggested_food

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.suggested_food.authentication.LoginScreen
import com.example.suggested_food.authentication.RegisterScreen
import com.example.suggested_food.screens.AllCategoriesScreen
import com.example.suggested_food.screens.CartContent
import com.example.suggested_food.screens.CategoryProductsScreen
import com.example.suggested_food.screens.CheckoutScreen
import com.example.suggested_food.screens.MainScreen
import com.example.suggested_food.screens.ProductDetailScreen
import com.example.suggested_food.screens.ProfileContent
import com.example.suggested_food.screens.SearchScreen
import com.example.suggested_food.ui.theme.Suggested_FoodTheme
import com.example.suggested_food.viewmodels.AuthViewModel
import com.example.suggested_food.viewmodels.CartViewModel
import com.example.suggested_food.viewmodels.CategoryViewModel
import com.example.suggested_food.viewmodels.ProductViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel(),
    categoryViewModel: CategoryViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val navController = rememberAnimatedNavController()

    LaunchedEffect(Unit) {
        cartViewModel.loadCartFromFirestore()
    }

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
            ProfileContent(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable("SearchScreen") {
            SearchScreen(navController = navController)
        }
        composable("CartContent") {
            CartContent(
                navController = navController,
                cartViewModel = cartViewModel,
                authViewModel = authViewModel
            )
        }
        composable("AllCategoriesScreen") {
            AllCategoriesScreen(navController = navController)
        }
        composable(
            route = "category/{categoryId}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            CategoryProductsScreen(
                navController = navController,
                categoryId = categoryId
            )
        }
        composable(
            route = "ProductDetail/{productId}",
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType }
            )
        ) {
            val productId = it.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                navController = navController,
                productId = productId,
                cartViewModel = cartViewModel,
                authViewModel = authViewModel
            )
        }
        composable("checkout") {
            CheckoutScreen(navController, cartViewModel)
        }

    }
}