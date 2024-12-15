package com.susanafigueroa.villains

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array
import com.susanafigueroa.helpers.GameInfo

class Villain (
    private val world: World,
    nameTexturePath: String,
    x: Float,
    y: Float
) : Sprite(Texture(nameTexturePath)) {

    lateinit var body: Body

    // animation
    private var villainAtlas: TextureAtlas? = null
    private var walkingVillainRightAnimation: Animation<TextureRegion>? = null
    private var walkingVillainLeftAnimation: Animation<TextureRegion>? = null
    private var elapsedTime = 0f
    private var isVillainWalkingRight = false
    private var isVillainWalkingLeft = true
    private val villainIsDead = false
    private var initialVillainXPosition = 0f

    init {
        // ppm to pixels
        val xPixels = x * GameInfo.PPM

        // save initial x position
        initialVillainXPosition = xPixels
        val yPixels = y * GameInfo.PPM

        setSize(96f, 96f)
        setPosition(xPixels - width / 2, yPixels - height / 2)


        // animation villain
        villainAtlas = TextureAtlas("villains/villainsatlas.atlas")


        // villain walking right
        val walkingVillainRightFrames = Array<TextureRegion>()
        walkingVillainRightFrames.add(villainAtlas!!.findRegion("Walk (1)"))
        walkingVillainRightFrames.add(villainAtlas!!.findRegion("Walk (3)"))
        walkingVillainRightFrames.add(villainAtlas!!.findRegion("Walk (6)"))
        walkingVillainRightFrames.add(villainAtlas!!.findRegion("Walk (7)"))
        walkingVillainRightAnimation = Animation(1f / 5f, walkingVillainRightFrames)


        // villain walking left
        val walkingVillainLeftFrames = Array<TextureRegion>()

        val walkVillainLeft1 = TextureRegion(villainAtlas!!.findRegion("Walk (1)"))
        walkVillainLeft1.flip(true, false)
        walkingVillainLeftFrames.add(walkVillainLeft1)

        val walkVillainLeft2 = TextureRegion(villainAtlas!!.findRegion("Walk (3)"))
        walkVillainLeft2.flip(true, false)
        walkingVillainLeftFrames.add(walkVillainLeft2)

        val walkVillainLeft3 = TextureRegion(villainAtlas!!.findRegion("Walk (6)"))
        walkVillainLeft3.flip(true, false)
        walkingVillainLeftFrames.add(walkVillainLeft3)

        val walkVillainLeft4 = TextureRegion(villainAtlas!!.findRegion("Walk (7)"))
        walkVillainLeft4.flip(true, false)
        walkingVillainLeftFrames.add(walkVillainLeft4)

        walkingVillainLeftAnimation = Animation(1f / 5f, walkingVillainLeftFrames)
    }

    fun getVillainBody(): Body {
        return this.body
    }

    fun addBody(villainBody: Body) {
        body = villainBody
        for (fixture in villainBody.fixtureList) {
            fixture.userData = this
        }
    }

    fun updateVillainPositionBody() {
        this.setPosition(
            (body.position.x * GameInfo.PPM) - width / 2,
            (body.position.y * GameInfo.PPM) - height / 2)
    }

    fun villainIsWalking(delta: Float) {
        if (villainIsDead!!) {
            return
        }

        // position villain x ppm
        val positionVillainX = body.position.x * GameInfo.PPM

        if (isVillainWalkingLeft && positionVillainX <= initialVillainXPosition - 64) {
            isVillainWalkingLeft = false
            isVillainWalkingRight = true
        } else if (isVillainWalkingRight && positionVillainX >= initialVillainXPosition) {
            isVillainWalkingRight = false
            isVillainWalkingLeft = true
        }

        var velVillainX = 0f

        if (isVillainWalkingLeft) {
            velVillainX = -1f
        } else if (isVillainWalkingRight) {
            velVillainX = +1f
        }

        body.setLinearVelocity(velVillainX, body.linearVelocity.y)

        // update position villainbody
        updateVillainPositionBody()

        elapsedTime += delta
    }

    fun drawVillainAnimation(batch: SpriteBatch) {
        val currentTexture: TextureRegion
        val defaultTexture = TextureRegion(texture)

        currentTexture = if (isVillainWalkingRight) {
            walkingVillainRightAnimation!!.getKeyFrame(elapsedTime, true)
        } else if (isVillainWalkingLeft) {
            walkingVillainLeftAnimation!!.getKeyFrame(elapsedTime, true)
        } else {
            defaultTexture
        }

        batch.draw(
            currentTexture, x, y, width, height
        )
    }
}
