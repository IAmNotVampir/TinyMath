package com.rok.tinyMath.Parser;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.rok.tinyMath.Exceptions.ParserException;
import com.rok.tinyMath.Expressions.ExpressionNode;
import com.rok.tinyMath.Expressions.FunctionExpressionNode;
import com.rok.tinyMath.Parser.Token.TType;;


/**
 * 
 * Лексический анализатор
 *
 */
public class LexicalTokenizer {
	
	protected char buffer[];
	protected int currentPosition=0;
	protected Token EOLToken = new Token(TType.EOL,0,new String());
	protected Token currentToken;
	protected List<Token> tokenList = new LinkedList<>();
	protected Iterator<Token> it;
	
	public LexicalTokenizer(String s) throws ParserException{
		
		buffer=s.toCharArray();
		Token t;
		
		do
		{
		  t=getNextToken();
		  tokenList.add(t);
		}while(t.getTokenType()!=TType.EOL);
		
		it=tokenList.iterator();
		currentToken=it.next();
		
	}
	
	public LexicalTokenizer(List<Token> lt){
		
		tokenList=lt;
		it=tokenList.iterator();
		currentToken=it.next();
		
	}
	
	private Token getNextToken() throws ParserException{
		
		StringBuffer strbuff;

		//если достигнут конец выражения
		if (currentPosition>=buffer.length) {
			return EOLToken;
		};
		
		//пропускаем пробелы
		while ((buffer[currentPosition]==' ') || (buffer[currentPosition]=='\t'))
		{
			currentPosition++;
			if (currentPosition>=buffer.length) {
				return EOLToken;
			};
		}

		
		//на входе получено число
		if ((Character.isDigit(buffer[currentPosition]))||(buffer[currentPosition]=='.')){
			strbuff = new StringBuffer();
			while((Character.isDigit(buffer[currentPosition]))||(buffer[currentPosition]=='.'))
				{
				strbuff.append(buffer[currentPosition++]);
				if (currentPosition>=buffer.length) break;
				//для поддержки научной записи числа
				if (buffer[currentPosition]=='E'){
					strbuff.append(buffer[currentPosition++]);
					if (currentPosition>=buffer.length) break;
					if ((buffer[currentPosition]=='+')||(buffer[currentPosition]=='-')){
						strbuff.append(buffer[currentPosition++]);
						if (currentPosition>=buffer.length) break;
					}
				}
				}
			return new Token(TType.NUMBER,0,strbuff.toString());
		}

		// на входе получен оператор
		for (int i =0; i < ExpressionNode.operators.length;i++){
			if (buffer[currentPosition]==ExpressionNode.operators[i]){
				return new Token(TType.OPERATOR,i,String.valueOf(buffer[currentPosition++])) ;
			}
		}
		
		//на входе получена функция
		if (Character.isLetter(buffer[currentPosition])){
			strbuff = new StringBuffer();
			while (Character.isLetter(buffer[currentPosition])){
				strbuff.append(buffer[currentPosition++]);
				if (currentPosition>=buffer.length) break;
			}
			String fun = strbuff.toString().intern();
			//объевление новой функции
			if(fun=="func".intern()){
				return new Token(TType.FUNCTION_DEC,0,fun);
			}
			for(int i=0; i<FunctionExpressionNode.functions.length; i++){
				if (FunctionExpressionNode.functions[i]==fun) {
					return new Token(TType.FUNCTION,i,fun);
				}
			}
			return new Token(TType.UNKNOW,0,fun);
		}
		
		//на вход получены '(' ')'
		if (buffer[currentPosition]==')')
		{
			Token t = new Token(TType.CLOSE_BRACKET,0,String.valueOf(buffer[currentPosition++]));
			return t;
		}
		
		if (buffer[currentPosition]=='(')
		{
			Token t = new Token(TType.OPEN_BRACKET,0,String.valueOf(buffer[currentPosition++]));
			return t;
		}
		
		if (buffer[currentPosition]==',')
		{
			Token t = new Token(TType.DELIMETER,0,String.valueOf(buffer[currentPosition++]));
			return t;
		}
		
		if (buffer[currentPosition]=='=')
		{
			Token t = new Token(TType.EQUAL,0,String.valueOf(buffer[currentPosition++]));
			return t;
		}
		
		//символ не распознан
		throw new ParserException("Unknow symbol");
		
	}
	
	public boolean match(TType type,int id) throws ParserException{
		
		if ((currentToken.getTokenType()==type)&&(currentToken.getTokenID()==id)){
			if (it.hasNext()) {
				currentToken=it.next();
			}else{
				currentToken=EOLToken;
			}
			return true;
		}else{
			return false;
		}
		
	}
	/**
	 * Сравниваем текущий токен, возвращаем true в случаи совпадения
	 * и инкриментим указатель на текущий токен
	 */
	public boolean match(TType type) throws ParserException{
		
		if (currentToken.getTokenType()==type){
			if (it.hasNext()) {
				currentToken=it.next();
			}else{
				currentToken=EOLToken;
			}
			return true;
		}else{
			return false;
		}
		
	}
	
	public Token next() throws ParserException{
		
		Token save = currentToken;
		if (it.hasNext()) {
			currentToken=it.next();
		}else{
			currentToken=EOLToken;
		}
		return save;
		
	}
	
	public Token getCurToken(){
		return currentToken;
	}

}
