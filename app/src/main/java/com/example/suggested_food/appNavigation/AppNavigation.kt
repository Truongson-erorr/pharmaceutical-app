package com.example.suggested_food.appNavigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.suggested_food.authentication.ForgotPasswordScreen
import com.example.suggested_food.authentication.LoginScreen
import com.example.suggested_food.authentication.RegisterScreen
import com.example.suggested_food.authentication.WelcomeScreen
import com.example.suggested_food.screens.activitylog.ActivityLogScreen
import com.example.suggested_food.screens.ai.AISearchScreen
import com.example.suggested_food.screens.category.AllCategoriesScreen
import com.example.suggested_food.screens.category.CategoryProductsScreen
import com.example.suggested_food.screens.chat_ai.ChatScreen
import com.example.suggested_food.screens.drug.AllProductScreen
import com.example.suggested_food.screens.drug_look_up.DrugLookupScreen
import com.example.suggested_food.screens.export_receipt.ExportStockScreen
import com.example.suggested_food.screens.home.MainScreen
import com.example.suggested_food.screens.import_receipt.ImportStockScreen
import com.example.suggested_food.screens.invoice.InvoiceDashboardScreen
import com.example.suggested_food.screens.invoice.InvoiceScreen
import com.example.suggested_food.screens.invoice_history.ExportDetailScreen
import com.example.suggested_food.screens.invoice_history.ImportDetailScreen
import com.example.suggested_food.screens.invoice_history.InvoiceHistoryScreen
import com.example.suggested_food.screens.notifications.NotificationScreen
import com.example.suggested_food.screens.patient.PatientDetailScreen
import com.example.suggested_food.screens.patient.PatientScreen
import com.example.suggested_food.screens.product.ProductDetailScreen
import com.example.suggested_food.screens.reminder.AddReminderScreen
import com.example.suggested_food.screens.reminder.ReminderScreen
import com.example.suggested_food.screens.search.SearchScreen
import com.example.suggested_food.screens.stock.StockAllScreen
import com.example.suggested_food.screens.stock.StockScreen
import com.example.suggested_food.screens.suggest.SuggestScreen
import com.example.suggested_food.viewmodel.ExportViewModel
import com.example.suggested_food.viewmodel.ImportViewModel
import com.example.suggested_food.viewmodels.AuthViewModel
import com.example.suggested_food.viewmodels.ProductViewModel
import com.example.suggested_food.viewmodels.PromoCodeViewModel
import com.example.suggested_food.viewmodels.ReminderViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel(),
    importViewModel: ImportViewModel = viewModel(),
    exportViewModel: ExportViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
) {
    val navController = rememberAnimatedNavController()
    val promoViewModel: PromoCodeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val isLoggedIn by authViewModel.isLoggedInFlow.collectAsState()
    val role by authViewModel.userRole.collectAsState()

    if (isLoggedIn && role == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val startDestination = when {
        !isLoggedIn -> "welcome"
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
        composable("welcome") {
            WelcomeScreen(navController = navController)
        }
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
            )
        }
        composable("chat") {
            ChatScreen(navController)
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
        composable("SearchScreen") {
            SearchScreen(navController, productViewModel = productViewModel)
        }
        composable("SuggestScreen") {
            SuggestScreen(navController)
        }
        composable("AllProductScreen") {
            AllProductScreen(navController)
        }
        composable("StockScreen") {
            StockScreen(navController)
        }
        composable("StockAllScreen") {
            StockAllScreen(navController)
        }
        composable("ImportStockScreen") {
            ImportStockScreen(navController)
        }
        composable("InvoiceScreen") {
            InvoiceScreen(navController)
        }
        composable("InvoiceDashboardScreen") {
            InvoiceDashboardScreen(navController)
        }
        composable("ExportStockScreen") {
            ExportStockScreen(navController)
        }
        composable("InvoiceHistoryScreen") {
            InvoiceHistoryScreen(navController)
        }
        composable("import_detail/{receiptId}") { backStack ->
            val id = backStack.arguments?.getString("receiptId") ?: ""
            ImportDetailScreen(
                navController= navController,
                receiptId = id,
                viewModel = importViewModel
            )
        }
        composable("export_detail/{receiptId}") { backStack ->
            val id = backStack.arguments?.getString("receiptId") ?: ""
            ExportDetailScreen(
                receiptId = id,
                navController = navController,
                viewModel = exportViewModel
            )
        }
        composable("NotificationScreen") {
            NotificationScreen(navController)
        }
        composable("ReminderScreen") {
            val viewModel: ReminderViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

            ReminderScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable("AddReminderScreen") {
            val viewModel: ReminderViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

            AddReminderScreen(
                navController = navController,
                viewModel = viewModel,
                productViewModel
            )
        }
        composable("PatientScreen") {
            PatientScreen(navController)
        }
        composable(
            "PatientDetail/{phone}"
        ) { backStackEntry ->
            val phone =
                backStackEntry.arguments?.getString("phone") ?: ""
            PatientDetailScreen(
                navController = navController,
                phone = phone
            )
        }
        composable("ActivityLogScreen") {
            ActivityLogScreen(navController)
        }
    }
}