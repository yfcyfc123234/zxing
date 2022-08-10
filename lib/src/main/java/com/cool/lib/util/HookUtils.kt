package com.cool.lib.util

import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import com.cool.lib.ext.logD
import com.cool.lib.ext.logE
import com.cool.lib.ext.logI
import java.lang.reflect.Method
import java.lang.reflect.Proxy

object HookUtils {
    private var cacheWifiInfo: WifiInfo? = null

    fun hookMacAddress(tag: String?, context: Context) {
        kotlin.runCatching {
            val iWifiManager = Class.forName("android.net.wifi.IWifiManager")
            val serviceField = WifiManager::class.java.getDeclaredField("mService")
            serviceField.isAccessible = true
            val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            // real mService
            val realIwm = serviceField[wifi]
            // replace mService  with Proxy.newProxyInstance
            serviceField[wifi] = Proxy.newProxyInstance(iWifiManager.classLoader, arrayOf(iWifiManager),
                InvocationHandler(tag, "getConnectionInfo", realIwm))
            logI(tag!!, "wifiManager hook success")
        }.onFailure {
            logE(tag!!, "printStackTrace:" + it.message)
        }
    }

    class InvocationHandler(private val tag: String?, private val methodName: String, private val real: Any) : java.lang.reflect.InvocationHandler {
        override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
            kotlin.runCatching {
                logD(tag!!, "method invoke " + method.name)
                return if (methodName == method.name) {
                    if (cacheWifiInfo != null) {
                        logD(tag, "cacheWifiInfo:$cacheWifiInfo")
                        return cacheWifiInfo!!
                    }
                    var wifiInfo: WifiInfo? = null
                    kotlin.runCatching {
                        val cls: Class<*> = WifiInfo::class.java
                        wifiInfo = cls.newInstance() as WifiInfo
                        val mMacAddressField = cls.getDeclaredField("mMacAddress")
                        mMacAddressField.isAccessible = true
                        mMacAddressField[wifiInfo] = ""
                        cacheWifiInfo = wifiInfo
                        logD(tag, "wifiInfo:$wifiInfo")
                    }.onFailure {
                        logE(tag, "WifiInfo error:" + it.message)
                    }
                    wifiInfo!!
                } else {
                    method.invoke(real, *args)
                }
            }.onFailure {
                logE(it)
            }
            return null
        }
    }
}