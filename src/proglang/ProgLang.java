/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proglang;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Scanner;
import java.util.StringTokenizer;
import jdk.nashorn.internal.codegen.CompilerConstants;

/**
 *
 * @author Raven
 */
public class ProgLang {

    //list of words
    public static LinkedList<Word> codelist = new LinkedList<Word>();
    //for checking of braces, bracket,parenthesis
    public static Deque<Character> charstack = new ArrayDeque<Character>();
    
    public static void main(String[] args) throws Exception {
        //String to be tested
        String code;
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter code");
        code = scanner.nextLine();
        
        //check if do-while is correct
        checkOrder(code);
        //check if ( ) { } [ ] are balanced
        isBalanced(code);
        //check what is missing in code
        checkMissing(code); 
        
        //split string into individual lexemes
        StringTokenizer st = new StringTokenizer(code," ");  
        while (st.hasMoreTokens()) {  
             codelist.add(new Word(st.nextToken()));

        }
//            for (String val : code.split(" ")) {
//                val.trim();
//                codelist.add(new Word(val));
//            }

        for (int i = 0; i < codelist.size(); i++) {
            //check and assign token to word
            checkToken(codelist.get(i));
            if (codelist.get(i).type == null) {
                regexChecker(codelist.get(i));
            }

        }
        for (int i = 0; i < codelist.size(); i++) {
            //pang check kung tama yung naassign HAHA
            System.out.println(codelist.get(i).word + " -> " + codelist.get(i).type);
            
        }                          
    }

    public static void isBalanced(String s) {
        for (int i = 0; i < s.length(); i++) {
            char x = s.charAt(i);
            if (x == '(' || x == '[' || x == '{') {
                // Push the element in the stack 
                charstack.push(x);
                //openBraceCounter++;
                //System.out.println("pushed");
            }
            if (x == ')'){
                if(charstack.isEmpty())
                    System.out.println("( missing");                
                else if(charstack.peek() == '(') 
                    charstack.pop();
            } else if (x == ']'){
                if(charstack.isEmpty())
                    System.out.println("[ missing");
                else if(charstack.peek() == '[') 
                charstack.pop();
            } else if (x == '}'){
                if(charstack.isEmpty())
                    System.out.println("{ missing");
                else if(charstack.peek() == '{')
                    charstack.pop();
            }
        }
        if(!charstack.isEmpty())
            System.out.println("missing closing block");
    }
    
    public static void checkOrder(String sentence){
        if(sentence.matches("do \\{.*\\; \\} while \\(.*\\) \\;"))
            System.out.println("do-while correct");            
        else
            System.out.println("do-while incorrect");
        if(sentence.contains("System.out.println (") && sentence.contains(")"))
            System.out.println("SOP correct");
        else
            System.out.println("SOP incorrect");
          
    }
    
    public static void checkMissing(String sentence){
        if(!sentence.contains(";") || !sentence.contains(") ;") || !sentence.contains("; }"))
            System.out.println("semicolon missing");         
        if(!sentence.contains("{") && !sentence.contains("}"))
            System.out.println("braces missing");
        if(!sentence.contains("(") && !sentence.contains(")"))
            System.out.println("parenthesis missing");
        
    }
    
    public static void checkToken(Word w) {
        String token = w.word;
        
        switch (token) {
            case "do":
                w.type = "do";
                break;
            case "{":
                w.type = "openBracket";
                break;
            case "}":
                w.type = "closeBracket";
                break;
            case "(":
                w.type = "openParenthesis";
                break;
            case ")":
                w.type = "closeParenthesis";
                break;
            case "while":
                w.type = "while";
                break;
            case ";":
                w.type = "semiColon";
            break;
            case ">":
                w.type = "expression";
            break;
            case "<":
                w.type = "expression";
            break;
            case "<=":
                w.type = "expression";
            break;
            case ">=":
                w.type = "expression";
            break;
            case "=":
                w.type = "equals";
            break;
            case "String":
                w.type = "stringDataType";
            break;
            case "int":
                w.type = "intDataType";
            break;   
        }
    }
    
    public static void regexChecker(Word w) {
        String token = w.word;
        
        String regexNumber = "\\d+";
        String regexVariables = "\\w*[a-zA-Z]\\w*";
        String regexString = "([\"'])(?:(?=(\\\\?))\\2.)*?\\1";
        String regexSOP = "System\\.out\\.println";
        
       if (token.matches(regexNumber)) {
           w.type = "intValue";
       } else if (token.matches(regexVariables)) {
           w.type = "variable";
       } else if (token.matches(regexString)) {
           w.type = "stringValue";
       } else if (token.matches(regexSOP)) {
           w.type = "SOP";
       }
    }
}