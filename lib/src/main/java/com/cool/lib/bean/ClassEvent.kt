package com.cool.lib.bean

import java.io.Serializable

open class ClassEvent : Serializable {
    var className: String
    var type: Int
    var what = 0
    var arg1 = 0
    var arg2 = 0
    var obj: Any? = null

    override fun toString(): String {
        return "ClassEvent{" +
                "className='" + className + '\'' +
                ", type=" + type +
                ", what=" + what +
                ", arg1=" + arg1 +
                ", arg2=" + arg2 +
                ", obj=" + obj +
                '}'
    }

    constructor(className: String, type: Int) {
        this.className = className
        this.type = type
    }

    constructor(className: String, type: Int, what: Int) {
        this.className = className
        this.type = type
        this.what = what
    }

    constructor(className: String, type: Int, what: Int, arg1: Int, arg2: Int) {
        this.className = className
        this.type = type
        this.what = what
        this.arg1 = arg1
        this.arg2 = arg2
    }

    constructor(className: String, type: Int, what: Int, arg1: Int, arg2: Int, obj: Any?) {
        this.className = className
        this.type = type
        this.what = what
        this.arg1 = arg1
        this.arg2 = arg2
        this.obj = obj
    }

    constructor(className: String, what: Int, obj: Any?) {
        this.className = className
        type = TYPE_OTHER
        this.what = what
        this.obj = obj
    }

    constructor(className: String, obj: Any?) {
        this.className = className
        type = TYPE_OTHER
        this.obj = obj
    }

    companion object {
        private const val serialVersionUID = -8557898657903905452L
        const val TYPE_FINISH = 0
        const val TYPE_REFRESH = 1
        const val TYPE_FINISH_ALL = 2
        const val TYPE_OTHER = 9999
    }
}