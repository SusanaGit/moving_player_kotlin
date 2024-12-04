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

    // I want to use the WIDTH and the HEIGHT of the GameInfo to declare thing positions and sizes
    // OrthographicCamera -> defines 2D perspective of the game
    private lateinit var camera: OrthographicCamera
    // StretchViewport -> to maintain a fixed aspect ratio
    private lateinit var viewport: StretchViewport

    override fun create() {
        // initialize camera and viewport
        camera = OrthographicCamera()
        viewport = StretchViewport(GameInfo.WIDTH.toFloat(), GameInfo.HEIGHT.toFloat(), camera)

        // set the position of the camera in the middle of the screen
        camera.position.set(GameInfo.WIDTH/2f, GameInfo.HEIGHT/2f, 0f)
        camera.update()



        turtle = Sprite(Texture("turtle.png"))

        turtle.setPosition((GameInfo.WIDTH/2).toFloat(), 0f)
    }

    override fun render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)

        camera.update()
        batch.setProjectionMatrix(camera.combined);
        batch.draw(turtle, turtle.x, turtle.y, 200f, 200f)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun dispose() {
        batch.dispose()
        image.dispose()
        turtle.getTexture().dispose()
    }*/
}
