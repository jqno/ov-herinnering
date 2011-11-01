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
import android.app.Notification._
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context._
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.location.LocationManager._

class Engine(context: Context, state: State) {
  private val INTERVAL_TIME     = 60000  // milliseconds
  private val INTERVAL_DISTANCE = 100.0f // meters
  private val THRESHOLD         = 100.0f // meters

  private val TRAVELLING_NOTIFICATION_ID = 1337
  private val ARRIVED_NOTIFICATION_ID    = 1338

  private var locationListeners: Set[LocationListener] = Set()

  def activate(station: String) {
    state.setStation(station)
    registerListeners
    setTravellingNotification
  }

  def deactivate {
    clearTravellingNotification
    locationListeners foreach (locationManager removeUpdates _)
    state.unsetStation
  }

  private def registerListeners {
    val providers = Set(GPS_PROVIDER, NETWORK_PROVIDER)
    locationListeners = providers map { provider =>
      val listener = new LocationListener(context, updateLocation)
      locationManager.requestLocationUpdates(provider, INTERVAL_TIME, INTERVAL_DISTANCE, listener)
      listener
    }
  }

  private def updateLocation(location: Location) {
    if ((location distanceTo state.location) < THRESHOLD) {
      clearTravellingNotification
      setArrivedNotification
    }
  }

  private def setTravellingNotification {
    val text = context.getText(R.string.notification_active)
    val notification = buildNotification(text, text, FLAG_ONGOING_EVENT | FLAG_NO_CLEAR)
    notificationManager.notify(TRAVELLING_NOTIFICATION_ID, notification)
  }

  private def clearTravellingNotification {
    notificationManager.cancel(TRAVELLING_NOTIFICATION_ID)
  }

  private def setArrivedNotification {
    val text = context.getText(R.string.notification_arrived)
    val title = context.getText(R.string.notification_arrived_title)
    val notification = buildNotification(text, title, FLAG_AUTO_CANCEL | FLAG_INSISTENT | FLAG_SHOW_LIGHTS)
    notification.defaults |= DEFAULT_ALL
    notificationManager.notify(ARRIVED_NOTIFICATION_ID, notification)
  }

  private def buildNotification(text: CharSequence, title: CharSequence, flags: Int): Notification = {
    val notification = new Notification(R.drawable.app_icon, text, System.currentTimeMillis)
    val intent = PendingIntent.getActivity(context, 0, new Intent(context, classOf[MainActivity]), 0)
    notification.setLatestEventInfo(context, title, text, intent)
    notification.flags |= flags
    notification
  }

  private lazy val locationManager: LocationManager =
    context.getSystemService(Context.LOCATION_SERVICE).asInstanceOf[LocationManager]

  private lazy val notificationManager: NotificationManager =
    context.getSystemService(NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]
}
