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
    public static LinkedList<Word> conditionCodelist = new LinkedList<Word>();
    public static LinkedList<Word> doBlockCodelist = new LinkedList<Word>();
    //for checking of braces, bracket,parenthesis
    public static Deque<Character> charstack = new ArrayDeque<Character>();
    public static boolean isOrder, isBalanced, isNoMissing, isSOPCorrect;

    public static void main(String[] args) throws Exception {
        //String to be tested
        String code;
        isOrder = false;
        isBalanced = true;
        isNoMissing = false;
        isSOPCorrect = false;
        //do { String w = "haha" ; System.out.println ( w ) ; } while ( ( x < 10 ) || ( isRound ) && ( x >= 20 ) ) ;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter code");
        code = scanner.nextLine();

        //check if do-while is correct
        checkOrder(code);
        //check if ( ) { } [ ] are balanced
        isBalanced(code);
        //check what is missing in code
        checkMissing(code);

        if (isBalanced && isNoMissing && isOrder && isSOPCorrect) {
            //split string into individual lexemes
            StringTokenizer st = new StringTokenizer(code, " ");
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

            boolean afterWhile = false;
            boolean afterDo = false;
            boolean beforeWhile = true;

            for (int i = 0; i < codelist.size(); i++) {
                //pang check kung tama yung naassign HAHA
                //System.out.println(codelist.get(i).word + " -> " + codelist.get(i).type);            
                if (codelist.get(i).word.equals("while")) {
                    afterWhile = true;
                    beforeWhile = false;
                }
                if (afterWhile && !codelist.get(i).word.equals("(") && !codelist.get(i).word.equals(")")) {
                    conditionCodelist.add(codelist.get(i));
                }
                if (codelist.get(i).word.equals("do")) {
                    afterDo = true;
                }
                if (afterDo && !codelist.get(i).word.equals("{") && !codelist.get(i).word.equals("}") && beforeWhile) {
                    doBlockCodelist.add(codelist.get(i));
                }

            }
            
            checkCondition(conditionCodelist);
            checkDoBlock(doBlockCodelist);
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
            if (x == ')') {
                if (charstack.isEmpty()) {
                    System.out.println("( missing");
                    isBalanced = false;
                } else if (charstack.peek() == '(') {
                    charstack.pop();
                }
            } else if (x == ']') {
                if (charstack.isEmpty()) {
                    System.out.println("[ missing");
                    isBalanced = false;
                } else if (charstack.peek() == '[') {
                    charstack.pop();
                }
            } else if (x == '}') {
                if (charstack.isEmpty()) {
                    System.out.println("{ missing");
                    isBalanced = false;
                } else if (charstack.peek() == '{') {
                    charstack.pop();
                }
            }
        }
        if (!charstack.isEmpty()) {
            System.out.println("missing closing block");
        } 

    }

    public static void checkOrder(String sentence) {
        if (sentence.matches("do \\{.*\\; \\} while \\(.*\\) \\;")) {
            System.out.println("do-while correct");
            isOrder = true;
        } else {
            System.out.println("do-while incorrect");
        }

        if (sentence.contains("System.out.println (") && sentence.contains(")")) {
            System.out.println("SOP correct");
            isSOPCorrect = true;
        } else {
            System.out.println("SOP incorrect");
        }

    }

    public static void checkMissing(String sentence) {
        if (!sentence.contains(";") || !sentence.contains(") ;") || !sentence.contains("; }")) {
            System.out.println("semicolon missing");
        } else if (!sentence.contains("{") && !sentence.contains("}")) {
            System.out.println("braces missing");
        } else if (!sentence.contains("(") && !sentence.contains(")")) {
            System.out.println("parenthesis missing");
        } else {
            isNoMissing = true;
        }

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
            case "&&":
                w.type = "and";
                break;
            case "||":
                w.type = "or";
                break;
        }
    }

    public static void regexChecker(Word w) {
        String token = w.word;

        String regexNumber = "\\d+";
        String regexVariables = "\\w*[a-zA-Z]\\w*";
        String regexString = "([\"'])(?:(?=(\\\\?))\\2.)*?\\1";
        String regexSOP = "System\\.out\\.println";
        String regexNotBoolVar = "!\\w*[a-zA-Z]\\w*";
        if (token.matches(regexNumber)) {
            w.type = "intValue";
        } else if (token.matches(regexVariables)) {
            w.type = "variable";
        } else if (token.matches(regexString)) {
            w.type = "stringValue";
        } else if (token.matches(regexSOP)) {
            w.type = "SOP";
        } else if (token.matches(regexNotBoolVar)) {
            w.type = "variable";
        }
    }

    public static void checkCondition(LinkedList<Word> list) {
        list.removeFirst();
        list.removeLast();
        boolean check1, check2, check3, conditionSyntaxError;
        check1 = false;
        check2 = false;
        check3 = false;
        int counter = 0;
        conditionSyntaxError = false;
        if (list.size() == 0) {
            System.out.println("Empty Conditions");
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).type.equals("variable")) {
                check1 = true;
                counter++;
            } else if (list.get(i).type.equals("expression")) {
                check2 = true;
                counter++;
            } else if (list.get(i).type.equals("intValue")) {
                check3 = true;
                counter++;
            }
            if (list.get(i).type.equals("and") || list.get(i).type.equals("or")) {
                if (check1 && !check2 && !check3 && counter == 1) {
                    //System.out.println("Boolean variable");
                    check1 = false;
                    check2 = false;
                    check3 = false;
                    counter = 0;
                } else if (check1 && check2 && check3 && counter == 3) {
                    //System.out.println("Comparative Statement");
                    check1 = false;
                    check2 = false;
                    check3 = false;
                    counter = 0;
                } else {
                    //System.out.println("Wrong conditions");
                    conditionSyntaxError = true;
                    check1 = false;
                    check2 = false;
                    check3 = false;
                    counter = 0;
                }
            }
        }
        if (check1 && !check2 && !check3) {
            //System.out.println("Boolean variable");
            check1 = false;
            check2 = false;
            check3 = false;
        } else if (check1 && check2 && check3) {
            //System.out.println("Comparative Statement");
            check1 = false;
            check2 = false;
            check3 = false;
        } else {
            //System.out.println("Wrong conditions");
            conditionSyntaxError = true;
            check1 = false;
            check2 = false;
            check3 = false;
        }
        if (conditionSyntaxError) {
            System.out.println("Condition Syntax Error");
        } else {
            System.out.println("Condition correct");
        }
    }

    public static void checkDoBlock(LinkedList<Word> list) {
        list.removeFirst();
        //list.removeLast();
        boolean isStringStatement = false, isIntStatement = false, isSOP = false;
        boolean check1 = false, check2 = false, check3 = false, check4 = false;
        boolean noSyntaxError = true;
        int counter = 0;
        for ( int i = 0 ; i < list.size(); i++) {
            Word temp = list.get(i);
            //System.out.println(temp.word + " -> " + temp.type + " " +counter);
            switch (temp.type) {
                case "stringDataType":
                    isStringStatement = true;
                    isIntStatement = false;
                    isSOP = false;
                break;
                case "intDataType":
                    isIntStatement = true;
                    isStringStatement = false;
                    isSOP = false;
                break;
                case "SOP":
                    isSOP = true;
                    isIntStatement = false;
                    isStringStatement = false;
                break;
                case "semiColon":
                    isStringStatement = false;
                    isIntStatement = false;
                    isSOP = false;
                    if(!check1 || !check2 || !check3 || !check4 || counter > 4) {
                        noSyntaxError = false;
                    }
                    counter = 0;    
                break;
            }
            if (isStringStatement) {
                if(temp.type.equals("stringDataType")){
                    check1 = true; 
                    counter++;
                }                    
                if(temp.type.equals("variable")) {
                    check2 = true;
                    counter++;
                }
                if(temp.type.equals("equals")) {
                    check3 = true;
                    counter++;
                }
                if(temp.type.equals("stringValue")) {
                    check4 = true;
                    counter++;
                }                
            } else if (isIntStatement) {
                if(temp.type.equals("intDataType")){
                    check1 = true; 
                    counter++;
                }                    
                if(temp.type.equals("variable")) {
                    check2 = true;
                    counter++;
                }
                if(temp.type.equals("equals")) {
                    check3 = true;
                    counter++;
                }
                if(temp.type.equals("intValue")) {
                    check4 = true;
                    counter++;
                }        
            } else if (isSOP) {
               if(temp.type.equals("SOP")){
                    check1 = true; 
                    counter++;
                }                    
                if(temp.type.equals("openParenthesis")) {
                    check2 = true;
                    counter++;
                }
                if(temp.type.equals("variable")) {
                    check3 = true;
                    counter++;
                }
                if(temp.type.equals("closeParenthesis")) {
                    check4 = true;
                    counter++;
                }               
            }
        }
         
        if(!noSyntaxError) {
            System.out.println("do Block Syntax Error");
        }
    }
}
