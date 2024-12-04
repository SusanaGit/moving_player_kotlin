package scenes

import Player.Player
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.susanafigueroa.MovingPlayerKotlin
import helpers.GameInfo

class MainMenu (
    private var movingPlayerKotlin: MovingPlayerKotlin,
) : Screen {

    private val imageBackground: Texture
    private var turtle: Sprite
    private val camera: OrthographicCamera
    private val viewport: StretchViewport

    init {
        imageBackground = Texture("Game BG.png")

        turtle = Player("turtle.png", GameInfo.WIDTH.toFloat() / 2, GameInfo.HEIGHT.toFloat() / 2)

        camera = OrthographicCamera().apply {
            position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0f)
            update()
        }

        viewport = StretchViewport(
            GameInfo.WIDTH.toFloat(),
            GameInfo.HEIGHT.toFloat(),
            camera
        )
    }

    override fun show() {

    }

    override fun render(delta: Float) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)

        camera.update()
        movingPlayerKotlin.getBatch.setProjectionMatrix(camera.combined)
        movingPlayerKotlin.getBatch.begin()
        movingPlayerKotlin.getBatch.draw(imageBackground, 0f, 0f, GameInfo.WIDTH.toFloat(), GameInfo.HEIGHT.toFloat())
        movingPlayerKotlin.getBatch.draw(turtle, turtle.x, turtle.y, 200f, 200f)
        movingPlayerKotlin.getBatch.end()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {
        movingPlayerKotlin.getBatch.dispose()
        imageBackground.dispose()
        turtle.texture.dispose()
    }

}
