package bodiesmap

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import helpers.GameInfo

class BodiesMap {

    private val nameCollisionLayer = "collision_layer"

    fun createStaticBodiesFromMap(map: TiledMap, world: World) {

        val collisionLayer = map.layers[nameCollisionLayer]

        if (collisionLayer == null) {
            Gdx.app.log("!!!!!", "LAYER NOT FOUND :(");
            return;
        }

        for (mapObject in collisionLayer.objects) {
            Gdx.app.log("GOOD!!!!!", "LAYER FOUND :)");

            createStaticBody(mapObject, world);
        }
    }

    fun createStaticBody(mapObject: MapObject, world: World) {
        if (mapObject is RectangleMapObject) {
            Gdx.app.log("GOOD!!!!!", "INSTANCEOF RectangleMapObject, im so happy");

            val rectObject = mapObject.rectangle

            val bodyDef = BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(
                (rectObject.x + rectObject.width / 2)/GameInfo.PPM,
                (rectObject.y + rectObject.height / 2)/GameInfo.PPM
            )

            val mapBody = world.createBody(bodyDef);

            val shape = PolygonShape()
            shape.setAsBox(
                (rectObject.width / 2)/GameInfo.PPM,
                rectObject.height / 2/GameInfo.PPM
            );

            val fixtureDef = FixtureDef().apply {
                this.shape = shape
                this.density = 20f
            }

            mapBody.createFixture(fixtureDef);

            shape.dispose();
        }
    }

}
