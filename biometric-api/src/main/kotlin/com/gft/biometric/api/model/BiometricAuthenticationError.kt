package com.gft.biometric.api.model

sealed class BiometricAuthenticationError : RuntimeException() {
    object BiometricSensorUnavailable : BiometricAuthenticationError()
    object BiometricSensorLockedTemporarily : BiometricAuthenticationError()
    object BiometricSensorLockedPermanently : BiometricAuthenticationError()
    object AuthenticationCancelledByUser : BiometricAuthenticationError()
    object AuthenticationTimeout : BiometricAuthenticationError()
    object AuthenticationAborted : BiometricAuthenticationError()
    object BiometricNotEnrolled : BiometricAuthenticationError()
    object DeviceCredentialsNotSet : BiometricAuthenticationError()
    object UserNotAuthenticated : BiometricAuthenticationError()
    object UnknownBiometricAuthenticationError : BiometricAuthenticationError()
}
