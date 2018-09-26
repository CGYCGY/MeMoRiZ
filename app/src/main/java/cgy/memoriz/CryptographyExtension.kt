package cgy.memoriz

import android.util.Base64
import android.util.Log
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

val secureRandom = SecureRandom()

fun generateIV() : ByteArray {
    val iv = ByteArray(12)
    secureRandom.nextBytes(iv)
    return iv
}

fun generateKey() : ByteArray {
    val key = ByteArray(16)
    secureRandom.nextBytes(key)
    return key
}

fun String.encryptPass() : String {
//    Log.d("string", this)
    val key = Base64.decode("v7Hfvbf7h8jka3xpVtL7Mw==", Base64.DEFAULT)

//    Log.d("key", Base64.encodeToString(key, Base64.DEFAULT))

    val iv = generateIV()

//    Log.d("iv", Base64.encodeToString(iv, Base64.DEFAULT))

    val secretKey = SecretKeySpec(key, "AES")
    val paramSpec = GCMParameterSpec(128, iv)
    val encrypt = Cipher.getInstance("AES/GCM/NoPadding")
    encrypt.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec)

    val tempPass = encrypt.doFinal(this.toByteArray(Charsets.UTF_8))
    val byteBuffer = ByteBuffer.allocate(1 + iv.size + tempPass.size)
    Log.d("iv size", iv.size.toString())
    Log.d("tempPass size", tempPass.size.toString())
    byteBuffer.put(iv.size.toByte())
    byteBuffer.put(iv)
    byteBuffer.put(tempPass)
    val encryptedPass = byteBuffer.array()

    Arrays.fill(key, 0.toByte())

//    Log.d("before encode size", encryptedPass.size.toString())
//    Log.d("encrypted string", Base64.encodeToString(encryptedPass, Base64.DEFAULT))
    return Base64.encodeToString(encryptedPass, Base64.DEFAULT)
}

fun String.decryptPass() : String? {
//    Log.d("before", this)

    val byteThis = Base64.decode(this, Base64.DEFAULT)

//    Log.d("after decode size", byteThis.size.toString())

    val byteBuffer = ByteBuffer.wrap(byteThis)
//    Log.d("byteBuffer size", byteBuffer.limit().toString())

    val ivSize =
            if (byteBuffer.limit() <= 0) 0
            else byteBuffer.get().toInt()

//    if (ivSize < 12 || ivSize >= 16) throw IllegalArgumentException("invalid iv length")
    if (ivSize in 12..15) {
//        Log.d("byteBuffer position", byteBuffer.position().toString())
        val iv = ByteArray(ivSize)
        byteBuffer.get(iv)

//        Log.d("iv", Base64.encodeToString(iv, Base64.DEFAULT))

//        Log.d("byteBuffer position", byteBuffer.position().toString())

        val tempPass = ByteArray(byteBuffer.remaining())
        byteBuffer.get(tempPass)

//        Log.d("byteBuffer position", byteBuffer.position().toString())

        val key = Base64.decode("v7Hfvbf7h8jka3xpVtL7Mw==", Base64.DEFAULT)

        val decrypt = Cipher.getInstance("AES/GCM/NoPadding")
        decrypt.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), GCMParameterSpec(128, iv))
        val decryptedPass = decrypt.doFinal(tempPass)

//        Log.d("decrypted string", String(decryptedPass))
        return String(decryptedPass)
    } else {

        return null
    }
}

