package com.rok.tinyMath.Tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rok.tinyMath.Parser.Program;

public class ParserTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Program p = new Program();
		p.Execute("log(5)*2+1");
	}

}
