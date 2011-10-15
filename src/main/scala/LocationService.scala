/*
 * OV-Herinnering is an Android app that asks for a train station,
 * and gives a notification when it is reached.
 *
 * Copyright (C) 2011 by Jan Ouwens
 *
 * This file is part of OV-Herinnering.
 *
 * OV-Herinnering is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OV-Herinnering is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OV-Herinnering.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.jqno.ovherinnering

import android.app._
import android.content.Intent
import android.os._
import android.widget.Toast

class LocationService extends Service {
  private val ID = 1337

  private var active = false

  def isActive: Boolean = active

  def activate {
    startForeground(ID, notification)
    active = true
  }

  def deactivate {
    active = false
    stopForeground(true)
  }

  def notification: Notification = {
    val text = getText(R.string.notification_active)
    val n = new Notification(R.drawable.app_icon, text, System.currentTimeMillis)
    val i = PendingIntent.getActivity(this, 0, new Intent(this, classOf[MainActivity]), 0)
    n.setLatestEventInfo(this, text, text, i)
    return n
  }

  override def onStartCommand(intent: Intent, flags: Int, startId: Int): Int =
    Service.START_STICKY

  class LocalBinder extends Binder {
    def getService: LocationService = LocationService.this
  }

  val binder = new LocalBinder

  override def onBind(intent: Intent): IBinder = binder
}
