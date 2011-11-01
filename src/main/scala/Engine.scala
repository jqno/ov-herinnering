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

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context._
import android.content.Intent

class Engine(context: Context, state: State) {
  private var locationListeners: Set[LocationListener] = Set()
  private val NOTIFICATION_ID = 1337

  def activate(station: String) {
    state.setStation(station)
    scheduleAlarm
  }

  def deactivate {
    cancelAlarm
    state.unsetStation
  }

  private def scheduleAlarm {
    locationListeners = LocationListener.register(context, state)
    notificationManager.notify(NOTIFICATION_ID, notification)
  }

  private def cancelAlarm {
    locationListeners foreach (_ unregister)
    notificationManager.cancel(NOTIFICATION_ID)
  }

  def notification: Notification = {
    val text = context.getText(R.string.notification_active)
    val notification = new Notification(R.drawable.app_icon, text, System.currentTimeMillis)
    val intent = PendingIntent.getActivity(context, 0, new Intent(context, classOf[MainActivity]), 0)
    notification.setLatestEventInfo(context, text, text, intent)
    notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR
    return notification
  }

  private def notificationManager: NotificationManager =
    context.getSystemService(NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]
}
