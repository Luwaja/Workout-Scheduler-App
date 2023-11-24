package edu.uark.workoutreminderapp.ui.calendar

import android.widget.CalendarView
import android.graphics.Paint
import android.content.Context
import android.util.AttributeSet
import android.graphics.Color
import android.graphics.Canvas
import java.util.*

// This is a custom calendar view that extends the current calendar view, but allows me to override the onDraw() function
// so that I can highlight the colors on the calendar of workouts.
class WorkoutCalendarView : CalendarView {
    private val highlightDays = HashMap<Long, Int>()
    private val paint = Paint()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        paint.style = Paint.Style.FILL
    }

    // Add a workout with specified color
    fun addHighlightedDay(timeInMillis: Long, color: Int) {
        highlightDays[timeInMillis] = color
        invalidate()
    }

    // Remove workout from calendar view
    fun removeHighlightedDay(timeInMillis: Long) {
        highlightDays.remove(timeInMillis)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val firstVisibleDayOfMonth = getFirstVisibleDay()
        val cellWidth = width / 7f
        val cellHeight = height / 7f
        val cellMargin = 2f

        for ((dayInMillis, color) in highlightDays) {
            val day = dayOfMonthFromTimeInMillis(dayInMillis)
            val cell = getDayCell(day, firstVisibleDayOfMonth, cellWidth, cellHeight, cellMargin)
            drawHighlightedDay(canvas, cell, color)
        }
    }

    private fun drawHighlightedDay(canvas: Canvas, cell: FloatArray, color: Int) {
        paint.color = color
        canvas.drawCircle(cell[0], cell[1], cell[2] / 2, paint)
    }

    private fun getDayCell(day: Int, firstVisibleDayOfMonth: Int, cellWidth: Float, cellHeight: Float, cellMargin: Float): FloatArray {
        val dayOfMonth = day - firstVisibleDayOfMonth
        val col = dayOfMonth % 7
        val row = dayOfMonth / 7
        val x = col * (cellWidth + cellMargin)
        val y = row * (cellHeight + cellMargin)
        return floatArrayOf(x + cellWidth / 2, y + cellHeight / 2, cellWidth)
    }

    private fun getFirstVisibleDay(): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun dayOfMonthFromTimeInMillis(timeInMillis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        return calendar.get(Calendar.DAY_OF_MONTH)
    }
}