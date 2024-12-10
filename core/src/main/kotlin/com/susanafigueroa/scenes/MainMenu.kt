package com.susanafigueroa.scenes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.susanafigueroa.MovingPlayerKotlin
import com.susanafigueroa.Player.Player
import com.susanafigueroa.bodiesmap.BodiesMap
import com.susanafigueroa.contactplayervillain.ContactPlayerVillain
import com.susanafigueroa.helpers.GameInfo
import com.susanafigueroa.magicalobjects.chandelier.ChandelierManage
import com.susanafigueroa.magicalobjects.chest.ChestManage
import com.susanafigueroa.timer.Timer
import com.susanafigueroa.villains.VillainManage

class MainMenu (
    private var movingPlayerKotlin: MovingPlayerKotlin,
) : Screen {

    private val mapCamera: OrthographicCamera
    private val box2DCamera: OrthographicCamera
    private val hudCamera: OrthographicCamera
    private val viewport: StretchViewport
    private val mapLoader: TmxMapLoader
    private val tiledMap: TiledMap
    private val mapRenderer: OrthogonalTiledMapRenderer
    private val cuteGirl: Player
    private val world: World = World(Vector2(0f, -9.8f), true)
    private val bodiesMap: BodiesMap
    private val villainManage: VillainManage
    private val chestManage: ChestManage
    private val chandelierManage: ChandelierManage
    private val debugRenderer: Box2DDebugRenderer
    private var timer: Timer
    private var contactPlayerVillain: ContactPlayerVillain

    init {
        mapLoader = TmxMapLoader();
        tiledMap = mapLoader.load("map/mapa.tmx")
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

        // camera for HUD -> pixels
        hudCamera = OrthographicCamera()
        hudCamera.setToOrtho(false, GameInfo.WIDTH.toFloat(), GameInfo.HEIGHT.toFloat())
        hudCamera.update()

        viewport = StretchViewport(
            GameInfo.WIDTH.toFloat(),
            GameInfo.HEIGHT.toFloat(),
            mapCamera
        )

        timer = Timer(BitmapFont(), 120f)

        cuteGirl = Player(world, "player/player.png", GameInfo.WIDTH.toFloat() / 2, GameInfo.HEIGHT.toFloat() / 2, timer)

        bodiesMap = BodiesMap()
        bodiesMap.createStaticBodiesFromMap(tiledMap, world)

        villainManage = VillainManage()
        villainManage.createStaticSpriteVillains(tiledMap, world)

        chestManage = ChestManage()
        chestManage.createStaticSpriteChests(tiledMap, world)

        chandelierManage = ChandelierManage()
        chandelierManage.createStaticSpriteChandeliers(tiledMap, world)

        contactPlayerVillain = ContactPlayerVillain(timer)
        world.setContactListener(contactPlayerVillain)

        debugRenderer = Box2DDebugRenderer()
    }

    override fun show() {

    }

    private fun updateCamera() {
        val positionPlayerCuteGirl = cuteGirl.body.position // 4.8ppm x | 3.2ppm y

        val mapWidthTiles = tiledMap.properties.get("width", Int::class.java)
        val mapHeightTiles = tiledMap.properties.get("height", Int::class.java)

        val mapWidthPixels = mapWidthTiles * 32f
        val mapHeightPixels = mapHeightTiles * 32f

        val cameraWidth = mapCamera.viewportWidth
        val cameraHeight = mapCamera.viewportHeight

        val cameraX = (cameraWidth / 2).coerceAtLeast(
            (positionPlayerCuteGirl.x * GameInfo.PPM).coerceAtMost(mapWidthPixels - cameraWidth / 2)
        )

        val cameraY = (cameraHeight / 2).coerceAtLeast(
            (positionPlayerCuteGirl.y * GameInfo.PPM).coerceAtMost(mapHeightPixels - cameraHeight / 2)
        )

        // pixels cam TiledMap
        mapCamera.position.set(cameraX, cameraY, 0f)
        mapCamera.update()

        // ppm cam BoxD2
        box2DCamera.position.set(cameraX/GameInfo.PPM, cameraY/GameInfo.PPM, 0f)
        box2DCamera.update()
    }

    override fun render(delta: Float) {

        cuteGirl.handleInput()

        updateCamera()

        cuteGirl.updatePlayer(delta)

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)

        mapRenderer.setView(mapCamera)
        mapRenderer.render()

        debugRenderer.render(world, box2DCamera.combined)

        movingPlayerKotlin.getBatch.setProjectionMatrix(mapCamera.combined)

        movingPlayerKotlin.getBatch.begin()
        cuteGirl.drawPlayerAnimation(movingPlayerKotlin.getBatch)
        for (villain in villainManage.getListVillains()) {
            villain.villainIsWalking(delta)
            villain.drawVillainAnimation(movingPlayerKotlin.getBatch)
        }
        for (chest in chestManage.getListChests()) {
            chest.drawChest(movingPlayerKotlin.getBatch)
        }
        for (chandelier in chandelierManage.getListChandeliers()) {
            chandelier.drawChandelier(movingPlayerKotlin.getBatch)
        }
        movingPlayerKotlin.getBatch.end()

        // HUD
        movingPlayerKotlin.getBatch.setProjectionMatrix(hudCamera.combined);
        movingPlayerKotlin.getBatch.begin()
        timer.runTimer(movingPlayerKotlin.getBatch)
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
        cuteGirl.texture.dispose()
        debugRenderer.dispose()
    }
}
