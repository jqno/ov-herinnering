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

import android.content.SharedPreferences
import android.location.Location
import Stations._

class State(prefs: SharedPreferences) {
  private var station: Option[String] = None
  private val STATION_ID = "station"

  def isActive: Boolean = station isDefined

  def stationText: String = station getOrElse ""

  def isValidStation(s: String): Boolean = STATIONS contains s
  
  def location: Location = {
    if (station isEmpty)
      throw new IllegalStateException("No station set")
    val ref = LOCATIONS(station.get)
    val result = new Location("ov-herinnering")
    result setLatitude ref._1
    result setLongitude ref._2
    result
  }

  def setStation(s: String) {
    station = Some(s)
  }

  def unsetStation {
    station = None
  }

  def restore {
    station = Option(prefs.getString(STATION_ID, null))
  }

  def save {
    val editor = prefs.edit
    editor.putString(STATION_ID, station getOrElse null)
    editor.commit
  }
}
