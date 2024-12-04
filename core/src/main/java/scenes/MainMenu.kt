package scenes

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.susanafigueroa.MovingPlayerKotlin
import helpers.GameInfo

class MainMenu (private var movingPlayerKotlin: MovingPlayerKotlin) : Screen {

    // I want to use the WIDTH and the HEIGHT of the GameInfo to declare thing positions and sizes
    // OrthographicCamera -> defines 2D perspective of the game
    private lateinit var camera: OrthographicCamera

    // StretchViewport -> to maintain a fixed aspect ratio
    private lateinit var viewport: StretchViewport

    private lateinit var imageBackground: Texture
    private lateinit var turtle: Sprite

    override fun show() {
        // initialize camera and viewport
        camera = OrthographicCamera()
        viewport = StretchViewport(GameInfo.WIDTH.toFloat(), GameInfo.HEIGHT.toFloat(), camera)

        // set the position of the camera in the middle of the screen
        camera.position.set(GameInfo.WIDTH/2f, GameInfo.HEIGHT/2f, 0f)
        camera.update()

        imageBackground = Texture("Game BG.png")
        turtle = Sprite(Texture("turtle.png"))
        turtle.setPosition((GameInfo.WIDTH/2).toFloat(), 0f)
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
