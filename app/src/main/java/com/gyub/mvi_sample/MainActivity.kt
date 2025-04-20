package com.gyub.mvi_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gyub.mvi_sample.presentation.detail.DetailRoute
import com.gyub.mvi_sample.presentation.main.MainRoute
import com.gyub.mvi_sample.presentation.navigation.Destination
import com.gyub.mvi_sample.ui.theme.MVISampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MVISampleTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Destination.Main
                ) {
                    composable<Destination.Main> {
                        MainRoute(
                            navigateUserDetail = { navController.navigate(Destination.Detail(it)) }
                        )
                    }
                    composable<Destination.Detail> {
                        DetailRoute()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MVISampleTheme {
        Greeting("Android")
    }
}