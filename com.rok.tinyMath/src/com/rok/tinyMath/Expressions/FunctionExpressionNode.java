package com.rok.tinyMath.Expressions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.rok.tinyMath.Exceptions.ParserException;

public class FunctionExpressionNode extends ExpressionNode{

	public final static int COS = 0;
	public final static int SIN = 1;
	public final static int POW = 2;
	public final static int LOG = 3;
	public final static int ABS = 4;
	public final static int MOD = 5;
	public final static int SQRT= 6;
	public final static int CEIL= 7;
	public final static int FLOOR = 8;	

	protected int fun;
	
	public static final String[] functions = {
			"cos",
			"sin",
			"pow",
			"log",
			"abs",
			"mod",
			"sqrt",
			"ceil",
			"floor",
	};

	public FunctionExpressionNode(int fun, ExpressionNode arg1) {
		
		super();
		this.arg1=arg1;
		this.fun=fun;

	}
	
    public FunctionExpressionNode(int fun, ExpressionNode arg1, ExpressionNode arg2) {
		
		super();
		this.arg1=arg1;
		this.arg2=arg2;
		this.fun=fun;

	}
	
	@Override
	public double getValue() throws ParserException{
		try{
			switch(fun){
			
			case SIN :
				return Math.sin(arg1.getValue());
			case COS :
				return Math.cos(arg1.getValue());
			case SQRT :
				return Math.sqrt(arg1.getValue());
			case ABS:
				return Math.abs(arg1.getValue());
			case LOG:
				return Math.log(arg1.getValue());
			case FLOOR:
				return Math.floor(arg1.getValue());
			case POW:
				return Math.pow(arg1.getValue(), arg2.getValue());
			case CEIL:
				return Math.ceil(arg1.getValue());
			case MOD: 
				return Math.IEEEremainder(arg1.getValue(),arg2.getValue());
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
