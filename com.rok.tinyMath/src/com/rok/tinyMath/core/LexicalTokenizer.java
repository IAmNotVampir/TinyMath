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
		while ((buffer[currentPosition]==' ') || (buffer[currentPosition]=='\t'))
		{
			//если достигнут конец выражения
			if (currentPosition>=buffer.length) {
				return EOLToken;
			};
			currentPosition++;
		}
		
		//на входе получено число
		if ((Character.isDigit(buffer[currentPosition]))||(buffer[currentPosition]=='.')){
			str = new StringBuffer();
			while((Character.isDigit(buffer[currentPosition]))||(buffer[currentPosition]=='.')
					&&(currentPosition<buffer.length))
				{
				str.append(buffer[currentPosition]);
				currentPosition++;
				}
			return new Token(NUMBER,0,str.toString());
		}

		// на входе получен оператор
		for (int i =0; i < ExpressionNode.operators.length;i++){
			if (buffer[currentPosition]==ExpressionNode.operators[i]){
				Token t = new Token(OPERATOR,i,String.valueOf(buffer[currentPosition])); 
				currentPosition++;
				return t ;
			}
		}
		
		//на входе получена функция
		boolean compare;
		int prFunID = 0;
		String prStr = new String();
		String curStr = new String();
		for (int i=0;i<FunctionExpressionNode.functions.length;i++){
			compare=true;
			curStr=FunctionExpressionNode.functions[i];
			for (int k =0 ; k<curStr.length();k++){
				if ((buffer[currentPosition+k]!=curStr.charAt(k))||
						((currentPosition+k)>=buffer.length)){
					compare=false;
					break;
				}
			}		
			/*
			одна функция может включать в себя наименование другой,
			тогда выбирается функция с более длинным именем.
			 */
			if (compare&&((prStr.isEmpty())||(prStr.length()<curStr.length()))){
				prStr=curStr;
				prFunID=i;
			}
		}
		
		if (!prStr.isEmpty()){
			currentPosition=currentPosition+prStr.length();
			return new Token(FUNCTION,prFunID,prStr);
		}
		
		//на вход получены '(' ')'
		if ((buffer[currentPosition]==')')||(buffer[currentPosition]=='('))
		{
			Token t = new Token(BRACKETS,0,String.valueOf(buffer[currentPosition]));
			currentPosition++;
			return t;
		}
		
		//символ не распознан
		throw new ParserException("Unknow symbol");
		
		
	}

}
