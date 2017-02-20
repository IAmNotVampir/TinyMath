package com.rok.tinyMath.Parser;

import java.io.Serializable;
import java.util.List;

import com.rok.tinyMath.Expressions.ExpressionNode;
import com.rok.tinyMath.Expressions.UserExpressionNode;

public class UserFunction implements Serializable{
	
	//выражение содержащее тело функции
	public ExpressionNode body;
	//
	public List<UserExpressionNode> arg;
	private String name;
	
	public UserFunction(List<UserExpressionNode> arg, ExpressionNode body,String name){
		this.arg=arg;
		this.body=body;
		this.name=name;
	}
	
	public ExpressionNode getBody() {
		return body;
	}

	public void setBody(ExpressionNode body) {
		this.body = body;
	}

	public List<UserExpressionNode> getArg() {
		return arg;
	}

	public void setArg(List<UserExpressionNode> arg) {
		this.arg = arg;
	}
	
	public int getArgNumber(){
		return arg.size();
	}
	
	public String getName(){
		return name;
	}

}
