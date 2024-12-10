package com.susanafigueroa.contactplayervillain

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.susanafigueroa.Player.Player
import com.susanafigueroa.timer.Timer
import com.susanafigueroa.villains.VillainManage

class ContactPlayerVillain (
    private var timer: Timer
) : ContactListener {

    override fun beginContact(contact: Contact?) {
        val fixtureA = contact?.fixtureA
        val fixtureB = contact?.fixtureB

        if (fixtureA != null && fixtureB != null) {
            if ((fixtureA.userData is Player && fixtureB.userData is VillainManage) ||
                (fixtureB.userData is Player && fixtureA.userData is VillainManage)) {
                Gdx.app.log(
                    "CONTACT VILLAIN WITH PLAYER",
                    fixtureA.userData.toString() + fixtureB.userData.toString()
                )
                timer.reduceTimer()
            }
        }
    }

    override fun endContact(contact: Contact?) {

    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {

    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {

    }
}
