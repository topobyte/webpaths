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

public class TestWebPathsRelativize
{

	@Test
	public void test()
	{
		test("b", "foo/bar/a", "foo/bar/b");
		test("b/c", "foo/bar/a", "foo/bar/b/c");
		test("b/", "foo/bar/a", "foo/bar/b/");
		test("b/c/", "foo/bar/a", "foo/bar/b/c/");

		test("../car/b", "foo/bar/a", "foo/car/b");
		test("../car/b/", "foo/bar/a", "foo/car/b/");

		test("../../boo/car/b", "foo/bar/a", "boo/car/b");
		test("../../boo/car/b/", "foo/bar/a", "boo/car/b/");

		test("../b", "foo/bar/a/", "foo/bar/b");
		test("../b/c", "foo/bar/a/", "foo/bar/b/c");
		test("../b/", "foo/bar/a/", "foo/bar/b/");
		test("../b/c/", "foo/bar/a/", "foo/bar/b/c/");

		test("../../car/b", "foo/bar/a/", "foo/car/b");
		test("../../car/b/", "foo/bar/a/", "foo/car/b/");

		test("../../../boo/car/b", "foo/bar/a/", "boo/car/b");
		test("../../../boo/car/b/", "foo/bar/a/", "boo/car/b/");

		test("foo", "/", "/foo");
		test("foo/", "/", "/foo/");
		test("foo/bar", "/", "/foo/bar");
		test("foo/bar/", "/", "/foo/bar/");

		test("bar", "foo/", "foo/bar");
		test("foo/bar", "test", "foo/bar");
		test("bar", "foo/", "foo/bar");
		test("foo/bar", "fo", "foo/bar");
		test("foo/bar", "foo", "foo/bar");

		test("", "/", "/");
		test("", "foo", "foo");
		test("", "foo/bar", "foo/bar");
		test("", "foo/bar/cat", "foo/bar/cat");

		test("/", "foo", "/");
		test("../", "foo/bar", "/");
		test("../../", "foo/bar/cat", "/");
	}

	private void test(String expected, String first, String second)
	{
		WebPath path1 = WebPaths.get(first);
		WebPath path2 = WebPaths.get(second);
		WebPath relative = path1.relativize(path2);
		Assert.assertEquals(expected, relative.toString());
	}

}
