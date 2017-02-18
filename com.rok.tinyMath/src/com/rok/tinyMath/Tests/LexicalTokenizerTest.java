package com.rok.tinyMath.Tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rok.tinyMath.Exceptions.ParserException;
import com.rok.tinyMath.Parser.LexicalTokenizer;
import com.rok.tinyMath.Parser.Token;
import com.rok.tinyMath.Parser.Token.TType;

public class LexicalTokenizerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws ParserException {
		LexicalTokenizer tok = new LexicalTokenizer("cos(33.33)+mod(-5)*sadkgl+8+mod(xy,y,zjk)-asdlfh"); 
		Token t;
		do
		{
			t = tok.next();
			System.out.println(t);
		}while(t.getTokenType()!=TType.EOL);
		
	}

}
