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

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class LocationService extends Service {
  override def onBind(intent: Intent): IBinder = null

  override def onCreate  = Toast.makeText(this, "CREATED", Toast.LENGTH_LONG).show
  override def onDestroy = Toast.makeText(this, "DESTROYED", Toast.LENGTH_LONG).show
  override def onStart(intent: Intent, startId: Int) =
    Toast.makeText(this, "STARTED", Toast.LENGTH_LONG).show
}
