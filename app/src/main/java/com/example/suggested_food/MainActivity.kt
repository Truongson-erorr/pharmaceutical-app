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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.suggested_food.authentication.ForgotPasswordScreen
import com.example.suggested_food.authentication.LoginScreen
import com.example.suggested_food.authentication.RegisterScreen
import com.example.suggested_food.screens.address.AddressScreen
import com.example.suggested_food.screens.ai.AISearchScreen
import com.example.suggested_food.screens.category.AllCategoriesScreen
import com.example.suggested_food.screens.cart.CartContent
import com.example.suggested_food.screens.category.CategoryProductsScreen
import com.example.suggested_food.screens.chat_ai.ChatScreen
import com.example.suggested_food.screens.checkout.CheckoutScreen
import com.example.suggested_food.screens.drug_look_up.DrugLookupScreen
import com.example.suggested_food.screens.home.MainScreen
import com.example.suggested_food.screens.order.OrderDetailScreen
import com.example.suggested_food.screens.order.OrderHistoryScreen
import com.example.suggested_food.screens.checkout.PaymentSuccessScreen
import com.example.suggested_food.screens.product.ProductDetailScreen
import com.example.suggested_food.screens.profile.ProfileContent
import com.example.suggested_food.screens.chat_doctor.UserChatScreen
import com.example.suggested_food.screens.profile.HealthProfileScreen
import com.example.suggested_food.ui.theme.Suggested_FoodTheme
import com.example.suggested_food.viewmodels.AuthViewModel
import com.example.suggested_food.viewmodels.CartViewModel
import com.example.suggested_food.viewmodels.HealthProfileViewModel
import com.example.suggested_food.viewmodels.OrderHistoryViewModel
import com.example.suggested_food.viewmodels.ProductViewModel
import com.example.suggested_food.viewmodels.UserViewModel
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
    cartViewModel: CartViewModel = viewModel(),
    healthProfileViewModel: HealthProfileViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
    orderHistoryViewModel: OrderHistoryViewModel = viewModel(),
) {
    val navController = rememberAnimatedNavController()

    val isLoggedIn by authViewModel.isLoggedInFlow.collectAsState()
    val role by authViewModel.userRole.collectAsState()

    if (isLoggedIn && role == null) {
        Box(
            modifier = androidx.compose.ui.Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LaunchedEffect(Unit) {
        cartViewModel.loadCartFromFirestore()
    }

    val startDestination = when {
        !isLoggedIn -> "login"
        role == "admin" -> "admin_home"
        else -> "MainScreen"
    }

    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
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
            CheckoutScreen(navController, cartViewModel, userViewModel)
        }
        composable("payment_success") {
            PaymentSuccessScreen(navController)
        }
        composable("chat") {
            ChatScreen(navController)
        }
        composable("OrderHistoryScreen") {
            OrderHistoryScreen(navController, orderHistoryViewModel)
        }
        composable(
            route = "order_detail/{orderId}",
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: return@composable
            OrderDetailScreen(orderId = orderId, navController = navController)
        }
        composable(
            "UserChatScreen/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            UserChatScreen(
                navController = navController,
                userId = it.arguments?.getString("userId")!!
            )
        }
        composable("address") {
            AddressScreen(navController)
        }
        composable("drug_lookup") {
            DrugLookupScreen(navController)
        }
        composable("AISearchScreen") {
            AISearchScreen(navController, productViewModel = productViewModel)
        }
        composable("ForgotPasswordScreen") {
            ForgotPasswordScreen(navController, authViewModel)
        }
        composable("health_profile") {
            HealthProfileScreen(navController, authViewModel)
        }
    }
}