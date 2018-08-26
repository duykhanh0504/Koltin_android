package com.tornado.nhanhnhuchop.utils

import android.content.Context
import android.content.res.Resources
import kotlinx.android.synthetic.main.activity_main.*

val PREFS_FILENAME = "prefs"
val PREFS_ID = "id"

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()