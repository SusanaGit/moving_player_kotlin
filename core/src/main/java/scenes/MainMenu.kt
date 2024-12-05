package scenes

import Player.Player
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.susanafigueroa.MovingPlayerKotlin
import helpers.GameInfo

class MainMenu (
    private var movingPlayerKotlin: MovingPlayerKotlin,
) : Screen {

    private val camera: OrthographicCamera
    private val viewport: StretchViewport
    private val mapLoader: TmxMapLoader
    private var tiledMap: TiledMap
    private var mapRenderer: OrthogonalTiledMapRenderer
    private var turtle: Sprite
    private var world: World

    init {
        world = World(Vector2(0f, -9f), true)
        mapLoader = TmxMapLoader();
        tiledMap = mapLoader.load("mapa.tmx")
        mapRenderer = OrthogonalTiledMapRenderer(tiledMap)

        camera = OrthographicCamera().apply {
            position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0f)
            update()
        }

        viewport = StretchViewport(
            GameInfo.WIDTH.toFloat(),
            GameInfo.HEIGHT.toFloat(),
            camera
        )

        turtle = Player(world, "turtle.png", GameInfo.WIDTH.toFloat() / 2, GameInfo.HEIGHT.toFloat() / 2)
    }

    override fun show() {

    }

    override fun render(delta: Float) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)

        camera.update()

        mapRenderer.setView(camera)
        mapRenderer.render()

        movingPlayerKotlin.getBatch.setProjectionMatrix(camera.combined)

        movingPlayerKotlin.getBatch.begin()
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
        tiledMap.dispose()
        turtle.texture.dispose()
    }

}
