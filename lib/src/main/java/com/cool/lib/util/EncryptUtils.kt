package com.cool.lib.util

import android.util.Base64
import java.io.ByteArrayOutputStream
import java.security.Key
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

/**
 * 加密方法
 */
object EncryptUtils {
    /**
     * RSA算法
     */
    const val RSA = "RSA"
    /**加密方式，android的 */
    // public static final String TRANSFORMATION = "RSA/None/NoPadding";
    /**
     * 加密方式，标准jdk的
     */
    const val TRANSFORMATION = "RSA/None/PKCS1Padding"

    /**
     * 公钥加密
     */
    fun encryptByPublicKey(data: String, key: String?): String {
        println(key)
        val keyBytes = Base64.decode(key, Base64.DEFAULT)
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        val pubKey = keyFactory.generatePublic(keySpec)
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, pubKey)
        val databytes = data.toByteArray()
        val inputLen = databytes.size
        val out = ByteArrayOutputStream()
        var offSet = 0
        var cache: ByteArray
        var i = 0
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            cache = if (inputLen - offSet > 116) {
                cipher.doFinal(databytes, offSet, 116)
            } else {
                cipher.doFinal(databytes, offSet, inputLen - offSet)
            }
            out.write(cache, 0, cache.size)
            i++
            offSet = i * 116
        }
        val encryptedData = out.toByteArray()
        try {
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        var pwd = Base64.encodeToString(encryptedData, Base64.DEFAULT)
        pwd = pwd.replace("\n", "")
        return pwd
    }

    /**
     * @param encryptedData encryptedData
     * @param privateKey privateKey
     * @return
     * @throws Exception 使用私钥解密
     */
    fun decryptByPrivateKey(encryptedData: ByteArray, privateKey: String?): String {
        val keyBytes = Base64.decode(privateKey, Base64.DEFAULT)
        val pkcs8KeySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        val privateK: Key = keyFactory.generatePrivate(pkcs8KeySpec)
        val cipher = Cipher.getInstance(keyFactory.algorithm)
        cipher.init(Cipher.DECRYPT_MODE, privateK)
        val inputLen = encryptedData.size
        val out = ByteArrayOutputStream()
        var offSet = 0
        var cache: ByteArray
        var i = 0
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            cache = if (inputLen - offSet > 256) {
                cipher.doFinal(encryptedData, offSet, 256)
            } else {
                cipher.doFinal(encryptedData, offSet, inputLen - offSet)
            }
            out.write(cache, 0, cache.size)
            i++
            offSet = i * 256
        }
        val decryptedData = out.toByteArray()
        out.close()
        return String(decryptedData)
    }
}