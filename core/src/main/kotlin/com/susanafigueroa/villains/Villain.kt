package com.susanafigueroa.villains

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.susanafigueroa.helpers.GameInfo

class Villain (
    private val world: World,
    nameTexturePath: String,
    x: Float,
    y: Float
) : Sprite(Texture(nameTexturePath)) {

    lateinit var body: Body

    init {
        // ppm to pixels
        val xPixels = x * GameInfo.PPM
        val yPixels = y * GameInfo.PPM

        setSize(96f, 96f)
        setPosition(xPixels - width / 2, yPixels - height / 2)
    }

    fun addBody(villainBody: Body) {
        body = villainBody
    }

    fun updateVillainPositionBody() {
        this.setPosition(
            (body.position.x * GameInfo.PPM) - width / 2,
            (body.position.y * GameInfo.PPM) - height / 2)
    }
}
