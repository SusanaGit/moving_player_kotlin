package scenes

import com.badlogic.gdx.Screen
import com.susanafigueroa.MovingPlayerKotlin

class MainMenu : Screen {

    private lateinit var movingPlayerKotlin: MovingPlayerKotlin

    // I want to use the SpriteBatch from MovingPlayerKotlin class
    constructor(movingPlayerKotlin: MovingPlayerKotlin) {
        this.movingPlayerKotlin = movingPlayerKotlin
    }


    override fun show() {
        TODO("Not yet implemented")
    }

    override fun render(delta: Float) {
        TODO("Not yet implemented")
    }

    override fun resize(width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun hide() {
        TODO("Not yet implemented")
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }

}
