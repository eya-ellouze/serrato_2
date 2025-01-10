package com.serrato
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.serrato.ui.theme.SerratoTheme


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SerratoTopAppBar(
    title: String? = null,
    returnAction: () -> Unit = {},
    activity: ComponentActivity // Pass the activity for intent handling
) {
    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary,
    )

    val actions: @Composable RowScope.() -> Unit = {
        // Action 1: Open GreenHouseListActivity
        IconButton(onClick = {
            val intent = Intent(activity, GreenHouseListActivity::class.java)
            activity.startActivity(intent)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_greenhouse),
                contentDescription = stringResource(R.string.app_go_greenhouse_description)
            )
        }

        // Action 2: Send an email
        IconButton(onClick = {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:your_email@example.com")
            }
            activity.startActivity(intent)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_mail),
                contentDescription = stringResource(R.string.app_go_mail_description)
            )
        }

        // Action 3: Open GitHub page
        IconButton(onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/your-profile"))
            activity.startActivity(intent)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_github),
                contentDescription = stringResource(R.string.app_go_github_description)
            )
        }
    }

    if (title == null) {
        TopAppBar(
            title = { Text("") },
            colors = colors,
            actions = actions
        )
    } else {
        MediumTopAppBar(
            title = { Text(title) },
            colors = colors,
            navigationIcon = {
                IconButton(onClick = returnAction) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.app_go_back_description)
                    )
                }
            },
            actions = actions
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SerratoTopAppBarHomePreview() {
    val context = LocalContext.current
    SerratoTheme {
        SerratoTopAppBar(
            title = null,
            activity = context as ComponentActivity // Cast context to ComponentActivity
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SerratoTopAppBarPreview() {
    val context = LocalContext.current
    SerratoTheme {
        SerratoTopAppBar(
            title = "A page",
            activity = context as ComponentActivity // Cast context to ComponentActivity
        )
    }
}