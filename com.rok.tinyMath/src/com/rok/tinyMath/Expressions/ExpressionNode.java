package com.rok.tinyMath.Expressions;

import java.security.cert.Extension;
import java.util.HashMap;
import java.util.Map;

import com.rok.tinyMath.Exceptions.ParserException;

public class ExpressionNode {
	
	
		final static int  OP_ADD = 0;   // Addition '+'
		final static int  OP_SUB = 1;   // Subtraction '-'
		final static int  OP_MUL = 2;   // Multiplication '*'
		final static int  OP_DIV = 3;   // Division '/'
	
    
	protected ExpressionNode arg1,arg2;
	protected int oper;
	public static char[] operators = {
			'+',
			'-',
			'*',
			'/',	
	};
	

	
	public ExpressionNode(int oper, ExpressionNode arg1, ExpressionNode arg2)
	{
		this.arg1=arg1;
		this.arg2=arg2;
		this.oper=oper;
	}
	
	public ExpressionNode() {
		// TODO Auto-generated constructor stub
	}

	public double getValue() throws ParserException {
		
		try
		{

			switch(oper){
			case OP_ADD:
				return arg1.getValue() + arg2.getValue();
			case OP_SUB:
				return arg1.getValue() - arg2.getValue();	
			case OP_MUL:	
				return arg1.getValue()*arg2.getValue();	
			case OP_DIV:	
				return arg1.getValue()/arg2.getValue();	
			default:
				throw new ParserException("Unknow operator");
			}
		} catch (ParserException e){
			throw e;
		} catch (Exception e){
			throw new ParserException("Arithmetic Exception.");
		}
		
	}

}
