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
	}

	private void test(String expected, String first, String second)
	{
		WebPath path1 = WebPaths.get(first);
		WebPath path2 = WebPaths.get(second);
		WebPath relative = path1.relativize(path2);
		Assert.assertEquals(expected, relative.toString());
	}

}
