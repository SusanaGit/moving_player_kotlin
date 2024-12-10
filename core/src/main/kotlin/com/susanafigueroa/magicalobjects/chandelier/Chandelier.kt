package com.susanafigueroa.magicalobjects.chandelier

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.susanafigueroa.helpers.GameInfo


class Chandelier(private val world: World, nameTexturePath: String?, x: Float, y: Float) :
    Sprite(Texture(nameTexturePath)) {
    private var chandelierBody: Body? = null

    init {
        // ppm to pixels
        val xPixels = x * GameInfo.PPM
        val yPixels = y * GameInfo.PPM

        setSize(32f, 64f)

        setPosition(xPixels - width / 2, yPixels - height / 2)
    }

    fun addBody(chandelierBody: Body?) {
        this.chandelierBody = chandelierBody
    }

    fun drawChandelier(batch: SpriteBatch) {
        batch.draw(
            texture, x, y, width, height
        )
    }
}
