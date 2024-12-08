package com.susanafigueroa.villains

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.World

class Villain(
    world: World,
    name: String,
    x: Float,
    y: Float
) : Sprite(Texture(name)) {

}
