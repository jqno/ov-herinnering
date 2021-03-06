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

object Station {
  val LOCATIONS = Set(
    Station("Eindhoven", 51.442760, 5.479976),
    Station("Tilburg", 51.560713, 5.083459),
    Station("Tilburg Universiteit", 51.564940, 5.051840),
    Station("Tilburg Reeshof", 51.573867, 4.993110)
  )

  val STATIONS = LOCATIONS.map(_.name).toArray.sortWith(_ < _)
}

case class Station(
  val name: String,
  val latitude: Double,
  val longitude: Double
);
