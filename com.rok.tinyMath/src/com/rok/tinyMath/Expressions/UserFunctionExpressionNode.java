package com.rok.tinyMath.Expressions;

import java.io.Serializable;
import java.util.List;

import com.rok.tinyMath.Exceptions.ParserException;
import com.rok.tinyMath.Parser.UserFunction;

public class UserFunctionExpressionNode extends ExpressionNode{
	
	public List<UserExpressionNode> arg;
	public ExpressionNode body;
	public List<ExpressionNode> input;
	
	public UserFunctionExpressionNode(UserFunction fun,List<ExpressionNode> input) throws ParserException{
		
		this.arg=fun.getArg();
		this.body=fun.getBody();
		this.input=input;
		
		if (this.arg.size()!=this.input.size()){
			throw new ParserException("mismatch arg and input size");		
		}
		
	}
	
	@Override
	public double getValue() throws ParserException{
		
		for(int i = 0; i<arg.size(); i++){
			arg.get(i).setExp(input.get(i));
		}
		
		return body.getValue();
		
	}

}
