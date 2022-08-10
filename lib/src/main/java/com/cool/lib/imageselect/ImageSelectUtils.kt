package com.cool.lib.imageselect

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnPermissionDescriptionListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.language.LanguageConfig

fun Activity.createPictureSelector(): PictureSelector {
    return PictureSelector.create(this)
}

fun Fragment.createPictureSelector(): PictureSelector {
    return PictureSelector.create(this)
}

fun View.createPictureSelector(): PictureSelector {
    return PictureSelector.create(this.context)
}

fun Context.createPictureSelector(): PictureSelector {
    return PictureSelector.create(this)
}

/**
 * 单独拍照
 */
fun PictureSelector.takeImage(chooseMode: Int = SelectMimeType.TYPE_IMAGE, callback: ((result: ArrayList<LocalMedia>?, isCancel: Boolean) -> Unit)?) {
    val cameraModel = this.openCamera(chooseMode)
        .setCameraInterceptListener { _, _, _ -> }
        .setRecordAudioInterceptListener { _, _ -> }
        .setCropEngine { _, _, _, _, _ -> }
        .setCompressEngine(CompressFileEngine { _, _, _ -> })
        .setAddBitmapWatermarkListener { _, _, _, _ -> }
        .setVideoThumbnailListener { _, _, _ -> }
        .setLanguage(LanguageConfig.UNKNOWN_LANGUAGE)
        .setSandboxFileEngine { _, _, _, _ -> }
        .isOriginalControl(false)
        .setPermissionDescriptionListener(object : OnPermissionDescriptionListener {
            override fun onPermissionDescription(fragment: Fragment?, permissionArray: Array<out String>?) {
            }

            override fun onDismiss(fragment: Fragment?) {
            }
        })
        .setOutputAudioDir("")
        .setSelectedData(null)

    cameraModel.forResult(object : OnResultCallbackListener<LocalMedia> {
        override fun onResult(result: ArrayList<LocalMedia>?) {
            callback?.invoke(result, false)
        }

        override fun onCancel() {
            callback?.invoke(null, true)
        }
    })
}