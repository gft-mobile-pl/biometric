package com.gft.biometric.android.utils

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.provider.Settings

class BiometricSettingsIntentFactory {
    companion object {
        @SuppressLint("ObsoleteSdkInt")
        @Suppress("DEPRECATION")
        fun createIntent() = Intent(
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> Settings.ACTION_BIOMETRIC_ENROLL
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> Settings.ACTION_FINGERPRINT_ENROLL
                else -> Settings.ACTION_SECURITY_SETTINGS
            }
        )
    }
}
