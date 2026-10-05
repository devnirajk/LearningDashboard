package com.example.learningapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.learningapp.ui.coursedetail.CourseDetailScreen
import com.example.learningapp.ui.dashboard.DashboardScreen
import com.example.learningapp.ui.login.LoginScreen

private const val ROUTE_LOGIN = "login"
private const val ROUTE_DASHBOARD = "dashboard"
private const val ROUTE_DETAIL = "detail/{courseId}"

@Composable
fun LearningAppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ROUTE_LOGIN) {

        composable(ROUTE_LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(ROUTE_DASHBOARD) {
                        popUpTo(ROUTE_LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(ROUTE_DASHBOARD) {
            DashboardScreen(
                onCourseClick = { courseId ->
                    navController.navigate("detail/$courseId")
                }
            )
        }

        composable(
            route = ROUTE_DETAIL,
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) {
            CourseDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
