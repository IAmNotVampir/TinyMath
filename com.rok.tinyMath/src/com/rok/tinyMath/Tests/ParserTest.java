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
		p.Execute("(1+1)*(1+1)*(1+1+abs(-5))");
		
		double answer1 = Math.sin(5)+Math.cos(5+5)+Math.sqrt(5)+Math.abs(-5)+
				Math.log(5)*Math.floor(5)*(Math.pow(5,50)+Math.ceil(
						5)+Math.IEEEremainder(5,5));
		
		double answer2 = p.Execute("sin(5)+cos(5+5)+sqrt(5)+abs(-5)+log(5)*floor(5)"
				+ "*(pow(5,50)+ceil(5)+mod(5,5))");
		assertTrue(answer1==answer2);
		
		p.Execute("1.25E+2+1,00E+2");
	}
}
