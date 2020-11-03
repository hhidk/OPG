import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    static char[] tsymbol = {'+', '*', 'i', '(', ')', '#'};
    static char[] priority = {' ', '<', '>', '='};
    static int[][] table = {{2, 1, 1, 1, 2, 2},
                            {2, 2, 1, 1, 2, 2},
                            {2, 2, 0, 0, 2, 2},
                            {1, 1, 1, 1, 3, 2},
                            {2, 2, 0, 0, 2, 2},
                            {1, 1, 1, 1, 0, 0}};
    static Map<Object, Character> tsymbolTable = new HashMap<>();
    static List<String> grammarList = new ArrayList<String>();
    static Stack<Character> stack = new Stack<>();
    static Stack<Character> tStack = new Stack<>();

    static String filePath;
    static BufferedReader br;
    static String input;

    public static void initSymbolTable() {
        for(int i=0; i<6; i++){
            for(int j=0; j<6; j++){
                Pair<Character, Character> pair = new Pair<>(tsymbol[i], tsymbol[j]);
                tsymbolTable.put(pair, priority[table[i][j]]);
            }
        }
    }

    public static void initGrammar() {
        grammarList.add("N+N");
        grammarList.add("N*N");
        grammarList.add("(N)");
        grammarList.add("i");
    }

    public static void initStack() {
        stack.push('#');
        tStack.push('#');
    }

    public static void analyzeInput(String string) {
        int length = string.length();
        char cur, prev, opt;
        for(int i=0; i<length; i++){
            cur = string.charAt(i);
            prev = tStack.peek();
            opt = tsymbolTable.get(new Pair<>(prev, cur));
            if(i==length-1 && stack.peek()=='N' && stack.size() == 2)
                return;
            switch(opt){
                case ' ':
                    System.out.println("E");
                    return;
                case '=':
                case '<':
                    stack.push(cur);
                    tStack.push(cur);
                    System.out.println("I" + cur);
                    break;
                case '>':
                    StringBuilder stringBuilder = new StringBuilder();
                    while(true){
                        char popc = stack.pop();
                        if(isTerminate(popc))
                            tStack.pop();
                        if(popc == '#'){
                            System.out.println("RE");
                            return;
                        }
                        stringBuilder.insert(0, popc);
                        if(grammarList.contains(stringBuilder.toString())){
                            stack.push('N');
                            i--;
                            System.out.println("R");
                            break;
                        }
                    }
                    break;
            }
        }
    }

    public static boolean isTerminate(char c) {
        if(c!='+' && c!='*' && c!='i' && c!='#' && c!='(' && c!=')')
            return false;
        else
            return true;
    }

    public static void main(String[] args) throws IOException {
        initSymbolTable();
        initGrammar();
        initStack();
        filePath = args[0];
        br=new BufferedReader(new InputStreamReader
                (new FileInputStream(filePath),"UTF-8"));
        input = br.readLine();
        StringBuilder realInput = new StringBuilder(input);
        realInput.append('#');
        analyzeInput(realInput.toString().replace("\r",""));
    }
}
