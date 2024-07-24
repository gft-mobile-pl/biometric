package com.gft.biometric.api.services

import com.gft.biometric.api.model.AuthenticationStrength
import javax.crypto.Cipher

interface BiometricAuthenticator {

    suspend fun authenticate(authenticationStrength: AuthenticationStrength, promptParams: AuthenticationPromptParams)

    suspend fun authenticate(cipher: Cipher, promptParams: AuthenticationPromptParams)

    data class AuthenticationPromptParams(
        val title: String,
        val negativeButtonLabel: String? = null,
        val subtitle: String? = null,
        val description: String? = null
    )
}
