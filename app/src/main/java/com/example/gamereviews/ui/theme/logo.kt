
package com.example.gamereviews

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun Logo() {
    Image(
        painter = painterResource(id = R.drawable.logo), // Replace with your logo resource
        contentDescription = "Logo",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(40.dp) // Adjust this to your logo's height
    )
}
