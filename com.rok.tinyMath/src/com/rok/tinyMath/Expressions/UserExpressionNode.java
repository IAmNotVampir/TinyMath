package com.rok.tinyMath.Expressions;

import java.io.Serializable;

import com.rok.tinyMath.Exceptions.ParserException;

public class UserExpressionNode extends ExpressionNode{
	
	ExpressionNode exp;
	
	public UserExpressionNode(ExpressionNode exp){
		
		this.exp=exp;
		
	}
	
	@Override
	public double getValue() throws ParserException {
		
		return exp.getValue();
		
	}
	
	public void setExp(ExpressionNode exp){
		this.exp=exp;
	}

}
