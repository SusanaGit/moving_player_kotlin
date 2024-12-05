package Player

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World

class Player( name: String, x: Float, y: Float
) : Sprite(Texture(name)) {

    private lateinit var world: World
    private lateinit var body: Body

    init {
        setSize(200f, 200f)
        setPosition(x - width / 2, y - height / 2)
    }

    fun createBody() {
        val bodyDef: BodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        body = world.createBody(bodyDef)

        val shape: PolygonShape = PolygonShape()
        shape.setAsBox(width / 2, height / 2)

        val fixtureDef: FixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = 1f

        val fixture: Fixture = body.createFixture(fixtureDef)

        shape.dispose()
    }
}
