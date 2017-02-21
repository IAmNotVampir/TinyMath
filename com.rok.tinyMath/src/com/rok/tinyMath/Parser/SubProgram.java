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
 * ����� ��� �������� ���������� ������������� ����� �������.
 * ���� ����������� � ���, ����� ����������� ��������� ������� ��� ����������
 * ���������� � ������������ ���������� �������� ������ ����������. 
 *
 */

public class SubProgram extends Program {
	
	Program topProgram;
	// ��� �������
	String name;
	//������ ����������
	Map<String,UserExpressionNode> arg = new LinkedHashMap<>();
	LexicalTokenizer lt;
	
	public SubProgram(Program p,LexicalTokenizer lt) throws ParserException{
		
		topProgram=p;
		this.lt = lt;
	}
	
	@Override
	/**
	 * ������� ��������� ������� � ������� ������,
	 * ����� ������� ��������� ������� ������������ ��� ���������� ����������
	 */
	protected ExpressionNode getConstant(Token t){
		//������� ���� ���������� � ������ ����������
		ExpressionNode exp = arg.get(t.sValue);
		if (exp==null) {
			//��������� ������ ���������� ����������
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
	 * ������� ������� ������������ �������������
	 * @return ��������� ������ � ����� � ����������� �������
	 */
	public UserFunction parseSubProgram() throws ParserException{
		
		Token t;
		ExpressionNode cup = new ConstantExpressionNode(0);
		
		if (lt.match(TType.FUNCTION_DEC)){
			t = lt.getCurToken();
			name=t.getsValue();
			if (lt.match(TType.UNKNOW)){
				//���� ������� ��� ���������
				if (topProgram.getFunction(name)!=null){
					throw new ParserException("Function definition repeated");
				}
				if (lt.match(TType.OPEN_BRACKET)){
					//������ ���������� �������
					do
					{
						t = lt.getCurToken();
						if(lt.match(TType.UNKNOW)){
							//���� ����� ���������� ������� ���������
							if(arg.containsKey(t.getsValue())){
								throw new ParserException("Function Arguments name alignment");
							}
							//�������� �� ��������� ����� ��������� � ���������� ����������
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
