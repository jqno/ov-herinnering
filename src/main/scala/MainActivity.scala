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
import android.os.Bundle
import android.view.View
import android.widget._
import FindView._
import Station._

class MainActivity extends Activity with FindView {
  var active = false
  var station: Option[String] = None

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    val city = findView[AutoCompleteTextView](R.id.main_city)
    city setAdapter new ArrayAdapter(this, R.layout.city_item, STATIONS)

    findView[Button](R.id.main_ok).onClick { _ => start }
    findView[Button](R.id.main_stop).onClick { _ => stop }
  }

  def start {
    val city = findView[EditText](R.id.main_city).getText.toString
    if (STATIONS contains city) {
      station = Some(city)
      findView[TextView](R.id.main_text) setText city
      active = true
      toggle
    }
    else
      Toast.makeText(this, "Hela wel iets fatsoenlijks intikken hoor.", Toast.LENGTH_LONG).show
  }

  def stop {
    active = false
    toggle
  }

  def toggle {
    val inactiveVisibility = if (active) View.GONE else View.VISIBLE
    val activeVisibility =   if (active) View.VISIBLE else View.GONE
    find(R.id.main_city) setVisibility inactiveVisibility
    find(R.id.main_ok)   setVisibility inactiveVisibility
    find(R.id.main_text) setVisibility activeVisibility
    find(R.id.main_stop) setVisibility activeVisibility
  }
}
