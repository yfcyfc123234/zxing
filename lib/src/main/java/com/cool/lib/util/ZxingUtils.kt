package com.cool.lib.util

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.nio.charset.StandardCharsets

/**
 * 二维码生成工具类
 */
object ZxingUtils {
    /**
     * 生成二维码Bitmap
     *
     * @param content   内容
     * @param widthPix  图片宽度
     * @param heightPix 图片高度
     * 二维码中心的Logo图标（可以为null）
     * @param filePath  用于存储二维码图片的文件路径
     * @return 生成二维码及保存文件是否成功
     */
    fun createQRImage(content: String?, widthPix: Int, heightPix: Int, filePath: String?): Bitmap? {
        try {
            if (content == null || "" == content) {
                return null
            }
            // 配置参数
            val hints: MutableMap<EncodeHintType, Any?> = mutableMapOf()
            hints[EncodeHintType.CHARACTER_SET] = StandardCharsets.UTF_8
            // 容错级别
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            // 设置空白边距的宽度
            hints[EncodeHintType.MARGIN] = 0 //default is 4

            // 图像数据转换，使用了矩阵转换
            val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints)
            val pixels = IntArray(widthPix * heightPix)
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (y in 0 until heightPix) {
                for (x in 0 until widthPix) {
                    if (bitMatrix[x, y]) {
                        pixels[y * widthPix + x] = -0x1000000
                    } else {
                        pixels[y * widthPix + x] = -0x1
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            val bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix)
            return bitmap
            /*if (filePath == null) {
                return bitmap;
            }
            // 必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            if (bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath)))
                return BitmapFactory.decodeFile(filePath);*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}