package com.rok.tinyMath.Terminal;

import java.util.Scanner;

import com.rok.tinyMath.Parser.Program;

public class tinyMathTerminal {

	public static void main(String[] args) throws InterruptedException {
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		Program program = new Program();
		String  text;
		System.out.println("TinyMath Start");
		while(true){
			Thread.sleep(500);
			if (scanner.hasNext()){	
				text=scanner.nextLine().intern();
				if (text.isEmpty()) continue;
				if(text=="exit".intern()){
					break;
				}
				// обработка команд
				program.Execute(text);	
			}
		}
		System.out.println("TinyMath exit");
	}

}
