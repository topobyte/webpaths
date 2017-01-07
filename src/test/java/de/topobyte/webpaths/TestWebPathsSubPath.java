package de.topobyte.webpaths;

import org.junit.Assert;
import org.junit.Test;

public class TestWebPathsSubPath
{

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
		WebPath path = WebPaths.get(spec);
		WebPath subpath = path.subPathFrom(from);
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
