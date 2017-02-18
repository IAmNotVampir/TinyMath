package com.rok.tinyMath.Parser;

public class Token {
	
	public enum TType {
	EOL,
	NUMBER,
    OPERATOR,
	FUNCTION,
	DELIMETER,
	OPEN_BRACKET,
	CLOSE_BRACKET,
	UNKNOW,
	}
	
	protected TType tokenType;
	protected int tokenID;
	protected String sValue;
	
	public Token(TType tType, int tID, String sVal){
		tokenType= tType;
		tokenID= tID;
		sValue=sVal;
	}
	
	public TType getTokenType() {
		return tokenType;
	}
	public int getTokenID() {
		return tokenID;
	}
	public String getsValue() {
		return sValue;
	}
	
	@Override
	public String toString(){
		
		return "type="+tokenType+" tid="+tokenID+" str="+sValue;
		
	}

}
