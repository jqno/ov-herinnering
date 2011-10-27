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
import android.os._
import android.view.View
import android.widget._
import java.util.Calendar
import FindView._
import Station._

class MainActivity extends Activity with FindView {
  var station: Option[String] = None
  var active = false

  private val ACTIVE_ID = "active"
  private val STATION_ID = "station"
  private val NOTIFICATION_ID = 1337
  private val REQUEST_CODE = 12345
  private val INTERVAL_MINUTE = 60000

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    initializeAutoCompleter
    initializeEventHandlers
  }

  override def onResume {
    super.onResume

    val prefs = getPreferences(MODE_PRIVATE)
    active = prefs.getBoolean(ACTIVE_ID, false)
    station = Some(prefs.getString(STATION_ID, null))
    toggleUi
  }

  override def onPause {
    super.onPause

    val editor = getPreferences(MODE_PRIVATE).edit
    editor.putBoolean(ACTIVE_ID, active)
    station foreach (editor.putString(STATION_ID, _))
    editor.commit
  }

  def start {
    val city = findView[EditText](R.id.main_city).getText.toString
    if (STATIONS contains city) {
      station = Some(city)
      scheduleAlarm
      active = true
      toggleUi
    }
    else
      Toast.makeText(this, "Hela wel iets fatsoenlijks intikken hoor.", Toast.LENGTH_LONG).show
  }

  def stop {
    cancelAlarm
    active = false
    toggleUi
  }

  def scheduleAlarm {
    val cal = Calendar.getInstance
    cal.add(Calendar.SECOND, 5)
    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis, INTERVAL_MINUTE, broadcastIntent)
    notificationManager.notify(NOTIFICATION_ID, notification)
  }

  def cancelAlarm {
    alarmManager.cancel(broadcastIntent)
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

  def broadcastIntent: PendingIntent = {
    val intent = new Intent(this, classOf[AlarmReceiver])
    station foreach (intent.putExtra("station", _))
    PendingIntent.getBroadcast(this, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
  }

  def initializeAutoCompleter {
    findView[AutoCompleteTextView](R.id.main_city) setAdapter new ArrayAdapter(this, R.layout.city_item, STATIONS)
  }

  def initializeEventHandlers {
    findView[Button](R.id.main_ok).onClick { _ => start }
    findView[Button](R.id.main_stop).onClick { _ => stop }
  }

  private def alarmManager: AlarmManager =
    getSystemService(ALARM_SERVICE).asInstanceOf[AlarmManager]

  private def notificationManager: NotificationManager =
    getSystemService(NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]

  def toggleUi {
    find(R.id.main_active)   setVisibility (if (active) View.VISIBLE else View.GONE)
    find(R.id.main_inactive) setVisibility (if (active) View.GONE else View.VISIBLE)
    station foreach (findView[TextView](R.id.main_text) setText _)
  }
}
