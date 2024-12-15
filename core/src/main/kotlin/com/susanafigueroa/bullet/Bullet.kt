package com.susanafigueroa.bullet

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array
import com.susanafigueroa.helpers.GameInfo

class Bullet(private val world: World, name: String?, x: Float, y: Float) :
    Sprite(Texture(name)) {
    var body: Body? = null
        private set

    // animation
    private var elapsedTime = 0f
    private val playerAtlas: TextureAtlas
    private val shootingAnimation: Animation<TextureRegion>

    init {
        setSize(100f, 100f)
        setPosition(x - width / 2, y - height / 2)
        createBody()

        playerAtlas = TextureAtlas("bullet/bulletatlas.atlas")

        // player shooting
        val shootingFrames = Array<TextureRegion>()
        shootingFrames.add(playerAtlas.findRegion("shoot1"))
        shootingFrames.add(playerAtlas.findRegion("shoot2"))
        shootingFrames.add(playerAtlas.findRegion("shoot3"))
        shootingFrames.add(playerAtlas.findRegion("shoot4"))
        shootingFrames.add(playerAtlas.findRegion("shoot7"))
        shootingAnimation = Animation(1f / 20f, shootingFrames)
    }

    fun setElapsedTime(elapsedTime: Float) {
        this.elapsedTime = elapsedTime
    }

    fun createBody() {
        val bodyDef = BodyDef()

        bodyDef.type = BodyDef.BodyType.DynamicBody

        bodyDef.position[(x + width / 2) / GameInfo.PPM] = (y + height / 2) / GameInfo.PPM

        body = world.createBody(bodyDef)

        val shape = PolygonShape()
        shape.setAsBox(
            (width / 2f) / GameInfo.PPM,
            (height / 2f) / GameInfo.PPM
        )

        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = 1f
        fixtureDef.friction = 0f
        fixtureDef.restitution = 0.1f

        val bulletFixture = body!!.createFixture(fixtureDef)
        bulletFixture.userData = this

        shape.dispose()
    }

    fun updateBullet(delta: Float) {
        elapsedTime += delta
        this.setPosition(
            (body!!.position.x * GameInfo.PPM) - width / 2,
            (body!!.position.y * GameInfo.PPM) - height / 2
        )
    }

    fun drawBulletAnimation(batch: SpriteBatch) {
        val currentTexture = shootingAnimation.getKeyFrame(elapsedTime, true)

        batch.draw(
            currentTexture, x, y, width, height
        )
    }

    val isFinished: Boolean
        get() {
            val timeBullet = 2

            if (elapsedTime > timeBullet) {
                return true
            }

            return false
        }
}


