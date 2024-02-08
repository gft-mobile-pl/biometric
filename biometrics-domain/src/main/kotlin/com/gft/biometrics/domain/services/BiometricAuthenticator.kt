package com.gft.biometrics.domain.services

import com.gft.biometrics.domain.model.AuthenticationStrength
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
