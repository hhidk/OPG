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
    static List<String> grammarList = new ArrayList<String>();
    static Stack<Character> stack = new Stack<>();
    static Stack<Character> tStack = new Stack<>();

    static String filePath;
    static BufferedReader br;
    static String input;

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
            opt = getOpt(prev, cur);
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

    public static char getOpt(char prev, char cur){
        int iprev=0, jcur=0;
        switch(prev){
            case '+':
                iprev = 0;
                break;
            case '*':
                iprev = 1;
                break;
            case 'i':
                iprev = 2;
                break;
            case '(':
                iprev = 3;
                break;
            case ')':
                iprev = 4;
                break;
            case '#':
                iprev = 5;
                break;
        }
        switch(cur){
            case '+':
                jcur = 0;
                break;
            case '*':
                jcur = 1;
                break;
            case 'i':
                jcur = 2;
                break;
            case '(':
                jcur = 3;
                break;
            case ')':
                jcur = 4;
                break;
            case '#':
                jcur = 5;
                break;
        }
        switch(table[iprev][jcur]){
            case 0:
                return ' ';
            case 1:
                return '<';
            case 2:
                return '>';
            case 3:
                return '=';
            default:
                return ' ';
        }
    }

    public static void main(String[] args) throws IOException {
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
