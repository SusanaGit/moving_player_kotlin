package scenes

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Texture
import com.susanafigueroa.MovingPlayerKotlin
import helpers.GameInfo

class MainMenu (private var movingPlayerKotlin: MovingPlayerKotlin) : Screen {

    private var imageBackground: Texture

    init {
        imageBackground = Texture("Game BG.png")
    }

    override fun show() {

    }

    override fun render(delta: Float) {
        movingPlayerKotlin.getBatch.begin()
        movingPlayerKotlin.getBatch.draw(imageBackground, 0f, 0f, GameInfo.WIDTH.toFloat(), GameInfo.HEIGHT.toFloat())
        movingPlayerKotlin.getBatch.end()
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {

    }

}
