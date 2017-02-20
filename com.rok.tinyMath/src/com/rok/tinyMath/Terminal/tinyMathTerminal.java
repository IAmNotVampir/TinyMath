package com.rok.tinyMath.Terminal;

import java.util.Scanner;

import com.rok.tinyMath.Parser.Program;

public class tinyMathTerminal {

	public static void main(String[] args) throws InterruptedException {
		
		Scanner scanner = new Scanner(System.in);
		Program program = new Program();
		String  text;
		System.out.println("TinyMath Start");
		while(true){
			Thread.sleep(500);
			if (scanner.hasNext()){	
				text=scanner.nextLine();	
				if(text=="exit"){
					break;
				}
				// обработка команд
				program.Execute(text);	
			}
		}
		System.out.println("TinyMath Start");
	}

}
