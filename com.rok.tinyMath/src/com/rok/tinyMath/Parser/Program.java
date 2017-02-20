package com.rok.tinyMath.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	
	protected Map<String,ExpressionNode> constants = new HashMap<>();
	protected Map<String,UserFunction> functions = new HashMap<>();

	public double Execute(String str){

		try {
			//проверка на объявление новой функции
			SubProgram sb = new SubProgram(this,str);
			UserFunction fun = sb.parseSubProgram();
			if (fun!=null){
				functions.put(fun.getName(), fun);
				return 0;
			}
			
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

	protected ExpressionNode startParse(LexicalTokenizer lt) throws ParserException{
		
		ExpressionNode result = parseAddSub(lt);
		if (lt.match(TType.EOL)){
			return result;
		}else{
			throw new SyntaxException();
		}
	}

	protected ExpressionNode parseAddSub(LexicalTokenizer lt) throws ParserException{
		
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
	
	protected ExpressionNode parseMulDiv(LexicalTokenizer lt) throws ParserException{
		
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
	
	protected ExpressionNode parseUnary(LexicalTokenizer lt) throws ParserException{
		
		if (lt.match(TType.OPERATOR, ExpressionNode.OP_ADD)){			
			return parsePrimary(lt);
		};
		
		if (lt.match(TType.OPERATOR, ExpressionNode.OP_SUB)){			
			return new ExpressionNode(ExpressionNode.OP_SUB,new ConstantExpressionNode(0)
					,parsePrimary(lt));		
		};
		
		
		return parsePrimary(lt);
		
	}
	
	protected ExpressionNode parsePrimary(LexicalTokenizer lt) throws ParserException{
		
		Token t = lt.getCurToken();
		
		if (lt.match(TType.FUNCTION)){
			return parseFUNCTION(t,lt);
		}
		
		if (lt.match(TType.NUMBER)){
			return parseNUMBER(t);
		}
		
		if (lt.match(TType.UNKNOW)){
			return parseUNKNOW(t,lt);
		}
		
		if (lt.match(TType.OPEN_BRACKET)){
			 
			return parseBRACKETS(lt);
			
		}
		
		throw new SyntaxException();

		
	}



	protected ExpressionNode parseFUNCTION(Token prToken, LexicalTokenizer lt) throws ParserException{

		Token t;

		switch(prToken.tokenID){

		case FunctionExpressionNode.COS:
		case FunctionExpressionNode.SIN:
		case FunctionExpressionNode.SQRT:
		case FunctionExpressionNode.LOG:
		case FunctionExpressionNode.FLOOR:
		case FunctionExpressionNode.ABS:
		case FunctionExpressionNode.CEIL:

			if (!lt.match(TType.OPEN_BRACKET)){
				throw new SyntaxException();	
			}

			ExpressionNode e = parseAddSub(lt);

			if(!lt.match(TType.CLOSE_BRACKET)){
				throw new SyntaxException();
			}
			
			return new FunctionExpressionNode(prToken.getTokenID(),e);

		case FunctionExpressionNode.POW:
		case FunctionExpressionNode.MOD:

			if (!lt.match(TType.OPEN_BRACKET)){
				throw new SyntaxException();	
			}

			ExpressionNode e1 = parseAddSub(lt);

			if (!lt.match(TType.DELIMETER)){
				throw new SyntaxException();	
			}

			ExpressionNode e2 = parseAddSub(lt);

			if(!lt.match(TType.CLOSE_BRACKET)){
				throw new SyntaxException();
			}
			
			return new FunctionExpressionNode(prToken.getTokenID(),e1,e2);

		default:
			throw new RuntimeException("Unknow function");
		}


	}

	protected ExpressionNode parseUNKNOW(Token t, LexicalTokenizer lt) throws ParserException{

		ExpressionNode val = getConstant(t);
		if (val != null) {
			return val;
		}

		UserFunction ufun = getFunction(t.sValue);

		if (ufun==null) {
			if(!lt.match(TType.OPEN_BRACKET)){
				throw new ParserException("Unknow Function");	
			}else{
				throw new ParserException("Unknow Constant");
			}
		}
		
		if(!lt.match(TType.OPEN_BRACKET)){
			throw new SyntaxException();
		}
		
		//перебираем аргументы функции
		ExpressionNode e;
		List<ExpressionNode> input= new LinkedList<>();	
		e = parseAddSub(lt);
		input.add(e);
		while(lt.match(TType.DELIMETER))
		{
			e = parseAddSub(lt);
			input.add(e);
		}
		if(!lt.match(TType.CLOSE_BRACKET)){
			throw new SyntaxException();
		}
		
		UserFunctionExpressionNode ufexp = new UserFunctionExpressionNode(ufun,input);
		
		return ufexp;
		
	}

	protected ExpressionNode parseNUMBER(Token t) throws ParserException{
		try {
			return new ConstantExpressionNode(Double.valueOf(t.getsValue()));
		}catch(NumberFormatException e){
			throw new ParserException("Number Format Exception");
		}
	}

	protected ExpressionNode parseBRACKETS(LexicalTokenizer lt) throws ParserException{

		ExpressionNode result = parseAddSub(lt);
		if (lt.match(TType.CLOSE_BRACKET)){
			return result;
		}else{
			throw new ParserException("Unclosed Bracket");
		}

	}
	
	protected ExpressionNode getConstant(Token t){
		return constants.get(t.sValue);
	}
	
	public ExpressionNode getUserFun(Token t){
		return null;
	}

	public UserFunction getFunction(String str) {
		
		UserFunction exp = functions.get(str);
		
		if (exp==null) {
			return null;
		}
		
		//клонирование через сериализацию
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			
			oos = new ObjectOutputStream( baos );
			oos.writeObject(exp);
			oos.close();
			
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			
			return (UserFunction)ois.readObject();
			
		} catch (Exception e) {
			
			throw new RuntimeException(e);
		}			
	}
}






	

    
    	
	

