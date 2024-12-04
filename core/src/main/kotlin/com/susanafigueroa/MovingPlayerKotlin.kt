package com.susanafigueroa

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.StretchViewport
import helpers.GameInfo
import scenes.MainMenu

class MovingPlayerKotlin : Game() {

    private lateinit var batch: SpriteBatch

    val getBatch: SpriteBatch
        get() = batch

    override fun create() {
        batch = SpriteBatch()
        setScreen(MainMenu(this))
    }

    override fun render() {
        super.render()
    }

    /*
    private lateinit var turtle: Sprite




    override fun create() {




        turtle = Sprite(Texture("turtle.png"))

        turtle.setPosition((GameInfo.WIDTH/2).toFloat(), 0f)
    }

    override fun render() {
batch.draw(turtle, turtle.x, turtle.y, 200f, 200f)
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun dispose() {

        turtle.getTexture().dispose()
    }*/
}
