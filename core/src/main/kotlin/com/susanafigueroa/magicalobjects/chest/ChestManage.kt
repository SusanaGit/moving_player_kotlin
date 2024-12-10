package com.susanafigueroa.magicalobjects.chest

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.susanafigueroa.helpers.GameInfo


class ChestManage {
    private val listChests: MutableList<Chest> = ArrayList()
    private val nameCollisionLayer = "chests_layer"

    fun getListChests(): List<Chest> {
        return listChests
    }


    fun createStaticSpriteChests(map: TiledMap, world: World) {
        val collisionLayer = map.layers[nameCollisionLayer] ?: return

        for (mapObject in collisionLayer.objects) {
            val newChestBody = createStaticChestBodyFromMap(mapObject, world)

            val newChest = Chest(
                world,
                "magicalobjects/chest.png",
                newChestBody!!.position.x,
                newChestBody.position.y
            )
            newChest.addBody(newChestBody)

            listChests.add(newChest)
        }
    }

    private fun createStaticChestBodyFromMap(chestObject: MapObject, world: World): Body? {
        if (chestObject is RectangleMapObject) {
            val rectChest = chestObject.rectangle
            val bodyDefChest = BodyDef()
            bodyDefChest.type = BodyDef.BodyType.StaticBody
            bodyDefChest.position[(rectChest.x + rectChest.width / 2) / GameInfo.PPM] =
                (rectChest.y + rectChest.height / 2) / GameInfo.PPM
            val chestBody = world.createBody(bodyDefChest)
            val shape = PolygonShape()
            shape.setAsBox(
                (rectChest.width / 2) / GameInfo.PPM,
                (rectChest.height / 2) / GameInfo.PPM
            )
            val fixtureDef = FixtureDef()
            fixtureDef.shape = shape
            fixtureDef.density = 20f

            val chestFixture = chestBody.createFixture(fixtureDef)
            chestFixture.userData = this

            shape.dispose()

            return chestBody
        } else {
            return null
        }
    }

    fun removeChest(chestToRemove: Chest) {
        chestToRemove.chestBody!!.world.destroyBody(chestToRemove.chestBody)
        listChests.remove(chestToRemove)
    }
}
