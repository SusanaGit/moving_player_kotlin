package com.susanafigueroa.scenes

import com.badlogic.gdx.Game
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
import com.susanafigueroa.villains.VillainManage

class MainMenu (
    private var movingPlayerKotlin: MovingPlayerKotlin,
) : Screen {

    private val mapCamera: OrthographicCamera
    private val box2DCamera: OrthographicCamera
    private val viewport: StretchViewport
    private val mapLoader: TmxMapLoader
    private var tiledMap: TiledMap
    private var mapRenderer: OrthogonalTiledMapRenderer
    private var turtle: Player
    private var world: World = World(Vector2(0f, -9.8f), true)
    private val bodiesMap: BodiesMap
    private var villainManage: VillainManage
    private val debugRenderer: Box2DDebugRenderer

    init {
        mapLoader = TmxMapLoader();
        tiledMap = mapLoader.load("mapa.tmx")
        mapRenderer = OrthogonalTiledMapRenderer(tiledMap)

        // camera for the map -> TiledMap -> pixels
        mapCamera = OrthographicCamera().apply {
            setToOrtho(false, GameInfo.WIDTH.toFloat(), GameInfo.HEIGHT.toFloat())
            position.set(
                GameInfo.WIDTH / 2f,
                GameInfo.HEIGHT / 2f,
                0f)
            update()
        }

        // camera for the Box2D -> ppm
        box2DCamera = OrthographicCamera().apply {
            setToOrtho(false, (GameInfo.WIDTH/GameInfo.PPM).toFloat(), (GameInfo.HEIGHT/GameInfo.PPM).toFloat())
            position.set((
                GameInfo.WIDTH/2f)/GameInfo.PPM,
                (GameInfo.HEIGHT/2f)/GameInfo.PPM,0f)
            update()
        }

        viewport = StretchViewport(
            GameInfo.WIDTH.toFloat(),
            GameInfo.HEIGHT.toFloat(),
            mapCamera
        )

        turtle = Player(world, "turtle.png", GameInfo.WIDTH.toFloat() / 2, GameInfo.HEIGHT.toFloat() / 2)

        bodiesMap = BodiesMap()
        bodiesMap.createStaticBodiesFromMap(tiledMap, world)

        villainManage = VillainManage()
        villainManage.createStaticSpriteVillains(tiledMap, world)

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

        val mapWidthPixels = mapWidthTiles * 32f
        val mapHeightPixels = mapHeightTiles * 32f

        val cameraWidth = mapCamera.viewportWidth
        val cameraHeight = mapCamera.viewportHeight

        val cameraX = (cameraWidth / 2).coerceAtLeast(
            (positionPlayerTurtle.x * GameInfo.PPM).coerceAtMost(mapWidthPixels - cameraWidth / 2)
        )

        val cameraY = (cameraHeight / 2).coerceAtLeast(
            (positionPlayerTurtle.y * GameInfo.PPM).coerceAtMost(mapHeightPixels - cameraHeight / 2)
        )

        // pixels cam TiledMap
        mapCamera.position.set(cameraX, cameraY, 0f)
        mapCamera.update()

        // ppm cam BoxD2
        box2DCamera.position.set(cameraX/GameInfo.PPM, cameraY/GameInfo.PPM, 0f)
        box2DCamera.update()
    }

    override fun render(delta: Float) {

        update(delta)

        updateCamera()

        turtle.updatePlayer()

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)

        mapRenderer.setView(mapCamera)
        mapRenderer.render()

        debugRenderer.render(world, box2DCamera.combined)

        movingPlayerKotlin.getBatch.setProjectionMatrix(mapCamera.combined)

        movingPlayerKotlin.getBatch.begin()
        movingPlayerKotlin.getBatch.draw(turtle, turtle.x, turtle.y, turtle.width, turtle.height)

        for (villain in villainManage.getListVillains()) {
            movingPlayerKotlin.getBatch.draw(villain, villain.x, villain.y, villain.width, villain.height)
        }
        movingPlayerKotlin.getBatch.end()

        world.step(Gdx.graphics.deltaTime, 6, 2)
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
        debugRenderer.dispose()
    }

}
