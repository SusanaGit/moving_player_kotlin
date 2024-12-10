package com.susanafigueroa.magicalobjects.chest

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.susanafigueroa.helpers.GameInfo


class Chest(private val world: World, nameTexturePath: String?, x: Float, y: Float) :
    Sprite(Texture(nameTexturePath)) {

    private var chestBody: Body? = null

    init {
        // ppm to pixels
        val xPixels = x * GameInfo.PPM
        val yPixels = y * GameInfo.PPM

        setSize(32f, 32f)

        setPosition(xPixels - width / 2, yPixels - height / 2)
    }

    fun setBody(chestBody: Body) {
        this.chestBody = chestBody
        for (fixture in chestBody.fixtureList) {
            fixture.userData = this
        }
    }

    fun getChestBody(): Body {
        return chestBody!!
    }

    fun drawChest(batch: SpriteBatch) {
        batch.draw(
            texture, x, y, width, height
        )
    }
}
