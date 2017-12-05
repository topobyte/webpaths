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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import de.topobyte.collections.util.ListUtil;

public class NioPaths
{

	public static WebPath convert(Path path, boolean isDir)
	{
		int ups = 0;
		List<String> comps = new ArrayList<>();
		for (Path part : path) {
			String p = part.toString();
			if (p.equals(".")) {
				// do nothing
			} else if (p.equals("..")) {
				if (comps.isEmpty()) {
					ups += 1;
				} else {
					ListUtil.removeLast(comps);
				}
			} else {
				comps.add(p);
			}
		}
		return new WebPath(ups, comps, isDir);
	}

	public static Path resolve(Path path, WebPath relative)
	{
		Path p = path;
		for (int i = 0; i < relative.getUps(); i++) {
			p = p.getParent();
		}
		for (String part : relative) {
			p = p.resolve(part);
		}
		return p;
	}

}
