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

import android.content._
import android.location._
import android.os.Bundle
import android.widget.Toast
import LocationListener._
import Stations._

object LocationListener {
  private val INTERVAL_TIME = 60000 // milliseconds
  private val INTERVAL_DISTANCE = 100.0f // meters
  private val THRESHOLD = 100.0f

  def register(context: Context, city: String): Option[LocationListener] = {
    val lm = locationManager(context)
    val provider = lm.getBestProvider(criteria, true)
    if (provider == null) return None

    val listener = new LocationListener(context, city)
    lm.requestLocationUpdates(provider, INTERVAL_TIME, INTERVAL_DISTANCE, listener)
    Some(listener)
  }

  private def locationManager(context: Context): LocationManager =
    context.getSystemService(Context.LOCATION_SERVICE).asInstanceOf[LocationManager]

  private def criteria: Criteria = new Criteria

  def locationFor(city: String): Location = {
    val ref = LOCATIONS(city)
    val result = new Location("ov-herinnering")
    result setLatitude ref._1
    result setLongitude ref._2
    result
  }
}

class LocationListener(context: Context, city: String) extends android.location.LocationListener {
  val refLocation = LocationListener.locationFor(city)

  def unregister = {
    locationManager(context) removeUpdates this
  }

  override def onLocationChanged(location: Location) {
    if ((location distanceTo refLocation) < THRESHOLD) {
      //notify
      //unregister
    }
  }

  override def onProviderDisabled(provider: String) {
    Toast.makeText(context, "Kan geen locatie-updates ontvangen!", Toast.LENGTH_SHORT).show
  }

  override def onProviderEnabled(provider: String) {}
  override def onStatusChanged(provider: String, status: Int, extras: Bundle) {}
}
