package com.github.gleberphant.gameclick
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.hypot
import android.view.Choreographer
import kotlin.random.Random
class CanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), Choreographer.FrameCallback {
    private var pontos: Int = 0
    private var ballX: Float = 200f
    private var ballY: Float = 200f
    private val ballSize = 100f
    private val ballStyle = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }
    private var isDragging = false


    //call back de frame atrelado ao VSYNC do dispositivo
    private val choreographer = Choreographer.getInstance()
    private var isRunning = false


    //quando app em foco roda o loop
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        isRunning=true
        choreographer.postFrameCallback(this)
    }
    // quando perder o foco pausa o loop
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isRunning=false
        choreographer.removeFrameCallback (this)
    }


    // loop principal


    override fun doFrame(frameTimeNanos: Long) {
        if(!isRunning)return

        // UPDATE
        // DRAW
        invalidate()
        choreographer.postFrameCallback(this)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                val distance = hypot(
                    (event.x - ballX ).toDouble(),
                    (event.y - ballY ).toDouble()
                )
                if (distance <= ballSize) {

                    ballX = Random.nextDouble(ballSize.toDouble(), (width- ballSize).toDouble()).toFloat()
                    ballY = Random.nextDouble(ballSize.toDouble(), (height- ballSize).toDouble()).toFloat()
                    pontos++
                }

                return true
            }

            MotionEvent.ACTION_UP ->{
                isDragging = false
                return true
            }

            MotionEvent.ACTION_MOVE ->{
                if(isDragging) {
                    ballX = event.x
                    ballY = event.y

                    return true
                }
            }

    }
        return super.onTouchEvent(event)
    }

    private val textStyle = Paint().apply{
        color= Color.CYAN
        textSize=64f
        textAlign= Paint.Align.LEFT
        isFakeBoldText = true
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(ballX, ballY, ballSize,ballStyle)
        canvas.drawText("Pontos: $pontos", 50f , 120f, textStyle)
    }
}