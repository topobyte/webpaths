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

public class TestWebPathsGet
{

	@Test
	public void test()
	{
		test("test/foo", "test/", "foo");
		test("test/foo/bar", "test/", "foo/", "bar");

		test("test/foo/", "test/", "foo/");
		test("test/foo/bar/", "test/", "foo/", "bar/");

		test("foo", "test", "foo");
		test("bar", "test", "foo", "bar");

		test("foo/", "test", "foo/");
		test("bar/", "test", "foo", "bar/");

		test("/", "test", ".");
		test("/", "test", ".", ".");
		test("test/", "test/", ".");
		test("test/", "test/", ".", ".");

		test("/", "test/foo", "..");
		test("test/", "test/foo/", "..");
	}

	private void test(String expected, String first, String... more)
	{
		WebPath path = WebPaths.get(first, more);
		Assert.assertEquals(expected, path.toString());
	}

}
