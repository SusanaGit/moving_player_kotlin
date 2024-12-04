package com.susanafigueroa

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
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
}
