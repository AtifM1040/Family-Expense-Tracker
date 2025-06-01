package com.familyexpense.tracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familyexpense.tracker.ui.theme.GradientGreen
import com.familyexpense.tracker.ui.theme.GradientRed

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPositive: Boolean = true,
    enabled: Boolean = true
) {
    val gradient = if (isPositive) {
        Brush.horizontalGradient(
            colors = listOf(GradientGreen, GradientGreen.copy(alpha = 0.8f))
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(GradientRed, GradientRed.copy(alpha = 0.8f))
        )
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (enabled) gradient else Brush.horizontalGradient(
                colors = listOf(Color.Gray, Color.Gray)
            )),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}