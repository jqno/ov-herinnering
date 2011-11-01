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
import android.content.Context._
import android.location._
import android.os._
import android.view.View
import android.widget._
import java.util.Calendar
import FindView._
import Stations._

class MainActivity extends Activity with FindView {
  lazy val state = new State(getPreferences(MODE_PRIVATE))
  var locationListeners: Set[LocationListener] = Set()

  private val NOTIFICATION_ID = 1337
  private val REQUEST_CODE = 12345

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    initializeAutoCompleter
    initializeEventHandlers
  }

  override def onResume {
    super.onResume
    state.restore
    toggleUi
  }

  override def onPause {
    super.onPause
    state.save
  }

  def start {
    val city = findView[EditText](R.id.main_city).getText.toString
    if (STATIONS contains city) {
      state.activate(city)
      scheduleAlarm
      toggleUi
    }
    else
      Toast.makeText(this, "Hela wel iets fatsoenlijks intikken hoor.", Toast.LENGTH_LONG).show
  }

  def stop {
    cancelAlarm
    state.deactivate
    toggleUi
  }

  def scheduleAlarm {
    locationListeners = LocationListener.register(this, state)
    notificationManager.notify(NOTIFICATION_ID, notification)
  }

  def cancelAlarm {
    locationListeners foreach (_ unregister)
    notificationManager.cancel(NOTIFICATION_ID)
  }

  def notification: Notification = {
    val text = getText(R.string.notification_active)
    val notification = new Notification(R.drawable.app_icon, text, System.currentTimeMillis)
    val intent = PendingIntent.getActivity(this, 0, new Intent(this, classOf[MainActivity]), 0)
    notification.setLatestEventInfo(this, text, text, intent)
    notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR
    return notification
  }

  def initializeAutoCompleter {
    findView[AutoCompleteTextView](R.id.main_city) setAdapter new ArrayAdapter(this, R.layout.city_item, STATIONS)
  }

  def initializeEventHandlers {
    findView[Button](R.id.main_ok).onClick { _ => start }
    findView[Button](R.id.main_stop).onClick { _ => stop }
  }

  private def notificationManager: NotificationManager =
    getSystemService(NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]

  def toggleUi {
    val a = state.isActive
    find(R.id.main_active)   setVisibility (if (a) View.VISIBLE else View.GONE)
    find(R.id.main_inactive) setVisibility (if (a) View.GONE else View.VISIBLE)
    findView[TextView](R.id.main_text).setText(state stationText)
  }
}
