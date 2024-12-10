package com.susanafigueroa.player

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array
import com.susanafigueroa.helpers.GameInfo
import com.susanafigueroa.timer.Timer

class Player(
    private val world: World,
    name: String,
    x: Float,
    y: Float,
    private val timer: Timer
) : Sprite(Texture(name)) {

    lateinit var body: Body

    // animation
    private val playerAtlas: TextureAtlas
    private val walkingRightAnimation: Animation<TextureRegion>
    private val walkingLeftAnimation: Animation<TextureRegion>
    private val jumpingAnimation: Animation<TextureRegion>
    private val dyingAnimation: Animation<TextureRegion>
    private var elapsedTime: Float
    private var isWalkingRight: Boolean
    private var isWalkingLeft: Boolean
    private var isJumping: Boolean
    private var isDying: Boolean

    init {
        setSize(40f, 40f)
        setPosition(x - width / 2, y - height / 2)
        createBody()

        this.elapsedTime = 0f
        this.isWalkingLeft = false
        this.isWalkingRight = false
        this.isJumping = false
        this.isDying = false

        // animation player
        playerAtlas = TextureAtlas("player/playersatlas.atlas")

        // player jumping
        val jumpingFrames = Array<TextureRegion>()
        jumpingFrames.add(playerAtlas.findRegion("Jump (1)"))
        jumpingFrames.add(playerAtlas.findRegion("Jump (4)"))
        jumpingAnimation = Animation(1f / 20f, jumpingFrames)

        // player walking right
        val walkingRightFrames = Array<TextureRegion>()
        walkingRightFrames.add(playerAtlas.findRegion("Walk (1)"))
        walkingRightFrames.add(playerAtlas.findRegion("Walk (8)"))
        walkingRightAnimation = Animation(1f / 5f, walkingRightFrames)

        // player walking left
        val walkingLeftFrames = Array<TextureRegion>()

        val walkLeft1 = TextureRegion(playerAtlas.findRegion("Walk (1)"))
        walkLeft1.flip(true, false)
        walkingLeftFrames.add(walkLeft1)

        val walkLeft2 = TextureRegion(playerAtlas.findRegion("Walk (8)"))
        walkLeft2.flip(true, false)
        walkingLeftFrames.add(walkLeft2)
        walkingLeftAnimation = Animation(1f / 5f, walkingLeftFrames)

        // player dying
        val dyingFrames = Array<TextureRegion>()
        dyingFrames.add(playerAtlas.findRegion("Dead (1)"))
        dyingFrames.add(playerAtlas.findRegion("Dead (6)"))
        dyingFrames.add(playerAtlas.findRegion("Dead (17)"))
        dyingFrames.add(playerAtlas.findRegion("Dead (30)"))
        dyingAnimation = Animation(1f/4f, dyingFrames)
    }

    fun createBody() {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody

        bodyDef.position.set(
            (x + width / 2)/ GameInfo.PPM,
            (y + height / 2)/ GameInfo.PPM
        )

        body = world.createBody(bodyDef)

        val shape = PolygonShape()
        shape.setAsBox(
            (width / 2f)/ GameInfo.PPM,
            (height / 2f)/ GameInfo.PPM)

        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = 80f
        fixtureDef.friction = 40f

        val fixturePlayer = body.createFixture(fixtureDef)

        fixturePlayer.userData = this

        shape.dispose()
    }

    fun handleInput() {

        isWalkingRight = false
        isWalkingLeft = false

        if (!isDying) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                isWalkingLeft = true
                body.applyLinearImpulse(
                    Vector2(-3f, 0f), body.worldCenter, true
                )
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                isWalkingRight = true
                body.applyLinearImpulse(
                    Vector2(3f, 0f), body.worldCenter, true
                )
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                isJumping = true
                body.applyLinearImpulse(
                    Vector2(0f, 3f), body.worldCenter, true
                )
            }  else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                isJumping = true
                body.applyLinearImpulse(
                    Vector2(0f, -3f), body.worldCenter, true
                )
            }

            if (Gdx.input.isTouched) {

                isWalkingRight = false
                isWalkingLeft = false

                val valueTouchX = Gdx.input.x.toFloat()
                val valueTouchY = Gdx.input.y.toFloat()
                val screenWidth = Gdx.graphics.width.toFloat()
                val screenHeight = Gdx.graphics.height.toFloat()

                if (valueTouchX < screenWidth / 2) {
                    isWalkingLeft = true
                    body.applyLinearImpulse(
                        Vector2(-3f, 0f), body.worldCenter, true
                    )
                } else {
                    isWalkingRight = true
                    body.applyLinearImpulse(
                        Vector2(+3f, 0f), body.worldCenter, true
                    )
                }

                if (valueTouchY > screenHeight / 2) {
                    isJumping = true
                    body.applyLinearImpulse(
                        Vector2(0f, -3f), body.worldCenter, true
                    )
                } else {
                    isJumping = true
                    body.applyLinearImpulse(
                        Vector2(0f, +3f), body.worldCenter, true
                    )
                }
            }
        }
    }

    fun updatePlayer(dt: Float) {
        this.setPosition(
            (body.position.x * GameInfo.PPM) - width / 2,
            (body.position.y * GameInfo.PPM) - height / 2)

        if (isWalkingLeft || isWalkingRight) {
            elapsedTime += dt
        }

        if (body.linearVelocity.y == 0f) {
            isJumping = false
        }

        if (timer.getTotalTime().toInt() == 0) {
            if (!isDying) {
                isDying = true
                elapsedTime = 0f
            }
            elapsedTime += dt
        }
    }

    fun drawPlayerAnimation(batch: SpriteBatch) {
        val currentTexture: TextureRegion
        val defaultTexture = TextureRegion(texture)

        if (isWalkingRight) {
            currentTexture = walkingRightAnimation.getKeyFrame(elapsedTime, true)
        } else if (isWalkingLeft) {
            currentTexture = walkingLeftAnimation.getKeyFrame(elapsedTime, true)
        } else if (isJumping) {
            currentTexture = jumpingAnimation.getKeyFrame(elapsedTime, true)
        } else if (isDying) {
            currentTexture = dyingAnimation.getKeyFrame(elapsedTime, false)
        } else {
            currentTexture = defaultTexture
        }

        batch.draw(
            currentTexture, x, y, width, height
        )
    }
}
