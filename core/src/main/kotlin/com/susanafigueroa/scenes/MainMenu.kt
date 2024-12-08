package com.susanafigueroa.scenes

import com.susanafigueroa.Player.Player
import com.susanafigueroa.bodiesmap.BodiesMap
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.sun.jdi.IntegerValue
import com.susanafigueroa.MovingPlayerKotlin
import com.susanafigueroa.helpers.GameInfo
import com.susanafigueroa.villains.Villain

class MainMenu (
    private var movingPlayerKotlin: MovingPlayerKotlin,
) : Screen {

    private val camera: OrthographicCamera
    private val viewport: StretchViewport
    private val mapLoader: TmxMapLoader
    private var tiledMap: TiledMap
    private var mapRenderer: OrthogonalTiledMapRenderer
    private var turtle: Player
    private var world: World
    private val bodiesMap: BodiesMap
    private var villains: Villain
    private val debugRenderer: Box2DDebugRenderer

    init {
        world = World(Vector2(0f, -9.8f), true)
        mapLoader = TmxMapLoader();
        tiledMap = mapLoader.load("mapa.tmx")
        mapRenderer = OrthogonalTiledMapRenderer(tiledMap)

        camera = OrthographicCamera().apply {
            setToOrtho(false, GameInfo.WIDTH.toFloat(), GameInfo.HEIGHT.toFloat())
            position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0f)
            update()
        }

        viewport = StretchViewport(
            GameInfo.WIDTH.toFloat(),
            GameInfo.HEIGHT.toFloat(),
            camera
        )

        turtle = Player(world, "turtle.png", GameInfo.WIDTH.toFloat() / 2, GameInfo.HEIGHT.toFloat() / 2)

        bodiesMap = BodiesMap()
        bodiesMap.createStaticBodiesFromMap(tiledMap, world)

        villains = Villain()
        villains.createDynamicVillainsBodiesFromMap(tiledMap, world)

        debugRenderer = Box2DDebugRenderer()
    }

    fun update(dt: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            turtle.body.applyLinearImpulse(
                Vector2(-20f, 0f), turtle.body.worldCenter, true
            )
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            turtle.body.applyLinearImpulse(
                Vector2(20f, 0f), turtle.body.worldCenter, true
            )
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            turtle.body.applyLinearImpulse(
                Vector2(0f, 20f), turtle.body.worldCenter, true
            )
        }  else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            turtle.body.applyLinearImpulse(
                Vector2(0f, -20f), turtle.body.worldCenter, true
            )
        }

        if (Gdx.input.isTouched) {
            val valueTouchX = Gdx.input.x.toFloat()
            val valueTouchY = Gdx.input.y.toFloat()
            val screenWidth = Gdx.graphics.width.toFloat()
            val screenHeight = Gdx.graphics.height.toFloat()

            if (valueTouchX < screenWidth / 2) {
                turtle.body.applyLinearImpulse(
                    Vector2(-20f, 0f), turtle.body.worldCenter, true
                )
            } else {
                turtle.body.applyLinearImpulse(
                    Vector2(+20f, 0f), turtle.body.worldCenter, true
                )
            }

            if (valueTouchY > screenHeight / 2) {
                turtle.body.applyLinearImpulse(
                    Vector2(0f, -20f), turtle.body.worldCenter, true
                )
            } else {
                turtle.body.applyLinearImpulse(
                    Vector2(0f, +20f), turtle.body.worldCenter, true
                )
            }
        }
    }

    override fun show() {

    }

    private fun updateCamera() {
        val positionPlayerTurtle = turtle.body.position // 4.8ppm x | 3.2ppm y

        val mapWidthTiles = tiledMap.properties.get("width", Int::class.java)
        val mapHeightTiles = tiledMap.properties.get("height", Int::class.java)
        Gdx.app.log("TILES MAP WIDTH", mapWidthTiles.toString()) // 300
        Gdx.app.log("TILES MAP HEIGHT", mapHeightTiles.toString()) // 40

        val mapWidthPixels = mapWidthTiles * 32f
        val mapHeightPixels = mapHeightTiles * 32f
        Gdx.app.log("PIXELS MAP WIDTH", mapWidthPixels.toString()) // 300 * 32 = 9600
        Gdx.app.log("PIXELS MAP HEIGHT", mapHeightPixels.toString()) // 40 * 32 = 1280

        val cameraWidth = camera.viewportWidth
        val cameraHeight = camera.viewportHeight
        Gdx.app.log("CAMERA WIDTH", cameraWidth.toString()) // 960
        Gdx.app.log("CAMERA HEIGHT", cameraHeight.toString()) // 640

        val cameraX = (cameraWidth / 2).coerceAtLeast(
            (positionPlayerTurtle.x * GameInfo.PPM).coerceAtMost(mapWidthPixels - cameraWidth / 2)
        );
        val cameraY = (cameraHeight / 2).coerceAtLeast(
            (positionPlayerTurtle.y * GameInfo.PPM).coerceAtMost(mapHeightPixels - cameraHeight / 2)
        );

        camera.position.set(cameraX, cameraY, 0f)

        camera.update()
    }

    override fun render(delta: Float) {

        update(delta)

        updateCamera()

        turtle.updatePlayer()

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)

        camera.update()

        mapRenderer.setView(camera)
        mapRenderer.render()

        movingPlayerKotlin.getBatch.setProjectionMatrix(camera.combined)

        movingPlayerKotlin.getBatch.begin()
        movingPlayerKotlin.getBatch.draw(turtle, turtle.x, turtle.y, turtle.width, turtle.height)
        movingPlayerKotlin.getBatch.end()

        debugRenderer.render(world, camera.combined)

        world.step(Gdx.graphics.getDeltaTime(), 6, 2)
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
