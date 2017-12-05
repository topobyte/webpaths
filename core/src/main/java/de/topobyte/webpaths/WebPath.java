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
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import de.topobyte.collections.util.ListUtil;

public class WebPath implements Iterable<String>
{

	static Splitter SPLITTER = Splitter.on('/').omitEmptyStrings();
	static Joiner JOINER = Joiner.on('/');

	private int ups;
	private boolean isDir;
	private List<String> components = new ArrayList<>();

	public WebPath(int ups, Iterable<String> components, boolean isDir)
	{
		this.ups = ups;
		this.isDir = isDir;
		Iterables.addAll(this.components, components);
	}

	public int getUps()
	{
		return ups;
	}

	public boolean isDir()
	{
		return isDir;
	}

	void setDir(boolean isDir)
	{
		this.isDir = isDir;
	}

	public int getNameCount()
	{
		return components.size();
	}

	public String getName(int i)
	{
		return components.get(i);
	}

	public String getFileName()
	{
		return ListUtil.last(components);
	}

	@Override
	public Iterator<String> iterator()
	{
		return components.iterator();
	}

	public WebPath resolve(String other)
	{
		WebPath tOther = WebPaths.get(other);
		return resolve(tOther);
	}

	public WebPath resolve(WebPath other)
	{
		int remainingUps;
		int otherUps = other.getUps();
		List<String> comps = new ArrayList<>();
		if (otherUps == components.size()) {
			remainingUps = 0;
		} else if (otherUps > components.size()) {
			remainingUps = otherUps - components.size();
		} else {
			remainingUps = 0;
			int n = components.size() - otherUps;
			comps.addAll(components.subList(0, n));
		}

		if (!isDir && !comps.isEmpty()) {
			ListUtil.removeLast(comps);
		}

		int ups = this.ups + remainingUps;
		Iterables.addAll(comps, other);
		return new WebPath(ups, comps, other.isDir());
	}

	public WebPath relativize(WebPath other)
	{
		if (ups != other.ups) {
			int remaining = other.ups - Math.min(ups, other.ups);
			return new WebPath(remaining, other.components, other.isDir);
		}

		int n1 = components.size();
		int n2 = other.components.size();

		int min = Math.min(n1, n2);

		int equal = 0;
		for (int i = 0; i < min; i++) {
			boolean compIsDir1 = i < n1 - 1 | isDir;
			boolean compIsDir2 = i < n2 - 1 | other.isDir;
			String comp1 = components.get(i);
			String comp2 = other.components.get(i);
			if (comp1.equals(comp2) && compIsDir1 == compIsDir2) {
				equal += 1;
			} else {
				break;
			}
		}

		int nup;
		if (isDir) {
			nup = components.size() - equal;
		} else {
			nup = components.size() - equal - 1;
		}
		List<String> comps = other.components.subList(equal,
				other.components.size());
		return new WebPath(nup, comps, other.isDir);
	}

	@Override
	public String toString()
	{
		StringBuilder buffer = new StringBuilder();
		if (ups > 0) {
			buffer.append("..");
		}
		for (int i = 1; i < ups; i++) {
			buffer.append("/..");
		}
		if (ups > 0 && !components.isEmpty()) {
			buffer.append("/");
		}
		JOINER.appendTo(buffer, components);
		if (isDir) {
			buffer.append("/");
		}
		return buffer.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + components.hashCode();
		result = prime * result + (isDir ? 1231 : 1237);
		result = prime * result + ups;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		WebPath other = (WebPath) obj;
		if (isDir != other.isDir) {
			return false;
		}
		if (ups != other.ups) {
			return false;
		}
		if (!components.equals(other.components)) {
			return false;
		}
		return true;
	}

	public WebPath subPathFrom(int from)
	{
		if (from < 0) {
			throw new IllegalArgumentException();
		}
		if (from > ups + components.size()) {
			throw new IllegalArgumentException();
		}

		if (from == 0) {
			return this;
		}

		List<String> parts = new ArrayList<>();
		parts.addAll(components);

		if (from <= ups) {
			int rem = ups - from;
			return new WebPath(rem, parts, isDir);
		}

		int rem = from - ups;
		if (rem == parts.size()) {
			return new WebPath(0, new ArrayList<String>(), false);
		}

		return new WebPath(0, parts.subList(rem, parts.size()), isDir);
	}

	public WebPath subPathTo(int to)
	{
		if (to <= 0) {
			throw new IllegalArgumentException();
		}
		if (to > ups + components.size()) {
			throw new IllegalArgumentException();
		}

		if (to <= ups) {
			return new WebPath(to, new ArrayList<String>(), true);
		}

		List<String> parts = new ArrayList<>();
		parts.addAll(components);

		int rem = to - ups;

		if (rem < parts.size()) {
			return new WebPath(ups, parts.subList(0, rem), true);
		}
		return new WebPath(ups, parts, isDir);
	}

}
