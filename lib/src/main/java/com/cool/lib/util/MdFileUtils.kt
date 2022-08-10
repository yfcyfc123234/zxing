package com.cool.lib.util

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.MediaScannerConnectionClient
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.blankj.utilcode.util.FileUtils
import com.orhanobut.logger.Logger
import java.io.File
import java.io.FileNotFoundException
import java.util.*

/**
 * anthor:njb
 * date: 2020-04-21 04:36
 * desc: 文件保存工具类
 */
class MdFileUtils {
    private var mMediaScanner: MediaScannerConnection? = null
    fun refreshAlbum(context: Context?, fileAbsolutePath: String?, isVideo: Boolean) {
        //                Log.i(TAG, " 扫描完成 path: ", path, " uri: ", uri);
        mMediaScanner = MediaScannerConnection(context, object : MediaScannerConnectionClient {
            override fun onMediaScannerConnected() {
                if (mMediaScanner!!.isConnected) {
//                    if (isVideo) {
//                        mMediaScanner.scanFile(fileAbsolutePath, "video/mp4");
//                    } else {
                    mMediaScanner!!.scanFile(fileAbsolutePath, "image/jpeg")
                    //                    }
                } else {
//                    Log.e(TAG, " refreshAlbum() 无法更新图库，未连接，广播通知更新图库，异常情况下 ");
                }
            }

            override fun onScanCompleted(path: String, uri: Uri) {
//                Log.i(TAG, " 扫描完成 path: ", path, " uri: ", uri);
            }
        })
        mMediaScanner!!.connect()
    }

    companion object {
        /**
         * 保存图片
         *
         * @param context context
         * @param file    file
         */
        @Throws(FileNotFoundException::class)
        fun saveImage(context: Context, file: File) {
//        ContentResolver localContentResolver = context.getContentResolver();
//        ContentValues localContentValues = getImageContentValues(context, file, System.currentTimeMillis());
//        localContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, localContentValues);
//
//        Intent localIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
//        final Uri localUri = Uri.fromFile(file);
//        localIntent.setData(localUri);
//        context.sendBroadcast(localIntent);
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri));
//        String path = file.getPath();

//        LoggerUtil.d("获取添加员工二维码 == >path", path);
//        MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), path, null);
            if (Build.BRAND.uppercase(Locale.getDefault()) == "HUAWEI") {
                try {
                    MediaStore.Images.Media.insertImage(context.contentResolver, file.absolutePath, file.name, null)
                    Logger.d("华为--刷新相册")
                    FileUtils.delete(file.absolutePath)
                } catch (ignored: Exception) {
                }
            } else {
                context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
                try {
                    MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath),
                        null
                    ) { path: String?, uri: Uri? -> }
                } catch (ignored: Exception) {
                }
            }

//先保存到文件
//        OutputStream outputStream;
//        //再更新图库
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            ContentResolver localContentResolver = context.getContentResolver();
//            ContentValues localContentValues = getImageContentValues(context, file, System.currentTimeMillis());
//            localContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, localContentValues);
//            Uri uri = localContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  localContentValues);
//            if (uri == null) {
//                return;
//            }
//            try {
//                outputStream = localContentResolver.openOutputStream(uri);
//                FileInputStream fileInputStream = new FileInputStream(file);
//                FileUtils.copyFile(fileInputStream, outputStream);
//                fileInputStream.close();
//                outputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            MediaScannerConnection.scanFile(
//                    context,
//                    new String[]{file.getAbsolutePath()},
//                    new String[]{"image/jpeg"},
//                    (path, uri) -> {
//                        // Scan Completed
//                    });
//        }
        }

        @SuppressLint("InlinedApi")
        fun getImageContentValues(paramContext: Context?, paramFile: File, paramLong: Long): ContentValues {
            val localContentValues = ContentValues()
            localContentValues.put("title", paramFile.name)
            localContentValues.put("_display_name", paramFile.name)
            localContentValues.put("mime_type", "image/jpeg")
            localContentValues.put("datetaken", paramLong)
            localContentValues.put("date_modified", paramLong)
            localContentValues.put("date_added", paramLong)
            localContentValues.put("orientation", 0)
            localContentValues.put("_data", paramFile.absolutePath)
            localContentValues.put("_size", paramFile.length())
            localContentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, paramFile.name)
            localContentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            localContentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            return localContentValues
        }

        /**
         * 保存视频
         *
         * @param context context
         * @param file    file
         */
        fun saveVideo(context: Context, file: File) {
            //是否添加到相册
            val localContentResolver = context.contentResolver
            val localContentValues = getVideoContentValues(context, file, System.currentTimeMillis())
            val localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues)
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri))
        }

        fun getVideoContentValues(paramContext: Context?, paramFile: File, paramLong: Long): ContentValues {
            val localContentValues = ContentValues()
            localContentValues.put("title", paramFile.name)
            localContentValues.put("_display_name", paramFile.name)
            localContentValues.put("mime_type", "video/mp4")
            localContentValues.put("datetaken", paramLong)
            localContentValues.put("date_modified", paramLong)
            localContentValues.put("date_added", paramLong)
            localContentValues.put("_data", paramFile.absolutePath)
            localContentValues.put("_size", paramFile.length())
            return localContentValues
        }
    }
}