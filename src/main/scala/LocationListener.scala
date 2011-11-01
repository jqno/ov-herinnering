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
import android.content._
import android.location._
import android.os.Bundle
import android.widget.Toast
import LocationListener._
import LocationManager._
import Notification._
import Stations._

object LocationListener {
  private val INTERVAL_TIME = 60000 // milliseconds
  private val INTERVAL_DISTANCE = 100.0f // meters
  private val THRESHOLD = 100.0f

  def register(context: Context, state: State): Set[LocationListener] = {
    val lm = locationManager(context)
    val providers = Set(GPS_PROVIDER, NETWORK_PROVIDER)
    providers map { provider =>
      val listener = new LocationListener(context, state, provider)
      lm.requestLocationUpdates(provider, INTERVAL_TIME, INTERVAL_DISTANCE, listener)
      listener
    }
  }

  private def locationManager(context: Context): LocationManager =
    context.getSystemService(Context.LOCATION_SERVICE).asInstanceOf[LocationManager]
}

class LocationListener(context: Context, state: State, provider: String) extends android.location.LocationListener {
  val NOTIFICATION_ID = 86

  def unregister = {
    locationManager(context) removeUpdates this
  }

  override def onLocationChanged(location: Location) {
    if ((location distanceTo state.location) < THRESHOLD) {
      notificationManager.notify(NOTIFICATION_ID, notification)
      //unregister
    }
  }

  override def onProviderDisabled(provider: String) {
    Toast.makeText(context, "disabled: " + provider, Toast.LENGTH_SHORT).show
  }

  override def onProviderEnabled(provider: String) {
    Toast.makeText(context, "enabled: " + provider, Toast.LENGTH_SHORT).show
  }

  override def onStatusChanged(provider: String, status: Int, extras: Bundle) {}

  private def notification: Notification = {
    val text = context.getText(R.string.notification_arrived)
    val title = context.getText(R.string.notification_arrived_title)
    val notification = new Notification(R.drawable.app_icon, text, System.currentTimeMillis)
    val intent = PendingIntent.getActivity(context, 0, new Intent(context, classOf[MainActivity]), 0)
    notification.setLatestEventInfo(context, title, text, intent)
    notification.defaults |= DEFAULT_ALL
    notification.flags |= FLAG_AUTO_CANCEL | FLAG_INSISTENT | FLAG_SHOW_LIGHTS
    return notification
  }

  private def notificationManager: NotificationManager =
    context.getSystemService(Context.NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]
}
