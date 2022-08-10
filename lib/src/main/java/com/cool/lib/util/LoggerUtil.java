package com.cool.lib.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orhanobut.logger.LogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.Printer;

public class LoggerUtil {
    public static final String TAG = LoggerUtil.class.getSimpleName();

    public static final int VERBOSE = Logger.VERBOSE;
    public static final int DEBUG = Logger.DEBUG;
    public static final int INFO = Logger.INFO;
    public static final int WARN = Logger.WARN;
    public static final int ERROR = Logger.ERROR;
    public static final int ASSERT = Logger.ASSERT;

    private LoggerUtil() {
        //no instance
    }

    private static boolean noLog() {
//        return !BuildConfig.DEBUG;
        return false;
    }

    public static void printer(@NonNull Printer printer) {
        Logger.printer(printer);
    }

    public static void addLogAdapter(@NonNull LogAdapter adapter) {
        Logger.addLogAdapter(adapter);
    }

    public static void clearLogAdapters() {
        Logger.clearLogAdapters();
    }

    /**
     * Given tag will be used as tag only once for this method call regardless of the tag that's been
     * set during initialization. After this invocation, the general tag that's been set will
     * be used for the subsequent log calls
     */
    public static Printer t(@Nullable String tag) {
        return Logger.t(tag);
    }

    /**
     * General log function that accepts all configurations as parameter
     */
    public static void log(int priority, @Nullable String tag, @Nullable String message, @Nullable Throwable throwable) {
        if (noLog()) {
            return;
        }

        Logger.log(priority, tag, message, throwable);
    }

    public static void d(@NonNull String message, @Nullable Object... args) {
        if (noLog()) {
            return;
        }

        Logger.d(message, args);
    }

    public static void d(@Nullable Object object) {
        if (noLog()) {
            return;
        }

        Logger.d(object);
    }

    public static void e(@NonNull String message, @Nullable Object... args) {
        if (noLog()) {
            return;
        }

        Logger.e(message, args);
    }

    public static void e(@Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        if (noLog()) {
            return;
        }

        Logger.e(throwable, message, args);
    }

    public static void i(@NonNull String message, @Nullable Object... args) {
        if (noLog()) {
            return;
        }

        Logger.i(message, args);
    }

    public static void v(@NonNull String message, @Nullable Object... args) {
        if (noLog()) {
            return;
        }

        Logger.v(message, args);
    }

    public static void w(@NonNull String message, @Nullable Object... args) {
        if (noLog()) {
            return;
        }

        Logger.w(message, args);
    }

    /**
     * Tip: Use this for exceptional situations to log
     * ie: Unexpected errors etc
     */
    public static void wtf(@NonNull String message, @Nullable Object... args) {
        if (noLog()) {
            return;
        }

        Logger.wtf(message, args);
    }

    /**
     * Formats the given json content and print it
     */
    public static void json(@Nullable String json) {
        if (noLog()) {
            return;
        }

        Logger.json(json);
    }

    /**
     * Formats the given xml content and print it
     */
    public static void xml(@Nullable String xml) {
        if (noLog()) {
            return;
        }

        Logger.xml(xml);
    }

    /**
     * 信息太长,分段打印
     */
    public static void showLogSegment(String tag, String msg) {
        if (noLog()) {
            return;
        }

        int max_str_length = 3500 - tag.length();
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        showLog(tag, msg);
    }

    public static void showLog(String msg) {
        showLog(TAG, msg);
    }

    public static void showLog(String tag, String msg) {
        if (noLog()) {
            return;
        }

        com.blankj.utilcode.util.LogUtils.eTag(tag, msg);
    }

    public static void e(Throwable e) {
        if (noLog()) {
            return;
        }

        com.blankj.utilcode.util.LogUtils.e(e);
    }

    public static void eTag(final String tag, final Object... contents) {
        if (noLog()) {
            return;
        }

        com.blankj.utilcode.util.LogUtils.eTag(tag, contents);
    }
}
