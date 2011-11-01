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

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.widget.Toast

class LocationListener(context: Context, update: Location => Unit) extends android.location.LocationListener {
  override def onLocationChanged(location: Location) {
    update(location)
  }

  override def onProviderDisabled(provider: String) {
    Toast.makeText(context, "disabled: " + provider, Toast.LENGTH_SHORT).show
  }

  override def onProviderEnabled(provider: String) {
    Toast.makeText(context, "enabled: " + provider, Toast.LENGTH_SHORT).show
  }

  override def onStatusChanged(provider: String, status: Int, extras: Bundle) {}
}
