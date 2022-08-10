package com.cool.lib.util

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import com.cool.lib.ext.logE
import java.io.File

/**
 * Describe：文件帮助类
 */
open class FileUtils {
    companion object {
        val APPS_ROOT_DIR = externalStorePath //+ File.separator + MyApp.getApplication().getPackageName();
        val IMAGE_PATH = APPS_ROOT_DIR + "/Image"
        val TEMP_PATH = APPS_ROOT_DIR + "/Temp"
        val APP_CRASH_PATH = APPS_ROOT_DIR + "/AppCrash"
        val FILE_PATH = APPS_ROOT_DIR + "/AssKicker"

        /**
         * 外置存储卡的路径
         *
         * @return String
         */
        val externalStorePath: String?
            get() = if (isExistExternalStore) {
                Environment.getExternalStorageDirectory().absolutePath
            } else {
                null
            }

        fun getExternalFilesDir(context: Context): String {
            return if (isExistExternalStore) {
                context.getExternalFilesDir(null)!!.path
            } else {
                ""
            }
        }

        fun getFileDirPath(context: Context): File? {
            return create(getExternalFilesDir(context) + "/QU_FANG_APP")
        }

        fun getFileDirPathOne(context: Context): File? {
            return create(getExternalFilesDir(context))
        }

        fun extractFileNameFromURL(url: String): String {
            return url.substring(url.lastIndexOf('/') + 1)
        }

        /**
         * 是否有外存卡
         *
         * @return boolean
         */
        val isExistExternalStore: Boolean
            get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

        private fun create(path: String): File? {
            if (!isExistExternalStore) {
                logE("储存卡已拔出")
                return null
            }
            val directory = File(path)
            if (!directory.exists()) {
                directory.mkdir()
            }
            return directory
        }

        /**
         * 返回图片存放目录
         *
         * @return File
         */
        val imagePath: File?
            get() = create(IMAGE_PATH)

        /**
         * 返回临时存放目录
         *
         * @return File
         */
        val tempPath: File?
            get() = create(TEMP_PATH)

        /**
         * 存储日志文件目录
         *
         * @return File
         */
        val appCrashPath: File?
            get() = create(APP_CRASH_PATH)

        /**
         * 存储文件目录
         *
         * @return File
         */
        val filePath: File?
            get() = create(FILE_PATH)

        /**
         * 7.0以上拍照 安装应用等文件问题
         *
         * @param context context
         * @param file    file
         * @return Uri
         */
        fun getUriForFile(context: Context, file: File?): Uri {
            val fileUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(context, context.packageName + ".provider", file!!)
            } else {
                Uri.fromFile(file)
            }
            return fileUri
        }

        /**
         * 根据文件后缀获取文件MIME类型
         *
         * @param filePath
         * @return
         */
        fun getMimeType(filePath: String?): String? {
            val retriever = MediaMetadataRetriever()
            var mimeType: String? = "*/*"
            if (filePath != null) {
                mimeType = try {
                    retriever.setDataSource(filePath)
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
                } catch (e: RuntimeException) {
                    return mimeType
                }
            }
            return mimeType
        }

        fun getVideoThumbnail(url: String?): Bitmap? {
            var bitmap: Bitmap? = null
            //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
            //的接口，用于从输入的媒体文件中取得帧和元数据；
            val retriever = MediaMetadataRetriever()
            try {
                //根据文件路径获取缩略图
                retriever.setDataSource(url, hashMapOf())
                //获得第一帧图片
                bitmap = retriever.frameAtTime
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } finally {
                retriever.release()
            }
            return bitmap
        }
    }
}