package com.susanafigueroa.villains

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.susanafigueroa.helpers.GameInfo

class Villain {

    private val nameCollisionLayer = "villains_layer"

    fun createDynamicVillainsBodiesFromMap(map: TiledMap, world: World) {
        val collisionLayer = map.layers[nameCollisionLayer]
        if (collisionLayer == null) {
            Gdx.app.log("ERROR VILLAINS!!!!!", "LAYER NOT FOUND :(")
            return
        }

        for (mapObject in collisionLayer.objects) {
            Gdx.app.log("GOOD VILLAINS!!!!!", "LAYER FOUND :)")

            createDynamicVillainBodyFromMap(mapObject, world)
        }
    }

    private fun createDynamicVillainBodyFromMap(villainObject: MapObject, world: World) {
        if (villainObject is RectangleMapObject) {
            Gdx.app.log("GOOD!!!!!", "INSTANCEOF RectangleMapObject, im so happy")

            val rectVillain = villainObject.rectangle

            val bodyDefVillain = BodyDef().apply {
                type = BodyDef.BodyType.StaticBody
                position.set(
                    (rectVillain.x + rectVillain.width / 2) / GameInfo.PPM,
                    (rectVillain.y + rectVillain.height / 2) / GameInfo.PPM
                )
            }

            val villainBody = world.createBody(bodyDefVillain)

            val shape = PolygonShape().apply {
                setAsBox(
                    rectVillain.width / 2 / GameInfo.PPM,
                    rectVillain.height / 2 / GameInfo.PPM
                )
            }

            val fixtureDef = FixtureDef().apply {
                this.shape = shape
                density = 20f
            }

            villainBody.createFixture(fixtureDef)

            shape.dispose()
        }
    }
}
