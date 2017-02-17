package com.rok.tinyMath.core;

public class Token {
	
	protected int tokenType;
	protected int tokenID;
	protected String sValue;
	
	public Token(int tType, int tID, String sVal){
		tokenType= tType;
		tokenID= tID;
		sValue=sVal;
	}
	
	public int getTokenType() {
		return tokenType;
	}
	public int getTokenID() {
		return tokenID;
	}
	public String getsValue() {
		return sValue;
	}

}
