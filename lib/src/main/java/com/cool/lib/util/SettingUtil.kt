package com.cool.lib.util

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import com.blankj.utilcode.util.RomUtils
import com.cool.lib.ext.closeSafe
import com.cool.lib.ext.logE
import java.io.BufferedReader
import java.io.InputStreamReader

object SettingUtil {
    /**
     * 前往本应用的权限设置相关页面
     */
    fun gotoSelfPermissionSetting(context: Context) {
        kotlin.runCatching {
            if (RomUtils.isHuawei()) {
                gotoHuaWeiPermissionSetting(context)
            } else if (RomUtils.isMeizu()) {
                gotoMeiZuPermissionSetting(context)
            } else if (RomUtils.isXiaomi()) {
                gotoMiUiPermissionSetting(context)
            } else if (RomUtils.isSony()) {
                gotoSonyMobilePermissionSetting(context)
            } else if (RomUtils.isOppo()) {
                gotoOPPOPermissionSetting(context)
            } else if (RomUtils.isLg()) {
                gotoLGPermissionSetting(context)
            } else if (RomUtils.isLeeco()) {
                gotoLeTVPermissionSetting(context)
            } else if (RomUtils.is360()) {
                goto360PermissionSetting(context)
            } else if (RomUtils.isVivo()) {
                gotoViVoPermissionSetting(context)
            } else if (RomUtils.isCoolpad()) {
                gotoCoolPadPermissionSetting(context)
            } else if (RomUtils.isSamsung()) {
                gotoSelfSetting(context)
            } else {
                gotoSelfSetting(context)
            }
        }.onFailure {
            logE(it)
            gotoSelfSetting(context)
        }
    }

