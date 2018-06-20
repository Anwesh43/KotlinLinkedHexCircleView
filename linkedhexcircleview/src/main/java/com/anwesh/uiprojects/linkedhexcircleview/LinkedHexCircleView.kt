package com.anwesh.uiprojects.linkedhexcircleview

/**
 * Created by anweshmishra on 20/06/18.
 */

import android.view.MotionEvent
import android.view.View
import android.content.Context
import android.graphics.*

val HC_NODES : Int = 5

class LinkedHexCircleView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(stopcb : (Float) -> Unit) {
            scale += 0.1f * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(prevScale)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class HCNode (var i : Int) {

        private var next : HCNode? = null

        private var prev : HCNode? = null

        private val state : State = State()

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < HC_NODES - 1) {
                next = HCNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val gap : Float = 360f / HC_NODES
            val r : Float = Math.min(w, h) / 3
            prev?.draw(canvas, paint)
            paint.color = Color.parseColor("#e67e22")
            canvas.save()
            canvas.translate(w/2, h/2)
            canvas.rotate(gap * i + gap * state.scale)
            canvas.drawCircle(r, 0f, r/10, paint)
            canvas.restore()
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : HCNode {
            var curr : HCNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class LinkedHexCircle (var i : Int) {

        private var curr : HCNode = HCNode(i + 1)

        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(stopcb : (Float) -> Unit) {
            curr.update {
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                stopcb(it)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            curr.startUpdating(startcb)
        }
    }

    data class Renderer(var view : LinkedHexCircleView) {

        private val animator : Animator = Animator(view)

        private val lhc : LinkedHexCircle = LinkedHexCircle(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            lhc.draw(canvas, paint)
            animator.animate {
                lhc.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            lhc.startUpdating {
                animator.start()
            }
        }
    }
}