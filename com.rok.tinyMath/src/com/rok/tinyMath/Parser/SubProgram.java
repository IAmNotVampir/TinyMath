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

/**
 * 
 *  ласс дл€ парсинга объ€влени€ пользователем новой функции.
 * »де€ заключаетс€ в том, чтобы представить аргументы функции как глобальные
 * переменные и использовать функционал базового класса неизменным. 
 *
 */

public class SubProgram extends Program {
	
	Program topProgram;
	// им€ функции
	String name;
	//список аргументов
	Map<String,UserExpressionNode> arg = new LinkedHashMap<>();
	LexicalTokenizer lt;
	
	public SubProgram(Program p,LexicalTokenizer lt) throws ParserException{
		
		topProgram=p;
		this.lt = lt;
	}
	
	@Override
	/**
	 * ‘ункци€ подмен€ет функцию в базовом классе,
	 * таким образом аргументы функции определ€ютс€ как глобальные переменные
	 */
	protected ExpressionNode getConstant(Token t){
		//сначала ищем переменную в списке аргументов
		ExpressionNode exp = arg.get(t.sValue);
		if (exp==null) {
			//провер€ем список глобальных переменных
			return topProgram.getConstant(t);
		}else{
			return exp;
		}
	}
	
	@Override
	public UserFunction getFunction(String str){
		
		return topProgram.getFunction(str);
		
	}

	/**
	 * парсинг функции определенной пользователем
	 * @return структура данных с телом и аргументами функции
	 */
	public UserFunction parseSubProgram() throws ParserException{
		
		Token t;
		ExpressionNode cup = new ConstantExpressionNode(0);
		
		if (lt.match(TType.FUNCTION_DEC)){
			t = lt.getCurToken();
			name=t.getsValue();
			if (lt.match(TType.UNKNOW)){
				//если функци€ уже объ€влена
				if (topProgram.getFunction(name)!=null){
					throw new ParserException("Function definition repeated");
				}
				if (lt.match(TType.OPEN_BRACKET)){
					//разбор аргументов функции
					do
					{
						t = lt.getCurToken();
						if(lt.match(TType.UNKNOW)){
							//если имена аргументов функции совпадают
							if(arg.containsKey(t.getsValue())){
								throw new ParserException("Function Arguments name alignment");
							}
							//проверка на сопадение имени аргумента с глобальной переменной
							if(topProgram.getConstant(t)!=null){
								throw new ParserException("Function Arguments name equal global variable");
							}
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
