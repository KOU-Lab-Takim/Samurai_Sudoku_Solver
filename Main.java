import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    ArrayList<ArrayList<Integer>> squares = new ArrayList<>();
    Map<String, ArrayList<Integer>> availables = new HashMap<>();
    private Boolean isDone = false;

    public int checkChar(char i) {
        if (i == '*') {
            return 0;
        }
        return i - '0';
    }

    public String createKey(int i, int j) {
        return i + " " + j;
    }

    public void initAvailables() {
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

                if (data.length() < 21) {
                    if (data.length() < 18) {
                        for (int i = 0; i < 6; i++) {
                            temp.add(-1);
                        }
                        for (int i = 0; i < data.length(); i++) {
                            temp.add(checkChar(data.charAt(i)));
                        }
                        for (int k = 0; k < 6; k++) {
                            temp.add(-1);
                        }
                    } else {
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
                } else {
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

    public void printSquares() {
        for (int i = 0; i < squares.size(); i++) {
            System.out.println(squares.get(i));
        }
    }

    public void printAvailables() {
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                System.out.println(i + " " + j + " => " + availables.get(createKey(i, j)));
            }
        }
    }

    public String[] checkException(int x, int y) {
        if ((x >= 6 && x <= 8) && (y >= 6 && y <= 8)) {
            // sol üst
            return new String[] { "sol", "ust" };
        } else if ((x >= 6 && x <= 8) && (y >= 12 && y <= 14)) {
            // sol alt
            return new String[] { "sol", "alt" };
        } else if ((x >= 12 && x <= 14) && (y >= 6 && y <= 8)) {
            // sağ üst
            return new String[] { "sag", "ust" };
        } else if ((x >= 12 && x <= 14) && (y >= 12 && y <= 14)) {
            // sağ alt
            return new String[] { "sag", "alt" };
        }
        return new String[] { "none" };
    }

    public void deleteFrom(int val, int x, int y) {
        // System.out.println(val + " " + availables.get(createKey(x, y)));
        for (int i = 0; i < availables.get(createKey(x, y)).size(); i++) {
            if (availables.get(createKey(x, y)).get(i) == val) {
                availables.get(createKey(x, y)).remove(i);

            }
        }
        // System.out.println("Çozuldu" + " " + availables.get(createKey(x, y)));
    }

    public void check3by3(int x, int y, int val) {
        int i = x - (x % 3);
        int j = y - (y % 3);
        for (int j2 = i; j2 < i + 3; j2++) {
            for (int k = j; k < j + 3; k++) {
                deleteFrom(val, j2, k);
            }
        }
    }

    public void checkSquare(int x, int y, int x1, int x2, int y1, int y2, int val) {
        
        if (val == -1) {
            val = squares.get(x).get(y);
        }
        String[] control = checkException(x, y);
        if (!(control[0].equals("none"))) {
            if (control[0] == "sol") {
                for (int i = 0; i <= 5; i++) {
                    deleteFrom(val, i, y);
                }
            }
            if (control[0] == "sag") {
                for (int i = 15; i <= 20; i++) {
                    deleteFrom(val, i, y);
                }
            }
            if (control[1] == "ust") {
                for (int i = 0; i <= 5; i++) {
                    deleteFrom(val, x, i);
                }
            }
            if (control[1] == "alt") {
                for (int i = 15; i <= 20; i++) {
                    deleteFrom(val, x, i);
                }
            }
        }
        for (int x_ = x1; x_ <= x2; x_++) {
            deleteFrom(val, x_, y);
        }
        for (int y_ = y1; y_ <= y2; y_++) {
            deleteFrom(val, x, y_);
        }
        check3by3(x, y, val);
        squares.get(x).set(y, val);
        /* if(flag){
            System.out.println(x + " " + y + " index " + val + " silindi");
            System.out.println(availables.get(createKey(1, 18)));
            printSquares();
            
        } */
    }

    public void checkSquares(int x1, int x2, int y1, int y2) {
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                if (squares.get(x).get(y) != 0 && squares.get(x).get(y) != -1) {
                    checkSquare(x, y, x1, x2, y1, y2, squares.get(x).get(y));
                }
            }
        }
    }

    public void findOnes(int x1, int x2, int y1, int y2) {
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                if (squares.get(i).get(j) == 0 && availables.get(createKey(i, j)).size() == 1) {
                    isDone = true;
                    //squares.get(i).set(j, availables.get(createKey(i, j)).get(0));
                    checkSquare(i, j, x1, x2, y1, y2, availables.get(createKey(i, j)).get(0));
                    //deleteFrom(availables.get(createKey(i, j)).get(0), i, j);
                    return;
                }
            }
        }
    }

    public void searchSingle(int x, int y, int x1, int x2, int y1, int y2) {
        ArrayList<Integer> numRepeat = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numRepeat.add(0);
        }
        for (int i = x; i < x + 3; i++) {
            for (int j = y; j < y + 3; j++) {
                if (squares.get(i).get(j) == 0) {
                    ArrayList<Integer> av = availables.get(createKey(i, j));
                    for (int j2 = 0; j2 < av.size(); j2++) {
                        int num = av.get(j2);
                        numRepeat.set(num, numRepeat.get(num) + 1);
                    }

                }

            }
        }

        for (int i = 1; i < 10; i++) {
            if (numRepeat.get(i) == 1) {
                isDone = true;
                // find square index
                int index_i = 0, index_j = 0;
                for (int i2 = x; i2 < x + 3; i2++) {
                    for (int j2 = y; j2 < y + 3; j2++) {
                        if (squares.get(i2).get(j2) == 0) {
                            ArrayList<Integer> av = availables.get(createKey(i2, j2));
                            for (int j3 = 0; j3 < av.size(); j3++) {
                                if (av.get(j3) == i) {
                                    index_i = i2;
                                    index_j = j2;
                                }
                            }
                        }

                    }
                }

                //squares.get(x).set(y, i);
                checkSquare(index_i, index_j, x1, x2, y1, y2, i);
                //deleteFrom(i, index_i, index_j);
                
                return;
            }
        }
    }

    public void findSingle(int x1, int x2, int y1, int y2) {
        for (int i = x1; i <= x2; i += 3) {
            for (int j = y1; j <= y2; j += 3) {
                searchSingle(i, j, x1, x2, y1, y2);
            }
        }
    }

    public Boolean isSudokuSolved(){
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                if(squares.get(i).get(j) == 0){
                    return false;
                }
            }
        }



        return true;
    }

    public Boolean isWrongDecision(){
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                ArrayList<Integer> av = availables.get(createKey(i, j));
                if(squares.get(i).get(j) == 0 && av.size() == 0){
                    /* System.out.println("wrong decision");
                    System.out.println(i + " " + j + av); */
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<Integer> findInterval(int i, int j){
        ArrayList<Integer> a = new ArrayList<>();
        if(i >= 6 && i <= 14 && j >= 6 && j <= 14){
            a.add(6);a.add(14);a.add(6);a.add(14);
            return a;
        }
        if(i >= 0 && i <= 8 && j >= 0 && j <= 8){
            a.add(0);a.add(8);a.add(0);a.add(8);
            return a;
        }
        if(i >= 0 && i <= 8 && j >= 12 && j <= 20){
            a.add(0);a.add(8);a.add(12);a.add(20);
            return a;
        }
        if(i >= 12 && i <= 20 && j >= 0 && j <= 8){
            a.add(12);a.add(20);a.add(0);a.add(8);
            return a;
        }
        a.add(12);a.add(20);a.add(12);a.add(20);
        return a;
    }

    public void copySquares(ArrayList<ArrayList<Integer>> square1, ArrayList<ArrayList<Integer>> square2){
        /* System.out.println(square2.size()); */
        for (int i = 0; i < square2.size(); i++) {
            ArrayList<Integer> temp = new ArrayList<>();
            for (int j = 0; j < square2.size(); j++) {
                temp.add(square2.get(i).get(j));
            }
            if(square1.size() == 21){
                square1.set(i, new ArrayList<>(temp));
            }
            else
                square1.add(temp);
        }
    }

    public Boolean solveThread(){
        while(true){

            if(isSudokuSolved())
                return true;
            if(isWrongDecision())
                return false;

            isDone = false;
            findOnes(0, 8, 0, 8);
            findOnes(12, 20, 0, 8);
            /* if(isWrongDecision())
                System.out.println("hatali"); */
            findOnes(0, 8, 12, 20);
            findOnes(6, 14, 6, 14);
            findOnes(12, 20, 12, 20);
         
            findSingle(0, 8, 0, 8);
            findSingle(12, 20, 0, 8);
            findSingle(0, 8, 12, 20);
            findSingle(6, 14, 6, 14);
            findSingle(12, 20, 12, 20);
          
            /* System.out.println("isDone  "  + isDone); */
            if(isDone == false){
                for (int i = 0; i < 21; i++) {
                    for (int j = 0; j < 21; j++) {
                        if(squares.get(i).get(j) == 0){
                            /* System.out.println("girdi"); */
                            ArrayList<Integer> av = availables.get(createKey(i, j));
                            for (int k = 0; k < av.size(); k++) {
                                Main m2 = new Main();
                                copySquares(m2.squares, squares);
                                for (Map.Entry<String,ArrayList<Integer>> entry : availables.entrySet())
                                    m2.availables.put(entry.getKey(), new ArrayList<Integer>(entry.getValue()));
                                ArrayList<Integer> a = findInterval(i, j);
                                m2.checkSquare(i, j, a.get(0), a.get(1), a.get(2), a.get(3), av.get(k));
                                if(m2.solveThread()){
                                    copySquares(squares, m2.squares);
                                    for (Map.Entry<String,ArrayList<Integer>> entry : m2.availables.entrySet())
                                        availables.put(entry.getKey(), new ArrayList<Integer>(entry.getValue())); 
                                    /* System.out.println("cozdum"); */
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        
        }
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.readFile();
        m.initAvailables();
        
        m.checkSquares(0, 8, 0, 8);
        m.checkSquares(12, 20, 0, 8);
        m.checkSquares(0, 8, 12, 20);
        m.checkSquares(6, 14, 6, 14);
        m.checkSquares(12, 20, 12, 20);
        
        m.solveThread();

        m.printSquares();
        
    }
}