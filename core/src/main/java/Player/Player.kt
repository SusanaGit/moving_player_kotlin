package Player

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite

class Player(
    name: String,
    x: Float,
    y: Float
) : Sprite(Texture(name)) {
    init {
        setSize(200f, 200f)
        setPosition(x - width / 2, y - height / 2)
    }
}
