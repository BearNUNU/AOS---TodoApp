package com.example.kim3409_todore.security
import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import java.security.KeyStore
import javax.crypto.spec.GCMParameterSpec

class EncryptionHelper(private val context: Context) {

    private val keyAlias = "my_key_alias"
    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    init {
        if (!keyStore.containsAlias(keyAlias)) {
            generateKey()
        }
    }

    private fun generateKey() {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
        )
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    fun encryptData(data: String): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val key = keyStore.getKey(keyAlias, null) as SecretKey
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptionIv = cipher.iv
        val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        // Store the IV and encrypted data
        return encryptionIv + encryptedData
    }

    fun decryptData(encryptedDataWithIv: ByteArray): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val key = keyStore.getKey(keyAlias, null) as SecretKey
        val encryptionIv = encryptedDataWithIv.copyOfRange(0, 12) // Extract the IV
        val encryptedData = encryptedDataWithIv.copyOfRange(12, encryptedDataWithIv.size)
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, encryptionIv))
        val decryptedData = cipher.doFinal(encryptedData)
        return String(decryptedData, Charsets.UTF_8)
    }
}
