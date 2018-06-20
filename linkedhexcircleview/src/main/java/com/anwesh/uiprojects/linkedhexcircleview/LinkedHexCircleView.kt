package com.anwesh.uiprojects.linkedhexcircleview

/**
 * Created by anweshmishra on 20/06/18.
 */

import android.view.MotionEvent
import android.view.View
import android.content.Context
import android.graphics.*

class LinkedHexCircleView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        return true
    }
}