package com.susanafigueroa.Player

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.susanafigueroa.helpers.GameInfo

class Player(
    world: World,
    name: String,
    x: Float,
    y: Float
) : Sprite(Texture(name)) {

    lateinit var body: Body

    init {
        setSize(40f, 40f)
        setPosition(x - width / 2, y - height / 2)
        createBody(world)
    }

    fun createBody(world: World) {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody

        bodyDef.position.set(
            (x + width / 2)/ GameInfo.PPM,
            (y + height / 2)/ GameInfo.PPM
        )

        body = world.createBody(bodyDef)

        val shape = PolygonShape()
        shape.setAsBox(
            (width / 2f)/ GameInfo.PPM,
            (height / 2f)/ GameInfo.PPM)

        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = 80f
        fixtureDef.friction = 40f

        body.createFixture(fixtureDef)

        shape.dispose()
    }

    fun updatePlayer() {
        this.setPosition(
            (body.position.x * GameInfo.PPM) - width / 2,
            (body.position.y * GameInfo.PPM) - height / 2)
    }

}
