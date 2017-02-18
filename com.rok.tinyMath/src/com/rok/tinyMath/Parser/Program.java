package com.rok.tinyMath.Parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.rok.tinyMath.Exceptions.ParserException;
import com.rok.tinyMath.Exceptions.SyntaxException;
import com.rok.tinyMath.Expressions.ConstantExpressionNode;
import com.rok.tinyMath.Expressions.ExpressionNode;
import com.rok.tinyMath.Expressions.FunctionExpressionNode;
import com.rok.tinyMath.Parser.Token.TType;
import com.rok.tinyMath.Expressions.*;

public class Program {
	
	protected Map<String,Double> constants = new HashMap<>();	

	public double Execute(String str){

		try {
			LexicalTokenizer lt = new LexicalTokenizer(str);
			ExpressionNode exp = startParse(lt);
			Double answer = exp.getValue();
			System.out.println(answer);
			return answer;
		} catch (ParserException e) {
			System.out.println(e.toString());
			return 0;
		}
	}

	private ExpressionNode startParse(LexicalTokenizer lt) throws ParserException{
		
		ExpressionNode result = parseAddSub(lt);
		if (lt.match(TType.EOL)){
			return result;
		}else{
			throw new SyntaxException();
		}
	}

	private ExpressionNode parseAddSub(LexicalTokenizer lt) throws ParserException{
		
		ExpressionNode result = parseMulDiv(lt);
		
		while(true){
			
			if (lt.match(TType.OPERATOR, ExpressionNode.OP_ADD)){			
				result=new ExpressionNode(ExpressionNode.OP_ADD,result,parseMulDiv(lt));
				continue;		
			};
			
			if (lt.match(TType.OPERATOR, ExpressionNode.OP_SUB)){			
				result=new ExpressionNode(ExpressionNode.OP_SUB,result,parseMulDiv(lt));
				continue;		
			};
			
			break;
		}
		
		return result;
		
	}
	
	private ExpressionNode parseMulDiv(LexicalTokenizer lt) throws ParserException{
		
		ExpressionNode result = parseUnary(lt);
		
		while(true){
			
			if (lt.match(TType.OPERATOR,ExpressionNode.OP_MUL)){
				result=new ExpressionNode(ExpressionNode.OP_MUL,result,parseUnary(lt));
				continue;
			}
			
			if (lt.match(TType.OPERATOR,ExpressionNode.OP_DIV)){
				result=new ExpressionNode(ExpressionNode.OP_DIV,result,parseUnary(lt));
				continue;
			}
			
			break;
		}
		
		return result;
		
	}
	
	private ExpressionNode parseUnary(LexicalTokenizer lt) throws ParserException{
		
		if (lt.match(TType.OPERATOR, ExpressionNode.OP_ADD)){			
			return parsePrimary(lt);
		};
		
		if (lt.match(TType.OPERATOR, ExpressionNode.OP_SUB)){			
			return new ExpressionNode(ExpressionNode.OP_SUB,new ConstantExpressionNode(0)
					,parsePrimary(lt));		
		};
		
		
		return parsePrimary(lt);
		
	}
	
	private ExpressionNode parsePrimary(LexicalTokenizer lt) throws ParserException{
		
		Token t = lt.getCurToken();
		
		if (lt.match(TType.FUNCTION)){
			return parseFUNCTION(t,lt);
		}
		
		if (lt.match(TType.NUMBER)){
			return parseNUMBER(t);
		}
		
		if (lt.match(TType.UNKNOW)){
			return parseUNKNOW(t);
		}
		
		if (lt.match(TType.OPEN_BRACKET)){
			
			List<Token> tokenList = parseBRACKETS(lt);	
			LexicalTokenizer lt1 = new LexicalTokenizer(tokenList); 
			return startParse(lt);
			
		}
		
		throw new RuntimeException("Unknow Expression"+t);
		
	}



	private ExpressionNode parseFUNCTION(Token prToken, LexicalTokenizer lt) throws ParserException{

		Token t;

		switch(prToken.tokenID){

		case FunctionExpressionNode.COS:
		case FunctionExpressionNode.SIN:
		case FunctionExpressionNode.SQRT:
		case FunctionExpressionNode.LOG:
		case FunctionExpressionNode.FLOOR:
		case FunctionExpressionNode.ABS:
		case FunctionExpressionNode.CEIL:

			t=lt.next();
			if (t.getTokenType()!=TType.OPEN_BRACKET){
				throw new SyntaxException();	
			}

			ExpressionNode e;
			t=lt.next();
			if(t.getTokenType()==TType.NUMBER){
				e=parseNUMBER(t);
			}else if (t.getTokenType()==TType.UNKNOW){
				e=parseUNKNOW(t);
			}else{
				throw new SyntaxException();
			}

			t=lt.next();
			if(t.getTokenType()==TType.CLOSE_BRACKET){
				return new FunctionExpressionNode(prToken.getTokenID(),e);
			}else{
				throw new SyntaxException();
			}

		case FunctionExpressionNode.POW:
		case FunctionExpressionNode.MOD:

			t=lt.next();
			if (t.getTokenType()!=TType.OPEN_BRACKET){
				throw new SyntaxException();
			}

			ExpressionNode e1;
			t=lt.next();
			if(t.getTokenType()==TType.NUMBER){
				e1=parseNUMBER(t);
			}else if (t.getTokenType()==TType.UNKNOW){
				e1=parseUNKNOW(t);
			}else{
				throw new SyntaxException();
			}

			t=lt.next();
			if (t.getTokenType()!=TType.DELIMETER){
				throw new SyntaxException();
			}

			ExpressionNode e2;
			t=lt.next();
			if(t.getTokenType()==TType.NUMBER){
				e2=parseNUMBER(t);
			}else if (t.getTokenType()==TType.UNKNOW){
				e2=parseUNKNOW(t);
			}else{
				throw new SyntaxException();
			}

			t=lt.next();
			if(t.getTokenType()==TType.CLOSE_BRACKET){
				return new FunctionExpressionNode(prToken.getTokenID(),e1,e2);
			}else{
				throw new SyntaxException();
			}

		default:
			throw new RuntimeException("Unknow function");
		}


	}

	private ExpressionNode parseUNKNOW(Token t) throws ParserException{

		Double val = constants.get(t.sValue);
		if (val != null) {
			return new ConstantExpressionNode(val);
		}else{
			throw new ParserException("Unknow Constant");
		}
	}

	private ExpressionNode parseNUMBER(Token t) throws ParserException{
		try {
			return new ConstantExpressionNode(Double.valueOf(t.getsValue()));
		}catch(NumberFormatException e){
			throw new ParserException("Number Format Exception");
		}
	}

	private List<Token> parseBRACKETS(LexicalTokenizer lt) throws ParserException{

		List<Token> tokenList = new LinkedList<Token>();
		int openBrCount = 1;
		Token t;
		do{
			t=lt.next();
			if (t.getTokenType()==TType.OPEN_BRACKET){
				openBrCount++;
			}else if(t.getTokenType()==TType.CLOSE_BRACKET){
				openBrCount--;
			}
			if (openBrCount>0) tokenList.add(t);		
		}while((t.getTokenType()!=TType.EOL)&&(openBrCount>0));
		
		tokenList.add(new Token(TType.EOL,0,new String()));

		if (openBrCount>0){
			throw new ParserException("Unclosed Bracket");
		}else{
			return tokenList;
		}

	}

}






	

    
    	
	

