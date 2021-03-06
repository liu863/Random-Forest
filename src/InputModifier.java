import java.io.*;
import java.util.*;

public class InputModifier {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("No source file");
            System.exit(1);
        }
        InputModifier im = new InputModifier();
        //im.spaceTrimming(args[0]);
        im.categoryAssign(args[0]);
    }
    
    class Tuple {
        int open, high, low, close, volumn;
        Tuple(int open, int high, int low, int close, int volumn) {
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.volumn = volumn;
        }
    }
    
    private void addFields(String file) {
        String fileName = file.substring(0, file.length() - 4);
        File inFile = new File(file);
        File outFile = new File(fileName + "modified.txt");
        if (!outFile.exists()) {
            try{
                outFile.createNewFile();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        
        BufferedReader br;
        BufferedWriter bw;
        try {
            System.out.println("Start reading");
            br = new BufferedReader(new FileReader(inFile));
            bw = new BufferedWriter(new FileWriter(outFile.getAbsoluteFile()));
            List<Tuple> list = new ArrayList<>();
            String str;
            while ((str = br.readLine()) != null) {
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void categoryAssign(String file) {
        String fileName = file.substring(0, file.length() - 4);
        File inFile = new File(file);
        File outFile = new File(fileName + "_category.txt");
        if (!outFile.exists()) {
            try{
                outFile.createNewFile();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        
        BufferedReader br;
        BufferedWriter bw;
        try {
            System.out.println("Start reading");
            br = new BufferedReader(new FileReader(inFile));
            bw = new BufferedWriter(new FileWriter(outFile.getAbsoluteFile()));
            String str = br.readLine();
            int firstSpace = str.indexOf(' ');
            str = str.substring(firstSpace + 1);
            if (str.charAt(str.length() - 1) != ' ') 
                str += ' ';
            str += "Category";
            bw.write(str, 0, str.length());
            bw.newLine();
            int column = 0, increase = str.indexOf("Increase");
            for (int i = 0; i < increase; i++) 
                if (str.charAt(i) == ' ') column++;
            String buff = null;
            while ((str = br.readLine()) != null) {
                //System.out.println(str);
                firstSpace = str.indexOf(' ');
                str = str.substring(firstSpace + 1);
                if (buff != null) {
                    int front = 0, end = 0, count = 0;
                    for (; front < str.length() && count < column; front++) 
                        if (str.charAt(front) == ' ') 
                            count++;
                    for (end = front; end < str.length() && str.charAt(end) != ' '; end++);
                    String incr = str.substring(front, end);
                    //System.out.print(incr + " ");
                    Double category = Double.parseDouble(incr);
                    category = Math.floor((category + 10) / 2);
                    if (category > 9) category = 9.0;
                    if (category < 0) category = 0.0;
                    //System.out.println(category);
                    if (buff.charAt(buff.length() - 1) != ' ') 
                        buff += ' ';
                    buff += category.toString();
                    bw.write(buff, 0, buff.length());
                    bw.newLine();
                }
                buff = str;
            }
            br.close();
            bw.flush();
            bw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void spaceTrimming(String file) {
        String fileName = file.substring(0, file.length() - 4);
        File inFile = new File(file);
        File outFile = new File(fileName + "_space.txt");
        if (!outFile.exists()) {
            try{
                outFile.createNewFile();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        
        BufferedReader br;
        BufferedWriter bw;
        try {
            System.out.println("Start reading");
            br = new BufferedReader(new FileReader(inFile));
            bw = new BufferedWriter(new FileWriter(outFile.getAbsoluteFile()));
            int c, buff = -1;
            while ((c = br.read()) != -1) {
                if (c == ' ' || c == '\t') {
                    if (buff != ' ' && buff != '\t' && buff != '\n')
                        bw.write(' ');
                }
                else {
                    bw.write(c);
                }
                buff = c;
            }
            br.close();
            bw.flush();
            bw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}