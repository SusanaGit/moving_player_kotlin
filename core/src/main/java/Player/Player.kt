package Player

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World

class Player( name: String, x: Float, y: Float
) : Sprite(Texture(name)) {

    private lateinit var world: World
    private lateinit var body: Body

    init {
        setSize(200f, 200f)
        setPosition(x - width / 2, y - height / 2)
    }
}
