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

import android.app.Activity
import android.content._
import android.os._
import android.view.View
import android.widget._
import FindView._
import Station._

class MainActivity extends Activity with FindView {
  var station: Option[String] = None

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    startAndBindLocationService
    initializeAutoCompleter
    initializeEventHandlers
  }

  def start {
    val city = findView[EditText](R.id.main_city).getText.toString
    if (STATIONS contains city) {
      locationService match {
        case Some(service) =>
          station = Some(city)
          findView[TextView](R.id.main_text) setText city
          service.activate
          toggleUi
        case None => ()
      }
    }
    else
      Toast.makeText(this, "Hela wel iets fatsoenlijks intikken hoor.", Toast.LENGTH_LONG).show
  }

  def stop {
    locationService match {
      case Some(service) =>
        service.deactivate
        toggleUi
      case None => ()
    }
  }

  def startAndBindLocationService {
    val serviceIntent = new Intent(this, classOf[LocationService])
    startService(serviceIntent)
    bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
  }

  def initializeAutoCompleter =
    findView[AutoCompleteTextView](R.id.main_city) setAdapter new ArrayAdapter(this, R.layout.city_item, STATIONS)

  def initializeEventHandlers {
    findView[Button](R.id.main_ok).onClick { _ => start }
    findView[Button](R.id.main_stop).onClick { _ => stop }
  }

  def toggleUi {
    val active = locationService match {
      case Some(s) => s.isActive
      case None    => false
    }
    val inactiveVisibility = if (active) View.GONE else View.VISIBLE
    val activeVisibility =   if (active) View.VISIBLE else View.GONE
    find(R.id.main_city) setVisibility inactiveVisibility
    find(R.id.main_ok)   setVisibility inactiveVisibility
    find(R.id.main_text) setVisibility activeVisibility
    find(R.id.main_stop) setVisibility activeVisibility
  }

  private var locationService: Option[LocationService] = None

  val connection = new ServiceConnection {
    override def onServiceConnected(className: ComponentName, binder: IBinder) {
      locationService = Some(binder.asInstanceOf[LocationService#LocalBinder].getService)
      toggleUi
    }

    override def onServiceDisconnected(className: ComponentName) {
      locationService = None
      toggleUi
    }
  }
}
