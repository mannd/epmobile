/*  EP Mobile -- Mobile tools for electrophysiologists
    Copyright (C) 2011 EP Studios, Inc.
    www.epstudiossoftware.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.epstudios.epmobile;

public class UnitConverter {
	public static double lbsToKgs(double weight) {
		final double conversionFactor = 0.45359237;
		return weight * conversionFactor;
	}

	public static double kgsToLbs(double weight) {
		final double conversionFactor = 2.20462262;
		return weight * conversionFactor;
	}

	public static double cmsToIns(double distance) {
		final double conversionFactor = 0.39370;
		return distance * conversionFactor;
	}

}
