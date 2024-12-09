package com.susanafigueroa.timer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.susanafigueroa.helpers.GameInfo

class Timer (
    font: BitmapFont,
    totalTime: Float
) {

    private var font: BitmapFont
    private var totalTime = 0f

    init {
        this.font = font
        this.totalTime = totalTime
    }

    fun runTimer(batch: SpriteBatch?) {
        totalTime -= Gdx.graphics.deltaTime

        val minutes = totalTime.toInt() / 60
        val seconds = totalTime.toInt() % 60

        val showTimer = "$minutes : $seconds"
        font.draw(
            batch,
            showTimer,
            GameInfo.WIDTH - GameInfo.WIDTH.toFloat() / 15,
            GameInfo.WIDTH.toFloat() / 20
        )
    }
}
