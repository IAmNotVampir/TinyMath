package com.rok.tinyMath.Expressions;

public class ConstantExpressionNode extends ExpressionNode {
	
	double v;
	
	public ConstantExpressionNode(double a){
		v=a;
	}
	
	public double getValue(){
		return v;
	}
	

}
