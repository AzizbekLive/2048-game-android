package uz.gita.azizbek2048.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import uz.gita.azizbek2048.model.SideEnum
import java.lang.Math.abs

class MyTouchListener(context: Context) : View.OnTouchListener {
    private val myGestureDetector = GestureDetector(context, MySimpleGestureDetector())
    private var handlSideEnumListener: ((SideEnum) -> Unit)? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        myGestureDetector.onTouchEvent(event)
        return true
    }


    inner class MySimpleGestureDetector : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {

            if (abs(e1.x - e2.x) > 100 || abs(e1.y - e2.y) > 100) {
                if (abs(e1.x - e2.x) > abs(e1.y - e2.y)) {
                    if (e1.x > e2.x) {
                        handlSideEnumListener?.invoke(SideEnum.LEFT)
                    } else {
                        handlSideEnumListener?.invoke(SideEnum.RIGHT)
                    }
                } else {
                    if (e1.y > e2.y) {
                        handlSideEnumListener?.invoke(SideEnum.UP)
                    } else {
                        handlSideEnumListener?.invoke(SideEnum.DOWN)
                    }
                }
            }

            return super.onFling(e1, e2, velocityX, velocityY)
        }
    }


    fun setHandleSideEnumListener(block: (SideEnum) -> Unit) {
        handlSideEnumListener = block
    }
}