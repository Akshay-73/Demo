package com.io.app.demo.callback

interface ApiListener {
        fun isProgressShowing(show:Boolean)
        fun msg(msg:String)

}