    private fun gotoCoolPadPermissionSetting(context: Context) {
        if (!startApplicationWithPackageName(context, "com.yulong.android.security:remote")) {
            gotoSelfSetting(context)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startApplicationWithPackageName(context: Context, packageName: String): Boolean {
        kotlin.runCatching {
            val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
            val resolveIntent = Intent(Intent.ACTION_MAIN, null)
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            resolveIntent.setPackage(packageInfo.packageName)
            val infoList = context.packageManager.queryIntentActivities(resolveIntent, 0)
            val resolveInfo = infoList.iterator().next()
            if (resolveInfo != null) {
                val pk = resolveInfo.activityInfo.packageName
                val className = resolveInfo.activityInfo.name
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                val componentName = ComponentName(pk, className)
                intent.component = componentName
                context.startActivity(intent)
                return true
            }
        }.onFailure {
            logE(it)
        }
        return false
    }

    private fun gotoViVoPermissionSetting(context: Context) {
        kotlin.runCatching {
            val intent = Intent()
            if (Build.MODEL.contains("Y85") && !Build.MODEL.contains("Y85A") || Build.MODEL.contains("vivo Y53L")) {
                intent.setClassName("com.vivo.permissionmanager",
                    "com.vivo.permissionmanager.activity.PurviewTabActivity")
                intent.putExtra("packagename", context.packageName)
                intent.putExtra("tabId", "1")
            } else {
                intent.setClassName("com.vivo.permissionmanager",
                    "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity")
                intent.action = "secure.intent.action.softPermissionDetail"
                intent.putExtra("packagename", context.packageName)
            }
            context.startActivity(intent)
        }.onFailure {
            logE(it)
            gotoSelfSetting(context)
        }
    }

    private fun gotoHuaWeiPermissionSetting(context: Context) {
        kotlin.runCatching {
            val intent = Intent(context.packageName)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", context.packageName)
            val componentName = ComponentName("com.huawei.systemmanager",
                "com.huawei.permissionmanager.ui.MainActivity")
            intent.component = componentName
            context.startActivity(intent)
        }.onFailure {
            logE(it)
            gotoSelfSetting(context)
        }
    }

    private fun gotoMeiZuPermissionSetting(context: Context) {
        kotlin.runCatching {
            val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", context.packageName)
            context.startActivity(intent)
        }.onFailure {
            logE(it)
            gotoSelfSetting(context)
        }
    }

    private fun gotoMiUiPermissionSetting(context: Context) {
        kotlin.runCatching {
            val rom = miuiVersion()
            val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("extra_pkgname", context.packageName)
            if (TextUtils.equals(rom, "V6") || TextUtils.equals(rom, "V7")) {
                val componentName = ComponentName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity")
                intent.component = componentName
                context.startActivity(intent)
            } else if (TextUtils.equals(rom, "V8") || TextUtils.equals(rom, "V9")) {
                val componentName = ComponentName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.PermissionsEditorActivity")
                intent.component = componentName
                context.startActivity(intent)
            } else {
                gotoSelfSetting(context)
            }
        }.onFailure {
            logE(it)
            gotoSelfSetting(context)
        }
    }

    private fun miuiVersion(): String? {
        val propName = "ro.miui.ui.version.name"
        var line: String? = null
        var input: BufferedReader? = null
        kotlin.runCatching {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input?.readLine()
        }.onFailure {
            logE(it)
        }
        input.closeSafe()
        return line
    }

    private fun gotoSonyMobilePermissionSetting(context: Context) {
        kotlin.runCatching {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", context.packageName)
            val componentName = ComponentName("com.sonymobile.cta",
                "com.sonymobile.cta.SomcCTAMainActivity")
            intent.component = componentName
            context.startActivity(intent)
        }.onFailure {
            logE(it)
            gotoSelfSetting(context)
        }
    }

    private fun gotoOPPOPermissionSetting(context: Context) {
        kotlin.runCatching {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", context.packageName)
            val componentName = ComponentName("com.color.safecenter",
                "com.color.safecenter.permission.PermissionManagerActivity")
            intent.component = componentName
            context.startActivity(intent)
        }.onFailure {
            logE(it)
            gotoSelfSetting(context)
        }
    }

    private fun gotoLGPermissionSetting(context: Context) {
        kotlin.runCatching {
            val intent = Intent("android.intent.action.MAIN")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", context.packageName)
            val componentName = ComponentName("com.android.settings",
                "com.android.settings.Settings\$AccessLockSummaryActivity")
            intent.component = componentName
            context.startActivity(intent)
        }.onFailure {
            logE(it)
            gotoSelfSetting(context)
        }
    }

    private fun gotoLeTVPermissionSetting(context: Context) {
        kotlin.runCatching {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", context.packageName)
            val componentName = ComponentName("com.letv.android.letvsafe",
                "com.letv.android.letvsafe.PermissionAndApps")
            intent.component = componentName
            context.startActivity(intent)
        }.onFailure {
            logE(it)
            gotoSelfSetting(context)
        }
    }

    private fun goto360PermissionSetting(context: Context) {
        kotlin.runCatching {
            val intent = Intent("android.intent.action.MAIN")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", context.packageName)
            val componentName = ComponentName("com.qihoo360.mobilesafe",
                "com.qihoo360.mobilesafe.ui.index.AppEnterActivity")
            intent.component = componentName
            context.startActivity(intent)
        }.onFailure {
            logE(it)
            gotoSelfSetting(context)
        }
    }

    /**
     * 前往本应用的设置页面
     */
    @SuppressLint("ObsoleteSdkInt")
    fun gotoSelfSetting(context: Context) {
        kotlin.runCatching {
            val intent = Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (Build.VERSION.SDK_INT >= 9) {
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = Uri.fromParts("package", context.packageName, null)
            } else {
                intent.action = Intent.ACTION_VIEW
                intent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails")
                intent.putExtra("com.android.settings.ApplicationPkgName", context.packageName)
            }
            context.startActivity(intent)
        }.onFailure {
            logE(it)
            gotoSetting(context)
        }
    }

    /**
     * 前往系统设置页面
     */
    fun gotoSetting(context: Context) {
        kotlin.runCatching {
            context.startActivity(Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }.onFailure {
            logE(it)
        }
    }
}