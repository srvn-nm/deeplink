package com.example.deeplink

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.deeplink.ui.theme.DeeplinkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val context = this
            DeeplinkTheme {
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        Column(
                            modifier = Modifier
                                .padding(top = 260.dp)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = {
                                    navController.navigate("detail")
                                },
                                content = {
                                    Text(text = "Go to detail")
                                }
                            )

                            Button(
                                onClick = {
                                    navController.navigate("deeplink")
                                },
                                content = {
                                    Text(text = "Go to deeplink")
                                }
                            )
                        }
                    }
                    composable(
                        "detail",
                        deepLinks = listOf(
                            navDeepLink {
                                uriPattern = "https://www.example.com/detail/{id}"
                                action = Intent.ACTION_VIEW

                            }
                        ),
                        arguments = listOf(
                            navArgument("id") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    )
                    { entry ->
                        val id = entry.arguments?.getInt("id")
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Detail Screen with this id: $id")
                        }
                    }
                    composable("deeplink") {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            //for opening valid links
//                            Button(onClick = {
//                                val intent = Intent(
//                                    Intent.ACTION_VIEW,
//                                    Uri.parse("https://example.com/detail/42")
//                                )
//                                val pendingIntent =
//                                    TaskStackBuilder.create(applicationContext).run {
//                                        addNextIntentWithParentStack(intent)
//                                        getPendingIntent(
//                                            0,
//                                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//                                        )
//                                    }
//                                pendingIntent.send()
//
//                            })
                            // for opening local links withing the app
                            Button(onClick = {
                                val uri = Uri.parse("https://www.example.com/detail/42")
                                val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                                    // This forces the intent to stay inside your app
                                    `package` = context.packageName
                                }

                                // Now you can just start the activity directly
                                context.startActivity(intent)
                            })
                            {
                                Text("deeplink")
                            }
                        }
                    }
                }
            }
        }
    }
}
