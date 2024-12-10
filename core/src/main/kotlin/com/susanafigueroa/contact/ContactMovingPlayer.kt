package com.susanafigueroa.contact

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.susanafigueroa.magicalobjects.chandelier.ChandelierManage
import com.susanafigueroa.magicalobjects.chest.ChestManage
import com.susanafigueroa.player.Player
import com.susanafigueroa.timer.Timer
import com.susanafigueroa.villains.VillainManage

class ContactMovingPlayer(private val timer: Timer) : ContactListener {
    override fun beginContact(contact: Contact) {
        val fixtureA = contact.fixtureA
        val fixtureB = contact.fixtureB

        if ((fixtureA.userData is Player && fixtureB.userData is VillainManage) ||
            (fixtureB.userData is Player && fixtureA.userData is VillainManage)
        ) {
            Gdx.app.log(
                "CONTACT VILLAIN WITH PLAYER",
                fixtureA.userData.toString() + fixtureB.userData.toString()
            )
            timer.reduceTimer()

        } else if ((fixtureA.userData is Player && fixtureB.userData is ChandelierManage) ||
            (fixtureB.userData is Player && fixtureA.userData is ChandelierManage)
        ) {
            Gdx.app.log(
                "CONTACT PLAYER WITH CHANDELIER",
                fixtureA.userData.toString() + " | " + fixtureB.userData.toString()
            )
            timer.plusTimer()

        } else if ((fixtureA.userData is Player && fixtureB.userData is ChestManage) ||
            (fixtureB.userData is Player && fixtureA.userData is ChestManage)
        ) {
            Gdx.app.log(
                "CONTACT PLAYER WITH CHEST",
                fixtureA.userData.toString() + " | " + fixtureB.userData.toString()
            )
            timer.plusTimer()
        }
    }

    override fun endContact(contact: Contact) {
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold) {
    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse) {
    }
}
