package com.rok.tinyMath.Expressions;

import java.io.Serializable;

public class ConstantExpressionNode extends ExpressionNode {
	
	double v;
	
	public ConstantExpressionNode(double a){
		v=a;
	}
	
	public double getValue(){
		return v;
	}
	

}
