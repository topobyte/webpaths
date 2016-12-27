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

		for (String m : more) {
			result = result.resolve(getSingle(m));
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
