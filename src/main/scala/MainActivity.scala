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

import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.view.View
import _root_.android.widget.Button
import _root_.android.widget.EditText
import _root_.android.widget.Toast
import FindView._

class MainActivity extends Activity with FindView {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    findView[Button](R.id.main_ok).onClick { _ =>
      val city = findView[EditText](R.id.main_city)
      Toast.makeText(this, city.getText.toString, Toast.LENGTH_LONG).show
    }
  }
}
