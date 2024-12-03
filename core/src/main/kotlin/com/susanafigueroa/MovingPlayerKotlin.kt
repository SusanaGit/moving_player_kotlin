package com.susanafigueroa

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils

class MovingPlayerKotlin : Game() {

    private lateinit var batch: SpriteBatch
    private lateinit var image: Texture

    override fun create() {
        batch = SpriteBatch()
        image = Texture("Game BG.png")
    }

    override fun render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)

        val screenWidth = Gdx.graphics.width
        val screenHeight = Gdx.graphics.height

        batch.begin()
        batch.draw(image, 0f, 0f, screenWidth.toFloat(), screenHeight.toFloat())
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        image.dispose()
    }
}
