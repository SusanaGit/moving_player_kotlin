package com.susanafigueroa.magicalobjects.chandelier

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.susanafigueroa.helpers.GameInfo


class ChandelierManage {
    private val listChandeliers: MutableList<Chandelier> = ArrayList()
    private val nameCollisionLayer = "chandeliers_layer"

    fun getListChandeliers(): List<Chandelier> {
        return listChandeliers
    }

    fun createStaticSpriteChandeliers(map: TiledMap, world: World) {
        val collisionLayer = map.layers[nameCollisionLayer] ?: return

        for (mapObject in collisionLayer.objects) {
            val newChandelierBody = createStaticChandelierBodyFromMap(mapObject, world)

            val newChandelier = Chandelier(
                world,
                "magicalobjects/chandelier.png",
                newChandelierBody!!.position.x,
                newChandelierBody.position.y
            )
            newChandelier.addBody(newChandelierBody)
            listChandeliers.add(newChandelier)
        }
    }

    private fun createStaticChandelierBodyFromMap(
        chandelierObject: MapObject,
        world: World
    ): Body? {
        if (chandelierObject is RectangleMapObject) {
            val rectChandelier = chandelierObject.rectangle
            val bodyDefChandelier = BodyDef()
            bodyDefChandelier.type = BodyDef.BodyType.StaticBody
            bodyDefChandelier.position[(rectChandelier.x + rectChandelier.width / 2) / GameInfo.PPM] =
                (rectChandelier.y + rectChandelier.height / 2) / GameInfo.PPM
            val chandelierBody = world.createBody(bodyDefChandelier)
            val shape = PolygonShape()
            shape.setAsBox(
                (rectChandelier.width / 2) / GameInfo.PPM,
                (rectChandelier.height / 2) / GameInfo.PPM
            )
            val fixtureDef = FixtureDef()
            fixtureDef.shape = shape
            fixtureDef.density = 20f

            val chandelierFixture = chandelierBody.createFixture(fixtureDef)
            chandelierFixture.userData = this

            shape.dispose()

            return chandelierBody
        } else {
            return null
        }
    }
}
