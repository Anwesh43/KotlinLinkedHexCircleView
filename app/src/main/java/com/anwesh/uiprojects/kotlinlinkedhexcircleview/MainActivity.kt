package com.anwesh.uiprojects.kotlinlinkedhexcircleview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.linkedhexcircleview.LinkedHexCircleView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LinkedHexCircleView.create(this)
    }
}
