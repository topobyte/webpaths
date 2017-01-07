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

		test("test/foo", "test", "foo");
		test("test/foo/bar", "test", "foo", "bar");

		test("test/foo/", "test", "foo/");
		test("test/foo/bar/", "test", "foo", "bar/");

		test("test/", "test", ".");
		test("test/", "test", ".", ".");
		test("test/", "test/", ".");
		test("test/", "test/", ".", ".");

		test("test/", "test/foo", "..");
		test("test/", "test/foo/", "..");
	}

	private void test(String expected, String first, String... more)
	{
		WebPath path = WebPaths.get(first, more);
		Assert.assertEquals(expected, path.toString());
	}

}
