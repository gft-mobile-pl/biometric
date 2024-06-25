package com.gft.biometrics.framework.services

import androidx.biometric.BiometricManager
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.gft.biometrics.domain.model.AuthenticationStrength
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class AndroidXBiometricAuthenticationStatusTest {

    private val biometricManager = mockk<BiometricManager>()

    private val authStatus = AndroidXBiometricAuthenticationStatus(biometricManager)

    @ParameterizedTest(name = "givenStatus = {0}, expecting = {1}")
    @MethodSource("isBiometricAuthenticationSupportedMapping")
    fun `should return correct boolean based on Biometrics availability given BiometricManager status`(
        givenStatus: Int,
        expectedResult: Boolean,
    ) {
        // given
        every { biometricManager.canAuthenticate(any()) } returns givenStatus

        // when
        val actual = authStatus.isBiometricAuthenticationSupported(AuthenticationStrength.STRONG)

        // then
        assertThat(actual).isEqualTo(expectedResult)
    }

    @ParameterizedTest(name = "givenStatus = {0}, expecting = {1}")
    @MethodSource("isBiometricAuthenticationUsableMapping")
    fun `should return correct boolean based on Biometrics usability given BiometricManager status `(
        givenStatus: Int,
        expectedResult: Boolean,
    ) {
        // given
        every { biometricManager.canAuthenticate(any()) } returns givenStatus

        // when
        val actual = authStatus.isBiometricAuthenticationUsable(AuthenticationStrength.STRONG)

        // then
        assertThat(actual).isEqualTo(expectedResult)
    }

    @ParameterizedTest(name = "givenStatus = {0}, expecting = {1}")
    @MethodSource("areBiometricCredentialsEnrolledMapping")
    fun `should return correct boolean based on Credentials enrollment given BiometricManager status`(
        givenStatus: Int,
        expectedResult: Boolean,
    ) {
        // given
        every { biometricManager.canAuthenticate(any()) } returns givenStatus

        // when
        val actual = authStatus.areBiometricCredentialsEnrolled(AuthenticationStrength.STRONG)

        // then
        assertThat(actual).isEqualTo(expectedResult)
    }

    companion object {

        @JvmStatic
        fun isBiometricAuthenticationSupportedMapping() = listOf(
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_SUCCESS,
                // then
                true
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_STATUS_UNKNOWN,
                // then
                false
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED,
                // then
                false
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
                // then
                true
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED,
                // then
                true
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
                // then
                false
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED,
                // then
                true
            )
        )

        @JvmStatic
        fun isBiometricAuthenticationUsableMapping() = listOf(
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_SUCCESS,
                // then
                true
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_STATUS_UNKNOWN,
                // then
                false
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED,
                // then
                false
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
                // then
                false
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED,
                // then
                false
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
                // then
                false
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED,
                // then
                false
            )
        )

        @JvmStatic
        fun areBiometricCredentialsEnrolledMapping() = listOf(
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_SUCCESS,
                // then
                true
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_STATUS_UNKNOWN,
                // then
                false
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED,
                // then
                false
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
                // then
                true
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED,
                // then
                false
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
                // then
                false
            ),
            Arguments.of(
                // given
                BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED,
                // then
                true
            )
        )
    }
}
