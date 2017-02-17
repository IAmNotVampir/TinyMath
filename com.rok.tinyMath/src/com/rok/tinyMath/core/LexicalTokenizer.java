package com.rok.tinyMath.core;


import com.rok.tinyMath.Exceptions.ParserException;
import com.rok.tinyMath.Expressions.ExpressionNode;
import com.rok.tinyMath.Expressions.FunctionExpressionNode;

public class LexicalTokenizer {
	
	public final static int EOL = 0;
	public final static int NUMBER = 1;
	public final static int OPERATOR = 2;
	public final static int FUNCTION = 3;
	public final static int BRACKETS = 4;
	public final static int UNKNOW = 5;
	
	protected char buffer[];
	protected int currentPosition=0;
	protected int previousPosition=0;
	protected Token EOLToken = new Token(EOL,0,null);
	
	public LexicalTokenizer(String s){
		
		buffer=s.toCharArray();
		
	}
	
	public Token getNextToken() throws ParserException{
		
		StringBuffer str;
		
		previousPosition=currentPosition;

		//пропускаем пробелы
		do
		{
			//если достигнут конец выражения
			if (currentPosition>=buffer.length) {
				return EOLToken;
			};
		}while ((buffer[currentPosition]==' ') || (buffer[currentPosition++]=='\t'));
		
		//на входе получено число
		if ((Character.isDigit(buffer[currentPosition]))||(buffer[currentPosition]=='.')){
			str = new StringBuffer();
			while((Character.isDigit(buffer[currentPosition]))||(buffer[currentPosition]=='.'))
				{
				str.append(buffer[currentPosition++]);
				if (currentPosition>=buffer.length) break;
				}
			return new Token(NUMBER,0,str.toString());
		}

		// на входе получен оператор
		for (int i =0; i < ExpressionNode.operators.length;i++){
			if (buffer[currentPosition]==ExpressionNode.operators[i]){
				return new Token(OPERATOR,i,String.valueOf(buffer[currentPosition++])) ;
			}
		}
		
		//на входе получена функция
		if (Character.isLetter(buffer[currentPosition])){
			str = new StringBuffer();
			while (Character.isLetter(buffer[currentPosition])){
				str.append(buffer[currentPosition++]);
				if (currentPosition>=buffer.length) break;
			}
			String fun = str.toString();
			for(int i=0; i<FunctionExpressionNode.functions.length; i++){
				if (FunctionExpressionNode.functions[i].equals(fun)) {
					return new Token(FUNCTION,i,fun);
				}
				return new Token(UNKNOW,0,fun);
			}
		}
		
		//на вход получены '(' ')'
		if ((buffer[currentPosition]==')')||(buffer[currentPosition]=='('))
		{
			Token t = new Token(BRACKETS,0,String.valueOf(buffer[currentPosition++]));
			return t;
		}
		
		//символ не распознан
		throw new ParserException("Unknow symbol");
		
		
	}

}
