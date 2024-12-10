package com.susanafigueroa.contact

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.susanafigueroa.magicalobjects.chandelier.Chandelier
import com.susanafigueroa.magicalobjects.chandelier.ChandelierManage
import com.susanafigueroa.magicalobjects.chest.Chest
import com.susanafigueroa.magicalobjects.chest.ChestManage
import com.susanafigueroa.player.Player
import com.susanafigueroa.timer.Timer
import com.susanafigueroa.villains.VillainManage

class ContactMovingPlayer(
    private val timer: Timer,
    private val chestManage: ChestManage,
    private val chandelierManage: ChandelierManage,
) : ContactListener {
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
        } else if ((fixtureA.userData is Player && fixtureB.userData is Chandelier) ||
            (fixtureB.userData is Player && fixtureA.userData is Chandelier)
        ) {
            Gdx.app.log(
                "CONTACT PLAYER WITH CHANDELIER",
                fixtureA.userData.toString() + " | " + fixtureB.userData.toString()
            )

            if (fixtureA.userData is Chandelier) {
                chandelierManage.removeChandelier(fixtureA.userData as Chandelier)
            } else {
                chandelierManage.removeChandelier(fixtureB.userData as Chandelier)
            }

            timer.plusTimer()
        } else if ((fixtureA.userData is Player && fixtureB.userData is Chest) ||
            (fixtureB.userData is Player && fixtureA.userData is Chest)
        ) {
            Gdx.app.log(
                "CONTACT PLAYER WITH CHEST",
                fixtureA.userData.toString() + " | " + fixtureB.userData.toString()
            )

            if (fixtureA.userData is Chest) {
                chestManage.removeChest(fixtureA.userData as Chest)
            } else {
                chestManage.removeChest(fixtureB.userData as Chest)
            }

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
