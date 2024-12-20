package com.susanafigueroa.villains

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.susanafigueroa.helpers.GameInfo

class VillainManage {

    private val listVillains: MutableList<Villain> = mutableListOf()
    private val listVillainsToRemove: MutableList<Villain> = mutableListOf()
    private val nameCollisionLayer = "villains_layer"

    fun getListVillains(): List<Villain> {
        return listVillains
    }

    fun createStaticSpriteVillains(map: TiledMap, world: World) {
        val collisionLayer = map.layers[nameCollisionLayer]
        if (collisionLayer == null) {
            return
        }

        for (mapObject in collisionLayer.objects) {
            val newVillainBody = createStaticVillainBodyFromMap(mapObject, world)

            if (newVillainBody != null) {
                val newVillain = Villain(
                    world,
                    "villains/villain.png",
                    newVillainBody.position.x,
                    newVillainBody.position.y
                )
                newVillain.addBody(newVillainBody)
                listVillains.add(newVillain)
            }
        }
    }

    private fun createStaticVillainBodyFromMap(villainObject: MapObject, world: World): Body? {
        if (villainObject is RectangleMapObject) {
            val rectVillain = villainObject.rectangle
            val bodyDefVillain = BodyDef().apply {
                type = BodyDef.BodyType.DynamicBody
                position.set(
                    (rectVillain.x + rectVillain.width / 2) / GameInfo.PPM,
                    (rectVillain.y + rectVillain.height / 2) / GameInfo.PPM
                )
            }
            val villainBody = world.createBody(bodyDefVillain)
            val shape = PolygonShape().apply {
                setAsBox(
                    (rectVillain.width / 2) / GameInfo.PPM,
                    (rectVillain.height / 2) / GameInfo.PPM
                )
            }
            val fixtureDef = FixtureDef().apply {
                this.shape = shape
                density = 20f
            }

            villainBody.createFixture(fixtureDef)

            shape.dispose()

            return villainBody
        } else {
            return null
        }
    }

    fun removeVillain(villainToRemove: Villain) {
        if (!listVillainsToRemove.contains(villainToRemove)) {
            listVillainsToRemove.add(villainToRemove)
        }
    }

    fun updateListVillains() {
        for (villainToRemove in listVillainsToRemove) {
            val villainTextureToRemove = villainToRemove!!.texture
            val villainBodyToRemove: Body = villainToRemove.getVillainBody()

            if (villainToRemove != null) {
                villainBodyToRemove.world.destroyBody(villainBodyToRemove)
            }

            villainTextureToRemove?.dispose()

            listVillains.remove(villainToRemove)
        }
        listVillainsToRemove.clear()
    }
}
