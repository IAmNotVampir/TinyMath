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
		LexicalTokenizer tok = new LexicalTokenizer("func jk(a,b)=a+1"); 
		Token t;
		do
		{
			t = tok.next();
			System.out.println(t);
		}while(t.getTokenType()!=TType.EOL);
		
	}

}
