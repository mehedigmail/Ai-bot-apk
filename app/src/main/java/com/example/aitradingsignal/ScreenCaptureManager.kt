
package com.example.aitradingsignal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import androidx.core.content.ContextCompat

object ScreenCaptureManager {

    fun createCaptureIntent(context: Context): Intent {
        val manager = context.getSystemService(
            Context.MEDIA_PROJECTION_SERVICE
        ) as MediaProjectionManager

        return manager.createScreenCaptureIntent()
    }

    fun startCapture(
        context: Context,
        resultCode: Int,
        resultData: Intent
    ) {
        val serviceIntent = Intent(
            context,
            CaptureService::class.java
        ).apply {
            action = CaptureService.ACTION_START
            putExtra(
                CaptureService.EXTRA_RESULT_CODE,
                resultCode
            )
            putExtra(
                CaptureService.EXTRA_RESULT_DATA,
                resultData
            )
        }

        ContextCompat.startForegroundService(
            context,
            serviceIntent
        )
    }

    fun stopCapture(context: Context) {
        val serviceIntent = Intent(
            context,
            CaptureService::class.java
        ).apply {
            action = CaptureService.ACTION_STOP
        }

        context.startService(serviceIntent)
    }
}

