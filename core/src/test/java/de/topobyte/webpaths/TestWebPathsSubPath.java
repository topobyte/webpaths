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

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestWebPathsSubPath
{

	final static Logger logger = LoggerFactory
			.getLogger(TestWebPathsSubPath.class);

	@Test
	public void testFrom()
	{
		testFrom("foo/bar/a", "foo/bar/a", 0);
		testFrom("bar/a", "foo/bar/a", 1);
		testFrom("a", "foo/bar/a", 2);
		testFrom("", "foo/bar/a", 3);

		testFrom("foo/bar/a/", "foo/bar/a/", 0);
		testFrom("bar/a/", "foo/bar/a/", 1);
		testFrom("a/", "foo/bar/a/", 2);
		testFrom("", "foo/bar/a/", 3);

		testFromFail("foo/bar/a", 4);
		testFromFail("foo/bar/a", 5);
		testFromFail("foo/bar/a", -1);
	}

	@Test
	public void testTo()
	{
		testTo("foo/", "foo/bar/a", 1);
		testTo("foo/bar/", "foo/bar/a", 2);
		testTo("foo/bar/a", "foo/bar/a", 3);

		testTo("foo/", "foo/bar/a/", 1);
		testTo("foo/bar/", "foo/bar/a/", 2);
		testTo("foo/bar/a/", "foo/bar/a/", 3);

		testToFail("foo/bar/a", 0);
		testToFail("foo/bar/a", 4);
		testToFail("foo/bar/a", 5);
		testToFail("foo/bar/a", -1);
	}

	private void testFrom(String expected, String spec, int from)
	{
		logger.debug(String.format("spec:     '%s', from: %d, expected: '%s'",
				spec, from, expected));
		WebPath path = WebPaths.get(spec);
		logger.debug(String.format("path:     '%s'", path));
		WebPath subpath = path.subPathFrom(from);
		logger.debug(String.format("subpath:  '%s'", subpath));
		Assert.assertEquals(expected, subpath.toString());
	}

	private void testTo(String expected, String spec, int to)
	{
		WebPath path = WebPaths.get(spec);
		WebPath subpath = path.subPathTo(to);
		Assert.assertEquals(expected, subpath.toString());
	}

	private void testFromFail(String spec, int from)
	{
		WebPath path = WebPaths.get(spec);
		try {
			path.subPathFrom(from);
			Assert.assertTrue(false);
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
	}

	private void testToFail(String spec, int to)
	{
		WebPath path = WebPaths.get(spec);
		try {
			path.subPathTo(to);
			Assert.assertTrue(false);
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
	}

}
