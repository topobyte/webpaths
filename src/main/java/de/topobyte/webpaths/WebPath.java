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
		int min = Math.min(components.size(), other.components.size());
		int equal = 0;
		for (int i = 0; i < min; i++) {
			if (components.get(i).equals(other.components.get(i))) {
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

}
