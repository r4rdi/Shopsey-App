package com.example.shopsey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shopsey.ui.auth.CreatePasswordScreen
import com.example.shopsey.ui.auth.EmailInputScreen
import com.example.shopsey.ui.auth.ForgotPasswordScreen
import com.example.shopsey.ui.auth.LandingScreen
import com.example.shopsey.ui.auth.LoginSuccessScreen
import com.example.shopsey.ui.auth.OtpVerificationScreen
import com.example.shopsey.ui.auth.PasswordInputScreen
import com.example.shopsey.ui.auth.RegisterFormScreen
import com.example.shopsey.ui.auth.WelcomeScreen
import com.example.shopsey.ui.home.HomeScreen
import com.example.shopsey.ui.splash.SplashScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShopSeyNavGraph()
                }
            }
        }
    }
}

@Composable
fun ShopSeyNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // 1. Splash Screen
        composable(route = "splash") {
            SplashScreen(navController = navController)
        }

        // 2. Landing Screen (Halaman Awal setelah Splash Screen)
        composable(route = "landing_screen") {
            LandingScreen(
                onSignUpClick = {
                    navController.navigate("email_input_screen/signup")
                },
                onSignInClick = {
                    navController.navigate("email_input_screen/signin")
                }
            )
        }

        // 3. Email Input Screen (Routing Terpisah untuk Sign Up dan Sign In / Log In)
        composable(route = "email_input_screen/signup") {
            EmailInputScreen(
                isSignUp = true,
                onBack = {
                    navController.popBackStack()
                },
                onContinue = { inputEmail ->
                    val encodedEmail = URLEncoder.encode(inputEmail, StandardCharsets.UTF_8.toString())
                    // Alur Sign Up: Setelah klik Lanjutkan menuju pembuatan kata sandi terlebih dahulu
                    navController.navigate("create_password_screen/$encodedEmail")
                }
            )
        }

        composable(route = "email_input_screen/signin") {
            EmailInputScreen(
                isSignUp = false,
                onBack = {
                    navController.popBackStack()
                },
                onContinue = { inputEmail ->
                    val encodedEmail = URLEncoder.encode(inputEmail, StandardCharsets.UTF_8.toString())
                    // Alur Sign In / Log In: Menuju OTP Verification Screen
                    navController.navigate("otp_verification_screen/$encodedEmail")
                }
            )
        }

        // Fallback untuk route dengan parameter dinamis
        composable(
            route = "email_input_screen/{flowType}",
            arguments = listOf(
                navArgument("flowType") {
                    type = NavType.StringType
                    defaultValue = "signin"
                }
            )
        ) { backStackEntry ->
            val flowType = backStackEntry.arguments?.getString("flowType") ?: "signin"
            val isSignUp = flowType == "signup"

            EmailInputScreen(
                isSignUp = isSignUp,
                onBack = {
                    navController.popBackStack()
                },
                onContinue = { inputEmail ->
                    val encodedEmail = URLEncoder.encode(inputEmail, StandardCharsets.UTF_8.toString())
                    if (isSignUp) {
                        navController.navigate("create_password_screen/$encodedEmail")
                    } else {
                        navController.navigate("otp_verification_screen/$encodedEmail")
                    }
                }
            )
        }

        // 4. Create Password Screen (Alur Sign Up: Pembuatan kata sandi sebelum mengisi form data diri)
        composable(
            route = "create_password_screen/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = "member@shopsey.com"
                }
            )
        ) { backStackEntry ->
            val rawEmail = backStackEntry.arguments?.getString("email") ?: "member@shopsey.com"
            val email = URLDecoder.decode(rawEmail, StandardCharsets.UTF_8.toString())

            CreatePasswordScreen(
                userEmail = email,
                onBack = {
                    navController.popBackStack()
                },
                onEditEmail = {
                    navController.popBackStack("email_input_screen/signup", inclusive = false)
                },
                onContinue = { _ ->
                    val encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.toString())
                    // Setelah kata sandi dibuat, lanjut ke pengisian form data diri (nama, tanggal lahir, dll)
                    navController.navigate("register_form_screen/$encodedEmail")
                }
            )
        }

        // 4. OTP Verification Screen & Simulasi Pop-Up Notifikasi
        composable(
            route = "otp_verification_screen/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = "member@shopsey.com"
                }
            )
        ) { backStackEntry ->
            val rawEmail = backStackEntry.arguments?.getString("email") ?: "member@shopsey.com"
            val email = URLDecoder.decode(rawEmail, StandardCharsets.UTF_8.toString())

            OtpVerificationScreen(
                userEmail = email,
                onBack = {
                    navController.popBackStack()
                },
                onEditEmail = {
                    navController.popBackStack()
                },
                onLoginSuccess = {
                    val encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.toString())
                    navController.navigate("login_success_screen/$encodedEmail")
                },
                onNavigateToPassword = {
                    val encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.toString())
                    navController.navigate("password_input_screen/$encodedEmail")
                }
            )
        }

        // 5. Password Input Screen ("Apa kata sandi Anda?")
        composable(
            route = "password_input_screen/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = "member@shopsey.com"
                }
            )
        ) { backStackEntry ->
            val rawEmail = backStackEntry.arguments?.getString("email") ?: "member@shopsey.com"
            val email = URLDecoder.decode(rawEmail, StandardCharsets.UTF_8.toString())

            PasswordInputScreen(
                userEmail = email,
                onBack = {
                    navController.popBackStack()
                },
                onEditEmail = {
                    navController.popBackStack("email_input_screen/signin", inclusive = false)
                },
                onLoginSuccess = {
                    val encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.toString())
                    navController.navigate("login_success_screen/$encodedEmail")
                },
                onNavigateToOtp = {
                    navController.popBackStack()
                },
                onForgotPassword = {
                    val encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.toString())
                    navController.navigate("forgot_password_screen/$encodedEmail")
                }
            )
        }

        // 6. Forgot Password Screen ("Verifikasikan email Anda dan masukkan kata sandi baru.")
        composable(
            route = "forgot_password_screen/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = "member@shopsey.com"
                }
            )
        ) { backStackEntry ->
            val rawEmail = backStackEntry.arguments?.getString("email") ?: "member@shopsey.com"
            val email = URLDecoder.decode(rawEmail, StandardCharsets.UTF_8.toString())

            ForgotPasswordScreen(
                userEmail = email,
                onBack = {
                    navController.popBackStack()
                },
                onEditEmail = {
                    navController.popBackStack("email_input_screen/signin", inclusive = false)
                },
                onResetSuccess = {
                    // Kata sandi berhasil diperbarui, kembali ke Landing Screen agar pengguna melakukan Sign In
                    navController.navigate("landing_screen") {
                        popUpTo("landing_screen") { inclusive = true }
                    }
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        // 7. Register Form Screen (Diakses dari Sign Up)
        composable(
            route = "register_form_screen/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = "member@shopsey.com"
                }
            )
        ) { backStackEntry ->
            val rawEmail = backStackEntry.arguments?.getString("email") ?: "member@shopsey.com"
            val email = URLDecoder.decode(rawEmail, StandardCharsets.UTF_8.toString())

            RegisterFormScreen(
                userEmail = email,
                onBack = {
                    navController.popBackStack()
                },
                onEditEmail = {
                    navController.popBackStack("email_input_screen/signup", inclusive = false)
                },
                onCreateAccountSuccess = {
                    // Akun berhasil dibuat, kembali ke Landing Screen agar pengguna melakukan Sign In
                    navController.navigate("landing_screen") {
                        popUpTo("landing_screen") { inclusive = true }
                    }
                }
            )
        }

        // 8. Login Success Screen ("Anda berhasil masuk.")
        composable(
            route = "login_success_screen/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = "member@shopsey.com"
                }
            )
        ) { backStackEntry ->
            val rawEmail = backStackEntry.arguments?.getString("email") ?: "member@shopsey.com"
            val email = URLDecoder.decode(rawEmail, StandardCharsets.UTF_8.toString())

            LoginSuccessScreen(
                userEmail = email,
                onBack = {
                    navController.popBackStack()
                },
                onContinue = {
                    val encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.toString())
                    navController.navigate("welcome_screen/$encodedEmail")
                }
            )
        }

        // 9. Welcome Screen (Kolase Gambar Miring & Sambutan)
        composable(
            route = "welcome_screen/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = "member@shopsey.com"
                }
            )
        ) { backStackEntry ->
            val rawEmail = backStackEntry.arguments?.getString("email") ?: "member@shopsey.com"
            val email = URLDecoder.decode(rawEmail, StandardCharsets.UTF_8.toString())

            WelcomeScreen(
                userEmail = email,
                onContinue = {
                    navController.navigate("home") {
                        popUpTo("landing_screen") { inclusive = true }
                    }
                }
            )
        }

        // 10. Home Screen
        composable(route = "home") {
            HomeScreen()
        }
    }
}