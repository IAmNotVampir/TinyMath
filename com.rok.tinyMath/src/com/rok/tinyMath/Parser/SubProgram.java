package com.rok.tinyMath.Parser;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.rok.tinyMath.Exceptions.ParserException;
import com.rok.tinyMath.Expressions.ConstantExpressionNode;
import com.rok.tinyMath.Expressions.ExpressionNode;
import com.rok.tinyMath.Expressions.UserExpressionNode;
import com.rok.tinyMath.Parser.Token.TType;

public class SubProgram extends Program {
	
	Program topProgram;
	String name;
	Map<String,UserExpressionNode> arg = new LinkedHashMap<>();
	LexicalTokenizer lt;
	
	public SubProgram(Program p, String programText) throws ParserException{
		
		topProgram=p;
		lt = new LexicalTokenizer(programText);
	}
	
	@Override
	protected ExpressionNode getConstant(Token t){
		ExpressionNode exp = arg.get(t.sValue);
		if (exp==null) {
			return topProgram.getConstant(t);
		}else{
			return exp;
		}
	}
	
	@Override
	public UserFunction getFunction(String str){
		
		return topProgram.getFunction(str);
		
	}

	public UserFunction parseSubProgram() throws ParserException{
		
		ExpressionNode cup = new ConstantExpressionNode(0);
		if (lt.match(TType.FUNCTION_DEC)){
			name=lt.getCurToken().getsValue();
			if (lt.match(TType.UNKNOW)){
				if (lt.match(TType.OPEN_BRACKET)){
					Token t;
					//разбор аргументов функции
					do
					{
						t = lt.getCurToken();
						if(lt.match(TType.UNKNOW)){
							arg.put(t.getsValue(), new UserExpressionNode(cup));
						}else{
							throw new ParserException("Incorrect function decloration");
						}
					}while(lt.match(TType.DELIMETER));
					if (lt.match(TType.CLOSE_BRACKET)){
						if (lt.match(TType.EQUAL)){
							ExpressionNode body = startParse(lt);
							List<UserExpressionNode> l = new LinkedList<>();
							for(Map.Entry<String,UserExpressionNode> e : arg.entrySet()){
								l.add(e.getValue());
							}
							return new UserFunction(l,body,name);
						}
					}
				}
			}
			throw new ParserException("Incorrect function decloration");
		}
		return null;
		
	}
}
