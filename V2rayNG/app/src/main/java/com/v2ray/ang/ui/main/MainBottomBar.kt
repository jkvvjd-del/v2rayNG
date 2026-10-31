package com.v2ray.ang.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.v2ray.ang.R

@Composable
fun MainPowerSection(
    displayText: String,
    isRunning: Boolean,
    onAction: (MainAction) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val accent = colors.primary
    val haloColor = accent.copy(alpha = if (isRunning) 0.20f else 0.08f)
    val iconTint = if (isRunning) colors.onPrimary else accent
    val fillBrush = if (isRunning) {
        Brush.verticalGradient(listOf(accent, accent.copy(alpha = 0.7f)))
    } else {
        Brush.verticalGradient(listOf(colors.primaryContainer, colors.surfaceContainerHigh))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(128.dp)
                .background(haloColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(fillBrush, CircleShape)
                    .border(3.dp, accent, CircleShape)
                    .clip(CircleShape)
                    .clickable { onAction(MainAction.ToggleService) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_power_24dp),
                    contentDescription = stringResource(
                        if (isRunning) R.string.acc_stop else R.string.acc_start
                    ),
                    tint = iconTint,
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        Text(
            text = displayText,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onAction(MainAction.TestCurrentServer) }
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .semantics { contentDescription = displayText }
        )
    }
}

@Composable
fun MainBottomBar(
    displayText: String,
    isRunning: Boolean,
    isDarkTheme: Boolean,
    onAction: (MainAction) -> Unit
) {
}
