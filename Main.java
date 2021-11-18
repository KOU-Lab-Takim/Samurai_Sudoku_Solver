import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    ArrayList<ArrayList<Integer>> squares = new ArrayList<>();
    Map<String, ArrayList<Integer>> availables = new HashMap<>();


    public int checkChar(char i){
        if (i == '*'){
            return 0;
        }
        return i - '0';
    }

    public String createKey(int i, int j){
        return i + " " + j;
    }

    public void initAvailables(){
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                ArrayList<Integer> temp = new ArrayList<>();
                for (int k = 1; k < 10; k++) {
                    temp.add(k);
                }
                availables.put(createKey(i, j), temp);
            }
        }
    }

    public void readFile() {
        try {
            File myObj = new File("samurai.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                
                ArrayList<Integer> temp = new ArrayList<>();

                if(data.length() < 21){
                    if(data.length() < 18){
                        for (int i = 0; i < 6; i++) {
                            temp.add(-1);
                        }
                        for (int i = 0; i < data.length(); i++) {
                            temp.add(checkChar(data.charAt(i)));
                        }
                        for (int k = 0; k < 6; k++) {
                            temp.add(-1);
                        }
                    }
                    else{
                        for (int i = 0; i < 9; i++) {
                            temp.add(checkChar(data.charAt(i)));
                        }
                        for (int i = 0; i < 3; i++) {
                            temp.add(-1);
                        }
                        for (int i = 9; i < data.length(); i++) {
                            temp.add(checkChar(data.charAt(i)));
                        }
                    }
                }
                else{
                    for (int i = 0; i < data.length(); i++) {
                        temp.add(checkChar(data.charAt(i)));
                    }
                }
                squares.add(temp);

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void printSquares(){
        for (int i = 0; i < squares.size(); i++) {
            System.out.println(squares.get(i));
        }
    }

    public void printAvailables(){
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                System.out.println(i + " " + j + " => "+ availables.get(createKey(i, j)));
            }
        }
    }

    public String[] checkException(int x, int y){
        if( (x >= 6 && x <= 8) && (y >= 6 && y <= 8) ){
            // sol üst
            return new String[]{"sol", "ust"}; 
        }
        else if( (x >= 6 && x <= 8) && (y >= 12 && y <= 14) ){
            // sol alt
            return new String[]{"sol", "alt"}; 
        }
        else if( (x >= 12 && x <= 14) && (y >= 6 && y <= 8) ){
            // sağ üst
            return new String[]{"sag", "ust"}; 
        }
        else if( (x >= 12 && x <= 14) && (y >= 12 && y <= 14) ){
            // sağ alt
            return new String[]{"sag", "alt"}; 
        }
        return new String[]{"none"}; 
    }

    public void deleteFrom(int val, int x, int y){
        //System.out.println(val + " " + availables.get(createKey(x, y)));
        for (int i = 0; i < availables.get(createKey(x, y)).size(); i++) {
            if (availables.get(createKey(x, y)).get(i) == val){
                availables.get(createKey(x, y)).remove(i);
                    
            }
        }
        //System.out.println("Çozuldu" + " " + availables.get(createKey(x, y)));
    }

    public void check3by3(int x, int y){
        int i = x - (x % 3);
        int j = y - (y % 3);
        for (int j2 = i; j2 < i+3; j2++) {
            for (int k = j; k < j+3; k++) {
                deleteFrom(squares.get(x).get(y), j2, k);
            }
        }
    }

    public void checkSquare(int x, int y, int x1, int x2, int y1, int y2, int val){
        if(val == -1){
            val = squares.get(x).get(y);
        }
        String[] control = checkException(x, y);
        if(!(control[0].equals("none"))){
            if(control[0] == "sol"){
                for (int i = 0; i <= 5; i++) {
                    deleteFrom(val, i, y);
                }
            }
            if(control[0] == "sag"){
                for (int i = 15; i <= 20; i++) {
                    deleteFrom(val, x, i);
                }
            }
            if(control[1] == "ust"){
                for (int i = 0; i <= 5; i++) {
                    deleteFrom(val, x, i);
                }
            }
            if(control[1] == "alt"){
                for (int i = 15; i <= 20; i++) {
                    deleteFrom(val, x, i);
                }
            }
        }
        for (int x_ = x1; x_ <= x2; x_++) {
            if(x_ == x)
                continue;
            deleteFrom(val,x_, y);
        }
        for (int y_ = y1; y_ <= y2; y_++) {
            if(y_ == y)
                continue;
            deleteFrom(val,x, y_);
        }
        check3by3(x, y);

    }

    public void checkSquares(int x1,int  x2, int y1, int y2){
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                if(squares.get(x).get(y) != 0 && squares.get(x).get(y) != -1){
                    checkSquare(x, y, x1, x2, y1, y2, -1);
                }
            }
        }
    }

    public void findOnes(int x1, int x2, int y1, int y2){
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                if(squares.get(i).get(j) == 0 && availables.get(createKey(i, j)).size() == 1){
                    System.out.println(i + " " + j + " girdi");
                    //System.out.println("Önce" + availables.get(createKey(i, j)));
                    checkSquare(i, j, x1, x2, y1, y2, availables.get(createKey(i, j)).get(0));
                    deleteFrom(availables.get(createKey(i, j)).get(0), i, j);
                    //System.out.println("Sonra" + availables.get(createKey(i, j)));
                    
                }
            }
        }
    }

    public void findaa(){
        for (int i = 0; i <= 20; i++) {
            for (int j = 0; j <= 20; j++) {
                if(squares.get(i).get(j) == 0 && availables.get(createKey(i, j)).size() == 1){
                    System.out.println(i  + " " + j);
                }
            }
        }
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.readFile();
        //m.printSquares();
        m.initAvailables();
        m.checkSquares(0, 8, 0, 8);
        m.checkSquares(12, 20, 0, 9);
        m.checkSquares(0, 8, 12, 20);
        m.checkSquares(6, 14, 6, 14);
        m.checkSquares(12, 20, 12, 20);
        m.findOnes(0, 8, 12, 20);
        //m.findaa();
        m.printAvailables();
        
    }
}