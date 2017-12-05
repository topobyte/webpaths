// Copyright 2017 Sebastian Kuerten
//
// This file is part of webpaths.
//
// webpaths is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// webpaths is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with webpaths. If not, see <http://www.gnu.org/licenses/>.

package de.topobyte.webpaths;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import de.topobyte.collections.util.ListUtil;

public class WebPaths
{

	static Splitter SPLITTER = Splitter.on('/').omitEmptyStrings();
	static Joiner JOINER = Joiner.on('/');

	public static WebPath get(String first, String... more)
	{
		WebPath result = getSingle(first);
		// if (more.length > 0) {
		// result.setDir(true);
		// }

		for (int i = 0; i < more.length; i++) {
			WebPath next = getSingle(more[i]);
			result = result.resolve(next);
			// if (i < more.length - 1) {
			// result.setDir(true);
			// }
		}

		return result;
	}

	private static WebPath getSingle(String spec)
	{
		int ups = 0;
		List<String> components = new ArrayList<>();

		List<String> parts = SPLITTER.splitToList(spec);

		if (parts.isEmpty()) {
			return new WebPath(0, new ArrayList<String>(), false);
		}

		boolean lastWasDir = false;
		for (String part : parts) {
			if (part.equals(".")) {
				// ignore dots
				lastWasDir = true;
			} else if (part.equals("..")) {
				lastWasDir = true;
				if (!components.isEmpty()) {
					ListUtil.removeLast(components);
				} else {
					ups++;
				}
			} else {
				lastWasDir = false;
				components.add(part);
			}
		}

		boolean isDir = spec.endsWith("/");
		isDir |= lastWasDir;

		return new WebPath(ups, components, isDir);
	}

}
