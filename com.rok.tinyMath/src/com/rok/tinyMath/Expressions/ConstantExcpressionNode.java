package com.rok.tinyMath.Expressions;

public class ConstantExcpressionNode extends ExpressionNode {
	
	double v;
	
	public ConstantExcpressionNode(double a){
		v=a;
	}
	
	public double getValue(){
		return v;
	}
	

}
