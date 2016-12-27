package de.topobyte.webpaths;

import org.junit.Assert;
import org.junit.Test;

public class TestWebPathsResolve
{

	@Test
	public void test()
	{
		test("test", "test");
		test("test/foo", "test/foo");
		test("test/foo/", "test/foo/bar/..");
		test("test/foo/", "test/foo/.");
		test("test/foo/", "test/foo/bar/../.");
		test("test/foo/", "test/foo/bar/.././.");
		test("test/foo/", "test/./foo/bar/.././.");
		test("test/foo/", "test/././asdf/../foo/bar/.././.");
		test("test/foo/", "test/././asdf//../foo/bar/.././.");
		test("test/foo/", "test/././asdf///../foo/bar/.././.");
		test("/", ".");
		test("test/", "test/");
		test("test/asdf", "test/asdf");
		test("test/asdf/", "test/asdf/");
		test("test/asdf/", "test/asdf/.");
		test("test/asdf/", "test/asdf/./");

		test("foo", "test", "foo");
		test("test/foo", "test/", "foo");
		test("foo/", "test", "foo/");
		test("test/foo/", "test/", "foo/");
		test("/", "test/", "..");
		test("/", "test/", "../");
		test("/", "test/foo", "..");
		test("/", "test/foo", "../");
		test("test/", "test/foo", ".");

		test("foo", ".", "foo");
		test("foo", ".", ".", "foo");
		test("foo", ".", ".", ".", "foo");
		test("../", "..");
		test("../../", "../..");
		test("foo", "foo");
		test("../foo", "..", "foo");
		test("../", "foo/", "../../");
		test("../bar/", "foo/", "../../bar/");
		test("../bar", "foo/", "../../bar");

		test("bar", "foo", ".", "bar");
		test("foo/bar", "foo/", "bar");
	}

	private void test(String expected, String first, String... more)
	{
		WebPath path = WebPaths.get(first, more);
		Assert.assertEquals(expected, path.toString());
	}

}
