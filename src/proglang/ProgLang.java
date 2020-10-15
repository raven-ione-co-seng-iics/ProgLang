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
        
        // check if ( ) { } [ ] are balanced
        if (isBalanced(code)) {
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
                
            }
            for (int i = 0; i < codelist.size(); i++) {
                //pang check kung tama yung naassign HAHA
                System.out.println(codelist.get(i).word + " = " + codelist.get(i).type);
            }
        } else {
           throw new Exception("Unbalanced Block characters");
        }
    }

    public static boolean isBalanced(String s) {
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
                    return false;                
                else if(charstack.peek() == '(') 
                    charstack.pop();
            } else if (x == ']'){
                if(charstack.isEmpty())
                    return false;
                else if(charstack.peek() == '[') 
                charstack.pop();
            } else if (x == '}'){
                if(charstack.isEmpty())
                    return false;
                else if(charstack.peek() == '{')
                    charstack.pop();
            }
        }
        return charstack.isEmpty();
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
            
            
        }
    }
}