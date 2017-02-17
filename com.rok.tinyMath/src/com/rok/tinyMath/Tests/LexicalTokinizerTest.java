package com.rok.tinyMath.Tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rok.tinyMath.Exceptions.ParserException;
import com.rok.tinyMath.core.LexicalTokenizer;
import com.rok.tinyMath.core.Token;

public class LexicalTokinizerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws ParserException {
		LexicalTokenizer tok = new LexicalTokenizer("cos(33.33)+mod(-5)*sadkgl+8"); 
		Token t = tok.getNextToken();
		while(t.getTokenType()!=LexicalTokenizer.EOL){
			System.out.println(t.getsValue());
			t = tok.getNextToken();
		}
		
	}

}
