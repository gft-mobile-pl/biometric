package com.gft.biometric.android.services


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.CryptoObject
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gft.biometric.api.model.AuthenticationStrength
import com.gft.biometric.api.services.BiometricAuthenticator
import com.gft.biometric.android.utils.toAuthenticator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.crypto.Cipher

class AndroidXBiometricAuthenticator(
    private val applicationContext: Context,
) : BiometricAuthenticator {

    override suspend fun authenticate(
        authenticationStrength: AuthenticationStrength,
        promptParams: BiometricAuthenticator.AuthenticationPromptParams
    ) {
        authenticate(
            createPromptInfo(authenticationStrength, promptParams),
            null
        )
    }

    override suspend fun authenticate(
        cipher: Cipher,
        promptParams: BiometricAuthenticator.AuthenticationPromptParams
    ) {
        authenticate(
            createPromptInfo(AuthenticationStrength.STRONG, promptParams),
            CryptoObject(cipher)
        )
    }

    private fun createPromptInfo(
        authenticationStrength: AuthenticationStrength,
        promptParams: BiometricAuthenticator.AuthenticationPromptParams
    ): PromptInfo {
        if (authenticationStrength != AuthenticationStrength.DEVICE_CREDENTIAL && promptParams.negativeButtonLabel == null) {
            throw IllegalArgumentException("Negative button label for authentication strength $authenticationStrength is required.")
        }

        return PromptInfo.Builder()
            .setAllowedAuthenticators(authenticationStrength.toAuthenticator())
            .setTitle(promptParams.title)
            .apply {
                promptParams.negativeButtonLabel?.let { negativeButtonLabel -> setNegativeButtonText(negativeButtonLabel) }
                promptParams.subtitle?.let { subtitle -> setSubtitle(subtitle) }
                promptParams.description?.let { description -> setDescription(description) }
            }
            .build()
    }

    private suspend fun authenticate(
        promptInfo: PromptInfo,
        cryptoObject: CryptoObject?
    ) = withContext(Dispatchers.Main) {
        val authenticationActivity = onAuthenticationActivityReady
            .onSubscription {
                startAuthenticationActivity()
            }
            .take(1)
            .first()
        authenticationActivity.authenticate(promptInfo, cryptoObject)

        merge(
            onAuthenticationSuccessful,
            onAuthenticationFailed
                .map { error ->
                    throw error
                })
            .take(1)
            .collect()
    }

    private fun startAuthenticationActivity() {
        val intent = Intent(applicationContext, BiometricAuthenticationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
    }

    internal class BiometricAuthenticationActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    onAuthenticationActivityReady.emit(this@BiometricAuthenticationActivity)
                }
            }
        }

        internal fun authenticate(promptInfo: PromptInfo, cryptoObject: CryptoObject?) {
            val prompt = BiometricPrompt(this, BiometricAuthenticationCallback(
                onAuthenticationSuccessful = {
                    lifecycleScope.launch {
                        onAuthenticationSuccessful.emit(Unit)
                        finish()
                    }
                },
                onAuthenticationFailed = { exception ->
                    lifecycleScope.launch {
                        onAuthenticationFailed.emit(exception)
                        finish()
                    }
                }
            ))
            if (cryptoObject != null) {
                prompt.authenticate(promptInfo, cryptoObject)
            } else {
                prompt.authenticate(promptInfo)
            }
        }
    }

    private companion object {
        private val onAuthenticationSuccessful = MutableSharedFlow<Unit>()
        private val onAuthenticationFailed = MutableSharedFlow<Exception>()
        private val onAuthenticationActivityReady = MutableSharedFlow<BiometricAuthenticationActivity>()
    }
}
