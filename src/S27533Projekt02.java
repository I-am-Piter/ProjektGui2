import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class S27533Projekt02{
    public static void main(String[] args) {
        boolean[] poddanie = {false, false};
        boolean nextRound = false;
        Color gracz = Color.WHITE;
        Gameboard gameboard = new Gameboard(8);
        gameboard.setGame();
        Scanner scanner = new Scanner(System.in);
        boolean work = true;
        int x1, y1, x2, y2;
        int[] move = new int[0];
        Piece[][] copy = new Piece[gameboard.pieces_m.length][gameboard.pieces_m[0].length];
        String path = "zapis.bin";
        SaveManager saveManager = new SaveManager(path);
        final int[] surrender = {9, 9, 9, 9};
        final int[] gameSave = {9, 9, 9, 10};
        final int[] readSave = {9, 9, 9, 11};
        final int[] changePlayer = {10, 10, 10, 10};
        Piece[][] copy2;
        while (work) {
            gameboard.showBoard();
            copy2 = Gameboard.makeCopy(gameboard.makeCopy(gameboard.pieces_m));
            if (gameboard.checkIfCheck(gameboard.pieces_m, gracz)) {
                if (gameboard.checkIfMate(copy2, gracz)) {
                    System.out.println("Gra została zakończona zwycięstwem strony " + (gracz == Color.BLACK ? "białej" : "czarnej"));
                    work = false;
                    continue;
                }
            }
            move = gameboard.getMove(gracz);
            if (Arrays.equals(changePlayer, move)) {
                gracz = (gracz == Color.BLACK ? Color.WHITE : Color.BLACK);
                continue;
            }
            if (Arrays.equals(surrender, move)) {
                poddanie[gracz.getNum()] = true;
                nextRound = true;
                if (poddanie[0] == true && poddanie[1] == true && nextRound) {
                    work = false;
                    System.out.println("Gra została poddana");
                }
                gracz = (gracz == Color.BLACK ? Color.WHITE : Color.BLACK);
                continue;
            }else{
                nextRound = false;
                poddanie[0] = false;
                poddanie[1] = false;
            }
            if(Arrays.equals(gameSave,move)){
                try {
                    saveManager.saveGame(gameboard.pieces_m);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Game saved");
                continue;
            }
            if(Arrays.equals(readSave,move)){
                try {
                    gameboard.openFromSave(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Starting game from file");
                gracz = Color.WHITE;
                continue;
            }
            x1 = move[0];
            y1 = move[1];
            x2 = move[2];
            y2 = move[3];
            gameboard.pieces_m[y1][x1].madeMove_m = true;
            if (gameboard.pieces_m[y2][x2] != null) {
                gameboard.zbicie(x2, y2);
            }
            gameboard.pieces_m[y2][x2] = gameboard.pieces_m[y1][x1];
            gameboard.pieces_m[y1][x1] = null;
            gameboard.pieces_m[y2][x2].x_m = x2;
            gameboard.pieces_m[y2][x2].y_m = y2;
            if (gameboard.bicieWPrzelocie(gameboard.pieces_m, x1,y1,y2,gracz)){
                continue;
            }
            if (gameboard.pieces_m[y2][x2].type_m == 0 && (y2 == 8 || y2 == 1)) {
                gameboard.promocjaPiona(x2, y2, gracz);
            }
            System.out.println("następny gracz");
            gracz = (gracz == Color.BLACK ? Color.WHITE : Color.BLACK);
            if (gameboard.checkIfCheck(gameboard.pieces_m, gracz)) {
                System.out.println("SZACH");
            }


        }

    }
}
class Gameboard{
    Piece[][] pieces_m;
    static ArrayList<Piece> zbitePionki;


    Gameboard(int x) {
        pieces_m = new Piece[x + 1][x + 1];
        zbitePionki = new ArrayList<>();
    }

    void setGame() { //CHECK
        //Black
        pieces_m[1][1] = new Rook(1, 1, Color.BLACK);
        pieces_m[1][8] = new Rook(8, 1, Color.BLACK);
        pieces_m[1][2] = new Knight(2, 1, Color.BLACK);
        pieces_m[1][7] = new Knight(7, 1, Color.BLACK);
        pieces_m[1][3] = new Bishop(3, 1, Color.BLACK);
        pieces_m[1][6] = new Bishop(6, 1, Color.BLACK);
        pieces_m[1][4] = new Queen(4, 1, Color.BLACK);
        pieces_m[1][5] = new King(5, 1, Color.BLACK);
        for (int i = 1; i < pieces_m[2].length; i++) {
            pieces_m[2][i] = new Pawn(i, 2, Color.BLACK);
        }

//        White
        pieces_m[8][1] = new Rook(1, 8, Color.WHITE);
        pieces_m[8][8] = new Rook(8, 8, Color.WHITE);
        pieces_m[8][2] = new Knight(2, 8, Color.WHITE);
        pieces_m[8][7] = new Knight(7, 8, Color.WHITE);
        pieces_m[8][3] = new Bishop(3, 8, Color.WHITE);
        pieces_m[8][6] = new Bishop(6, 8, Color.WHITE);
        pieces_m[8][5] = new Queen(5, 8, Color.WHITE);
        pieces_m[8][4] = new King(4, 8, Color.WHITE);
        for (int i = 1; i < pieces_m[7].length; i++) {
            pieces_m[7][i] = new Pawn(i, 7, Color.WHITE);
        }

    }

    void showBoard() {// CZĘŚĆ WIZUALNA
        System.out.print(" " + "\t");
        for (int i = 1; i < pieces_m.length; i++) {
            System.out.print(i + "\t");
        }
        System.out.println();
        int counterForRows = 1;
        for (int i = 1; i < pieces_m.length; i++) {
            System.out.print(counterForRows++ + "\t");
            for (int j = 1; j < pieces_m[i].length; j++) {
                if (pieces_m[i][j] == null) {
                    System.out.print("□" + "\t");
                } else {
                    Print printer = (Piece p1) -> {
                        int type = p1.type_m;
                        Color color = p1.team_m;
                        if (color == Color.WHITE) {
                            switch (type) {
                                case 0 -> {
                                    return "♟︎";
                                }
                                case 1 -> {
                                    return "♚";
                                }
                                case 2 -> {
                                    return "♛";
                                }
                                case 3 -> {
                                    return "♜";
                                }
                                case 4 -> {
                                    return "♝";
                                }
                                case 5 -> {
                                    return "♞";
                                }
                            }
                        } else {
                            switch (type) {
                                case 0 -> {
                                    return "♙︎";
                                }
                                case 1 -> {
                                    return "♔";
                                }
                                case 2 -> {
                                    return "♕";
                                }
                                case 3 -> {
                                    return "♖";
                                }
                                case 4 -> {
                                    return "♗";
                                }
                                case 5 -> {
                                    return "♘";
                                }
                            }
                        }
                        return null;
                    };
                    System.out.print(printer.signToPrint(pieces_m[i][j]) + "\t");
                }
            }
            System.out.println();
        }
    } //CHECK



    boolean stillOnBoard(int x, int y, int x1, int y1) {
        if (x < pieces_m.length && x >= 1 && y < pieces_m.length && y >= 1 && x1 < pieces_m.length && x1 >= 1 && y1 < pieces_m.length && y1 >= 1) {
            return true;
        }
        return false;
    }//CHECK

    boolean checkIfCheck(Piece[][] p1, Color color) {
        int x = 0;
        int y = 0;
        for (int i = 1; i < p1.length; i++) {
            for (int j = 1; j < p1[i].length; j++) {
                if (p1[i][j] != null) {
                    if (p1[i][j].team_m == color && p1[i][j].type_m == 1) {
                        x = j;
                        y = i;
                    }
                }
            }
        }
        for (Piece[] pp : p1) {
            for (Piece ppp : pp) {
                if (ppp != null) {
                    if (ppp.team_m != color) {
                        if (ppp.viableMove(x, y, p1)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }//CHECK
    public boolean isMoveLegal(Piece[][] p1, Color color, int x1, int y1, int x2,int y2){//CHECK
        Piece[][] p2 = new Piece[p1.length][p1[0].length];
        for (int i = 0; i < p1.length; i++) {
            for (int j = 0; j < p1[i].length; j++) {
                p2[i][j] = p1[i][j];
            }
        }
        p2[y2][x2] = p2[y1][x1];
        p2[y1][x1] = null;
        return !checkIfCheck(p2,color);

    }

    static Piece[][] makeCopy(Piece[][] p1) { //CHECK
        Piece[][] copy = new Piece[p1.length][p1[0].length];
        for (int i = 0; i < copy.length; i++) {
            for (int j = 0; j < copy[i].length; j++) {
                if(p1[i][j] != null) {
                    copy[i][j] = (Piece) p1[i][j].clone();
                }
            }
        }
        return copy;
    }
    void openFromSave(String path) throws IOException { //CHECK
        SaveManager.path_m = path;
        Piece[] piecesTmp = SaveManager.openSavedGame();
        fillPiece2dArray(piecesTmp);
        fillZbitePionki(piecesTmp);
        if(checkIfMate(pieces_m,Color.BLACK) || checkIfMate(pieces_m,Color.WHITE)){
            System.out.println("Wczytałeś zakończoną grę.");
        }
    }

    private void fillZbitePionki(Piece[] piecesTmp) { //CHECK
        zbitePionki = new ArrayList<>();
        for (Piece p:
             piecesTmp) {
            if(p.x_m == 0 && p.y_m == 0){
                zbitePionki.add(p);
            }
        }
    }

    private void fillPiece2dArray(Piece[] piecesTmp) { //CHECK
        int pieces_mLength  = pieces_m.length;
        pieces_m = new Piece[pieces_mLength][pieces_mLength];
        for (int i = 1; i < pieces_m.length; i++) {
            for (int j = 1; j < pieces_m[0].length; j++) {
                for (Piece p:
                     piecesTmp) {
                    if(p.x_m == j && p.y_m == i){
                        pieces_m[i][j] = p;
                    }
                }
            }
        }
    }

    boolean checkIfMate(Piece[][] p1, Color color) { //CHECK
        Piece[][] p1Copy = makeCopy(p1);
        if (checkIfCheck(p1, color)) {
            for (int i = 1; i < p1.length; i++) {
                for (int j = 1; j < p1[i].length; j++) {
                    for (int k = 1; k < p1.length; k++) {
                        for (int l = 1; l < p1[i].length; l++) {
                            if (p1[i][j] != null) {
                                if (p1[i][j].team_m == color && p1[i][j].viableMove(l, k, p1) && isMoveLegal(p1,color,j,i,l,k)) {
                                    p1[k][l] = p1[i][j];
                                    p1[i][j] = null;
                                    p1[k][l].x_m = l;
                                    p1[k][l].y_m = k;
                                    if (!checkIfCheck(p1, color)) {
                                        p1 = makeCopy(p1Copy);
                                        return false;
                                    }else {
                                        p1 = makeCopy(p1Copy);
                                    }
                                }
                            }
                            p1 = makeCopy(p1Copy);
                        }
                    }
                }
            }
            p1 = makeCopy(p1Copy);
            return true;
        } else {
            p1 = makeCopy(p1Copy);
            return false;
        }
    }


    boolean bicieWPrzelocie(Piece[][] p1,int x,int y1, int y2, Color team){ //CHECK
        Piece kandydat = null;
        if(checkIfAnyCanditates(p1)){
            if((y1 == 2||y1 == 7)&&(y2 == 4||y2 == 5)){
                for (Piece p:p1[y2]){
                    if(p != null) {
                        if (Math.abs(p.x_m - x) == 1 && p.team_m != team) {
                            kandydat = p;
                            System.out.println("Zbić w przelocie? (" + p.x_m + ") TAK/NIE");
                            Scanner scanner = new Scanner(System.in);
                            if (scanner.nextLine().equals("TAK")) {
                                pieces_m[y2][x].x_m = 0;
                                pieces_m[y2][x].y_m = 0;
                                pieces_m[y2][x] = null;
                                if(y1 == 7) {
                                    pieces_m[6][x] = kandydat;
                                    pieces_m[kandydat.y_m][kandydat.x_m] = null;
                                    kandydat.x_m = x;
                                    kandydat.y_m = 6;
                                } else {
                                    pieces_m[3][x] = kandydat;
                                    pieces_m[kandydat.y_m][kandydat.x_m] = null;
                                    kandydat.x_m = x;
                                    kandydat.y_m = 3;
                                }
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean checkIfAnyCanditates(Piece[][] p1) { //CHECK
        for (Piece p:p1[4]
             ) {
            if(p != null) {
                if (p.type_m == 0 && p.team_m == Color.WHITE) {
                    return true;
                }
            }
        }
        for (Piece p:p1[5]
        ) {
            if(p != null) {
                if (p.type_m == 0 && p.team_m == Color.BLACK) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean roszada(Color team, boolean ls) { //CHECK
        switch (team) {
            case BLACK -> {
                if (ls) {
                    for (int i = 2; i < 5; i++) {
                        if (pieces_m[1][i] != null || pieces_m[1][1].madeMove_m || pieces_m[1][5].madeMove_m) {
                            return false;
                        }
                    }
                } else {
                    for (int i = 6; i < 8; i++) {
                        if (pieces_m[1][i] != null || pieces_m[1][8].madeMove_m || pieces_m[1][5].madeMove_m) {
                            return false;
                        }
                    }
                }
                makeRoszada(team, ls);
                return true;
            }
            case WHITE -> {
                if (ls) {
                    for (int i = 5; i < 8; i++) {
                        if (pieces_m[8][i] != null || pieces_m[8][8].madeMove_m || pieces_m[8][4].madeMove_m) {
                            return false;
                        }
                    }
                } else {
                    for (int i = 2; i < 4; i++) {
                        if (pieces_m[8][i] != null || pieces_m[8][1].madeMove_m || pieces_m[8][4].madeMove_m) {
                            return false;
                        }
                    }
                }
                makeRoszada(team, ls);
                return true;
            }
        }
        return true;
    }

    void makeRoszada(Color team, boolean ls) { //CHECK
        switch (team) {
            case BLACK -> {
                if (ls) {
                    pieces_m[1][3] = pieces_m[1][5];
                    pieces_m[1][5] = null;
                    pieces_m[1][4] = pieces_m[1][1];
                    pieces_m[1][1] = null;
                    pieces_m[1][3].x_m = 3;
                    pieces_m[1][4].x_m = 4;
                } else {
                    pieces_m[1][7] = pieces_m[1][5];
                    pieces_m[1][5] = null;
                    pieces_m[1][6] = pieces_m[1][8];
                    pieces_m[1][8] = null;
                    pieces_m[1][6].x_m = 6;
                    pieces_m[1][7].x_m = 7;
                }
            }
            case WHITE -> {
                if (ls) {
                    pieces_m[8][6] = pieces_m[8][4];
                    pieces_m[8][4] = null;
                    pieces_m[8][5] = pieces_m[8][8];
                    pieces_m[8][8] = null;
                    pieces_m[8][5].x_m = 5;
                    pieces_m[8][6].x_m = 6;
                } else {
                    pieces_m[8][2] = pieces_m[8][4];
                    pieces_m[8][4] = null;
                    pieces_m[8][3] = pieces_m[8][1];
                    pieces_m[8][1] = null;
                    pieces_m[8][2].x_m = 2;
                    pieces_m[8][3].x_m = 3;
                }
            }
        }
    }

    void promocjaPiona(int x, int y, Color color) { //CHECK
        System.out.println("Wprowadź kategorię pionka jaki chcesz wybrać: 2 - hetman,3 - wieza,4 - goniec,5 - koń");
        Scanner scanner = new Scanner(System.in);
        int kategoria = scanner.nextInt();
        zbicie(x, y);
        pieces_m[y][x] = createPiece(kategoria, x, y, color);
    }
    Piece createPiece(int category, int x, int y, Color color) {
        switch (category) {
            case 2 -> {
                return new Queen(x, y, color);
            }
            case 3 -> {
                return new Rook(x, y, color);
            }
            case 4 -> {
                return new Bishop(x, y, color);
            }
            case 5 -> {
                return new Knight(x, y, color);
            }
        }
        return null;
    }

    void zbicie(int x, int y) { //CHECK
        pieces_m[y][x].x_m = 0;
        pieces_m[y][x].y_m = 0;
        zbitePionki.add(pieces_m[y][x]);
    }

    public int[] getMove(Color team_t) { //CHECK
        Scanner scanner = new Scanner(System.in);
        int[] cords = new int[4];
        String input;
        System.out.println("Wprowadź kordynaty figury którą chcesz się ruszyć, oraz jej kordynaty docelowe zgodnie z formatem \"x1y1x2y2\", jeśli chcesz się poddać wpisz SURRENDER, aby wykonać roszadę wpisz ROSZADAL, albo ROSZADAS (long, short), READ aby wczytać grę, SAVE aby zapisać");
        boolean error = false;
        do {
            error = false;
            input = scanner.nextLine();
            if (input.equals("SURRENDER")) {
                for (int i = 0; i < cords.length; i++) {
                    cords[i] = 9;
                }
                return cords;
            }
            if (input.equals("ROSZADAL")) {
                if (roszada(team_t, true)) {
                    return new int[]{10, 10, 10, 10};
                } else {
                    error = true;
                    System.out.println("Wprowadź kordynaty figury którą chcesz się ruszyć, oraz jej kordynaty docelowe zgodnie z formatem \"x1y1x2y2\", jeśli chcesz się poddać wpisz SURRENDER, aby wykonać roszadę wpisz ROSZADAL, albo ROSZADAS (long, short), READ aby wczytać grę, SAVE aby zapisać");
                    continue;
                }
            }
            if (input.equals("ROSZADAS")) {
                if (roszada(team_t, false)) {
                    return new int[]{10, 10, 10, 10};
                } else {
                    error = true;
                    System.out.println("Wprowadź kordynaty figury którą chcesz się ruszyć, oraz jej kordynaty docelowe zgodnie z formatem \"x1y1x2y2\", jeśli chcesz się poddać wpisz SURRENDER, aby wykonać roszadę wpisz ROSZADAL, albo ROSZADAS (long, short), READ aby wczytać grę, SAVE aby zapisać");
                    continue;
                }
            }
            if(input.equals("SAVE")){
                return new int[]{9,9,9,10};
            }
            if(input.equals("READ")){
                return new int[]{9,9,9,11};
            }


            if (input.length() == 4) {
                cords[0] = Integer.valueOf(input.charAt(0) - 48);
                cords[1] = Integer.valueOf(input.charAt(1) - 48);
                cords[2] = Integer.valueOf(input.charAt(2) - 48);
                cords[3] = Integer.valueOf(input.charAt(3) - 48);
            } else {
                error = true;
            }
            if(!isMoveLegal(pieces_m,team_t,cords[0],cords[1],cords[2],cords[3])){
                System.out.println("move not legal");
                error = true;
            }
            if ((pieces_m[cords[3]][cords[2]] != null)) {
                if (pieces_m[cords[3]][cords[2]].team_m == team_t) {
                    System.out.println("piece null or not ur team");
                    error = true;
                }
            }
            if ((!stillOnBoard(cords[0], cords[1], cords[2], cords[3])) || (pieces_m[cords[1]][cords[0]] == null) ||
                    (!pieces_m[cords[1]][cords[0]].viableMove(cords[2], cords[3], pieces_m)) ||
                    (pieces_m[cords[1]][cords[0]].team_m != team_t)) {
                System.out.println("not on board or other");
                error = true;
            }
            if (error) {
                System.out.println("Wprowadź kordynaty figury którą chcesz się ruszyć, oraz jej kordynaty docelowe zgodnie z formatem \"x1y1x2y2\", jeśli chcesz się poddać wpisz SURRENDER, aby wykonać roszadę wpisz ROSZADAL, albo ROSZADAS (long, short), READ aby wczytać grę, SAVE aby zapisać");
            }

        } while (error);
        return cords;
    }

}
class SaveManager {
    static String path_m;
    SaveManager(String path){
        path_m = path;
    }





    /* tutaj wchodzi element kontrowersji, ponieważ nie do końca wiedziałem czy każda figura ma mieć dopełnione pół bajta, tak jak w pliku który Pan wysłał,
    czy mają być dwie figury na sztywno na 3 bajtach, więc zrobiłem obie wersje, miedzy którymi przechodzi się komentując niepotrzebne fragmenty,
    obecna wersja działa na pliku wysłanym przez Pana. */

    //tutaj zaczac komentarz
    public static Piece[] openSavedGame() throws IOException{ //CHECK
        Piece[] pieces = new Piece[0];
        FileInputStream fis = new FileInputStream(path_m);
        byte[] byteArray = readToByteArray();
        pieces = byteArrayToPieceArray(byteArray);
        return pieces;
    }
    private static Piece[] byteArrayToPieceArray(byte[] byteArray) { //CHECK

        Piece[] toReturn = new Piece[0];
        int type1 = 0;
        int x1 = 0;
        int y1 = 0;
        int team1 = 0;
        int data = 0;
        int tmp = 0;
        int index = 0;
        for (int i = 0; i < byteArray.length; i+=2) {
            data = ((byteArray[i]) << 8) | (byteArray[i+1] & 0xFF);
            team1 = (data>>11)&0b0001;
            y1 = (data>>7)&0b1111;
            x1 = (data>>3)&0b1111;
            type1 = (data)&0b0111;
            Piece[] tmpArr = new Piece[toReturn.length+1];
            for (int j = 0; j < toReturn.length; j++) {
                tmpArr[j] = toReturn[j];
            }
            tmpArr[index++] = returnPieceObject(type1,x1,y1,team1);
            toReturn = tmpArr;
        }
        return toReturn;
    }
    //tutaj skończyć i odkomentować wszystko w dół
//    public static Piece[] openSavedGame() throws IOException {
//        Piece[] pieces = new Piece[0];
//        FileInputStream fis = new FileInputStream(path_m);
//        byte[] byteArray = readToByteArray();
//        pieces = validateOutput(byteArrayToPieceArray(byteArray));
//        return pieces;
//    }
//    private static Piece[] validateOutput(Piece[] arr) {
//        int countValid = 0;
//        for (Piece pionek:
//             arr) {
//            if(pionek.x_m < 9&& pionek.y_m < 9){
//                countValid++;
//            }
//        }
//        Piece[] validPieces = new Piece[countValid];
//        int index = 0;
//        for (Piece pionek:
//             arr) {
//            if(pionek.x_m < 9&& pionek.y_m < 9){
//                validPieces[index++] = pionek;
//            }
//        }
//        return validPieces;
//    }
//    private static Piece[] byteArrayToPieceArray(byte[] byteArray) {
//
//        Piece[] toReturn = new Piece[0];
//        int type1 = 0;
//        int x1 = 0;
//        int y1 = 0;
//        int team1 = 0;
//        int type2 = 0;
//        int x2 = 0;
//        int y2 = 0;
//        int team2 = 0;
//        int data = 0;
//        int tmp = 0;
//        int index = 0;
//        for (int i = 0; i < byteArray.length; i+=3) {
//            data = ((byteArray[i] & 0xFF) << 16) | ((byteArray[i+1] & 0xFF) << 8) | (byteArray[i+2] & 0xFF);
//            team1 = (data>>23)&0b0001;
//            y1 = (data>>19)&0b1111;
//            x1 = (data>>15)&0b1111;
//            type1 = (data>>12)&0b0111;
//            team2 = (data>>11)&1;
//            y2 = (data>>7)&0b1111;
//            x2 = (data>>3)&0b1111;
//            type2 = (data)&0b0111;
//            Piece[] tmpArr = new Piece[toReturn.length+2];
//            for (int j = 0; j < toReturn.length; j++) {
//                tmpArr[j] = toReturn[j];
//            }
//            tmpArr[index++] = returnPieceObject(type1,x1,y1,team1);
//            tmpArr[index++] = returnPieceObject(type2,x2,y2,team2);
//            toReturn = tmpArr;
//        }
//        return toReturn;
//    }

    private static Piece returnPieceObject(int type, int x, int y, int team) { //CHECK
        Color color = null;
        switch (team){
            case 1 -> color = Color.BLACK;
            case 0 -> color = Color.WHITE;
        }

        switch (type) {
            case 0 -> {
                return new Pawn(x,y,color);
            }
            case 1 -> {
                return new King(x,y,color);
            }
            case 2 -> {
                return new Queen(x,y,color);
            }
            case 3 -> {
                return new Rook(x,y,color);
            }
            case 4 -> {
                return new Bishop(x,y,color);
            }
            case 5 -> {
                return new Knight(x,y,color);
            }
        }
        return new Pawn(x,y,color);
    }

    private static byte[] readToByteArray() throws IOException { //CHECK
        byte[] arr = Files.readAllBytes(Path.of(path_m));
        return arr;
    }
    void saveGame(Piece[][] pieces) throws IOException { //CHECK
        savePieces(piecesToByteArray(piecesToArray(pieces)));

    }

    private void savePieces(byte[] pieces) throws IOException { //CHECK
        FileOutputStream fos = new FileOutputStream(path_m);
        for (byte b:
                pieces) {
            fos.write(b);
        }
        fos.flush();
        fos.close();
    }
    private byte[] piecesToByteArray(Piece[] arr){ //CHECK
        byte[] toReturn = new byte[arr.length*2];
        int index = 0;
        byte[] bufferArray = new byte[2];
        short toSave = 0;
        for (int i = 0; i < arr.length; i++) {
            ByteBuffer buff = ByteBuffer.allocate(2);
            toSave = (short) arr[i].team_m.getNum();
            toSave = (short) (toSave<<4);
            toSave = (short) (toSave|arr[i].y_m);
            toSave = (short) (toSave<<4);
            toSave = (short) (toSave|arr[i].x_m);
            toSave = (short) (toSave<<3);
            toSave = (short) (toSave|arr[i].type_m);
            buff.putShort(toSave);
            bufferArray = buff.array();
            for (int j = 0; j < 2; j++) {
                toReturn[index++] = bufferArray[j];
            }
        }
        return toReturn;
    }
    //ODKOMENTOWAĆ W DÓŁ DLA DRUGIEGO SPOSOBU ZAPISU

    //    private byte[] piecesToByteArray(Piece[] arr){
//        int toSave = 0;
//        Piece p1 = null;
//        Piece p2 = null;
//        boolean fillRest = false;
//        byte[] final1 = new byte[0];
//        for (int i = 0; i < arr.length; i+=2) {
//            p1 = arr[i];
//            if(i+1 < arr.length){
//                p2 = arr[i+1];
//            }else{
//                fillRest = true;
//            }
//
//            toSave = p1.team_m.getNum();
//            toSave = toSave<<4;
//            toSave = toSave|p1.y_m;
//            toSave = toSave<<4;
//            toSave = toSave|p1.x_m;
//            toSave = toSave<<3;
//            toSave = toSave|p1.type_m;
//            if(!fillRest){
//                toSave = toSave<<1;
//                toSave = toSave|p2.team_m.getNum();
//                toSave = toSave<<4;
//                toSave = toSave|p2.y_m;
//                toSave = toSave<<4;
//                toSave = toSave|p2.x_m;
//                toSave = toSave<<3;
//                toSave = toSave|p2.type_m;
//            }else{
//                toSave = toSave<<12;
//                toSave = toSave|4095;
//            }
//
//            byte[] bytes = ByteBuffer.allocate(4).putInt(toSave).array();
//            byte[] toReturn = new byte[3];
//            for (int j = 0; j < toReturn.length; j++) {
//                toReturn[j] = bytes[j+1];
//            }
//            byte[] tmp = new byte[final1.length+3];
//            for (int j = 0; j < final1.length; j++) {
//                tmp[j] = final1[j];
//            }
//            for (int j = 0; j < 3; j++) {
//                tmp[tmp.length-3+j] = toReturn[j];
//            }
//            final1 = tmp;
//        }
//        return final1;
//    }

    //TU KOŃCZY SIĘ METODA
    Piece[] piecesToArray(Piece[][] p1){ //CHECK
        int countPieces = 0;
        for (int i = 1; i < p1.length; i++) {
            for (int j = 1; j < p1[i].length; j++) {
                if(p1[i][j] != null){
                    countPieces++;
                }
            }
        }
        Piece[] pArray = new Piece[countPieces+Gameboard.zbitePionki.size()];
        int lastIndex = 0;
        for (int i = 1; i < p1.length; i++) {
            for (int j = 1; j < p1[i].length; j++) {
                if(p1[i][j]!=null) {
                    pArray[lastIndex++] = p1[i][j];
                }
            }
        }
        for (Piece piece:Gameboard.zbitePionki) {
            pArray[lastIndex++] = piece;
        }

        return pArray;
    }
}
abstract class Piece implements Cloneable{
    int type_m;
    int x_m;
    int y_m;
    Color team_m;
    boolean madeMove_m;

    Piece(int type, int x, int y, Color team){
        type_m = type;
        x_m = x;
        y_m = y;
        team_m = team;
        madeMove_m = false;
    }
    @Override
    public Object clone() {
        try {
            return (Piece) super.clone();
        } catch (CloneNotSupportedException e) {
        }
        return null;
    }
    abstract boolean viableMove(int x, int y, Piece[][] pieces);
    public String toString(){
        return "pionek o typie " + type_m + " koloru " + team_m + " na " + x_m + " " + y_m;
    }

}
class King extends Piece{
    public boolean madeMove = false;
    King(int x, int y, Color team) {
        super(1, x, y, team);
    }

    @Override
    boolean viableMove(int x, int y, Piece[][] pieces) {
        if(!(x-this.x_m == 0 && y-this.y_m == 0)) {
            if (pieces[y][x] == null || pieces[y][x].team_m != this.team_m) {
                if (((Math.abs(this.x_m - x)) <= 1) && ((Math.abs(this.y_m - y)) <= 1)) {
                    return true;
                }
            }
        }
        return false;
    }
}
class Queen extends Piece{
    Queen(int x, int y, Color team) {
        super(2, x, y, team);
    }

    @Override
    boolean viableMove(int x, int y, Piece[][] pieces) { //CHECK
        if(!(x-this.x_m == 0 && y-this.y_m == 0)) {
            if (pieces[y][x] == null || pieces[y][x].team_m != this.team_m) {
                if (Math.abs(this.x_m - x) <=1 && Math.abs(this.y_m - y) <= 1) {
                    return true;
                }
                if (Math.abs(x_m - x) == Math.abs(y_m - y)) {
                    int jumpX = ((x_m < x)?1:(x_m > x)?-1:0);
                    int jumpY = ((y_m < y)?1:(y_m > y)?-1:0);
                    int currX = x_m + jumpX;
                    int currY = y_m + jumpY;
                    while(currX != x || currY != y){
                        if(pieces[currY][currX] != null){
                            return false;
                        }
                        currX += jumpX;
                        currY += jumpY;
                    }
                    return true;
                }
                if(x_m-x == 0 | y_m-y == 0){
                    int jumpX = ((x_m < x)?1:(x_m > x)?-1:0);
                    int jumpY = ((y_m < y)?1:(y_m > y)?-1:0);
                    int currX = x_m + jumpX;
                    int currY = y_m + jumpY;
                    while(currX != x || currY != y){
                        if(pieces[currY][currX] != null){
                            return false;
                        }
                        currX += jumpX;
                        currY += jumpY;
                    }
                    return true;
                }

            }
        }
        return false;
    }
}
class Bishop extends Piece{
    Bishop(int x, int y, Color team) {
        super(4, x, y, team);
    }

    @Override
    boolean viableMove(int x, int y, Piece[][] pieces) { //CHECK
        if(!(x-this.x_m == 0 && y-this.y_m == 0)) {
            if (pieces[y][x] == null || pieces[y][x].team_m != this.team_m) {
                if (Math.abs(x_m - x) == Math.abs(y_m - y)) {
                    int jumpX = ((x_m < x) ? 1 : (x_m > x) ? -1 : 0);
                    int jumpY = ((y_m < y) ? 1 : (y_m > y) ? -1 : 0);
                    int currX = x_m + jumpX;
                    int currY = y_m + jumpY;
                    while (currX != x || currY != y) {
                        if (pieces[currY][currX] != null) {
                            return false;
                        }
                        currX += jumpX;
                        currY += jumpY;
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
class Knight extends Piece{
    Knight(int x, int y, Color team) {
        super(5, x, y, team);
    }

    @Override
    boolean viableMove(int x, int y, Piece[][] pieces) { //CHECK
        if(!(x-this.x_m == 0 && y-this.y_m == 0)) {
            if (pieces[y][x] == null || pieces[y][x].team_m != this.team_m) {
                if((Math.abs(x - x_m) == 2 && Math.abs(y-y_m) == 1) || (Math.abs(x - x_m) == 1 && Math.abs(y-y_m) == 2)){
                    return true;
                }
            }
        }
        return false;
    }
}
class Pawn extends Piece{
    Pawn(int x, int y, Color team) {
        super(0, x, y, team);
    }

    @Override
    boolean viableMove(int x, int y, Piece[][] pieces) {//CHECK
        if(!(x-this.x_m == 0 && y-this.y_m == 0)) {
            if (pieces[y][x] == null || pieces[y][x].team_m != this.team_m) {
                if(team_m == Color.BLACK){
                    if(y_m == 2){
                        if(Math.abs(y_m - y) == 2 && x_m - x == 0) {
                            if(pieces[y][x] == null){
                                return true;
                            }
                        }
                    }
                }
                if(team_m == Color.WHITE){
                    if(y_m == 7){
                        if(Math.abs(y_m - y) == 2 && x_m - x == 0) {
                            if(pieces[y][x] == null){
                                return true;
                            }
                        }
                    }
                }
                if(team_m == Color.BLACK){
                    if(y_m - y > 0){
                        return false;
                    }
                }
                if(team_m == Color.WHITE){
                    if(y_m - y < 0){

                        return false;
                    }
                }
                if(Math.abs(y_m - y) == 1 && x_m - x == 0) {
                    if(pieces[y][x] == null){
                        return true;
                    }
                }
                if(((Math.abs(y_m - y)) == 1) && ((Math.abs(x_m - x)) == 1) && (pieces[y][x] != null?(pieces[y][x].team_m != this.team_m): false)){
                    return true;
                }
            }
        }
        return false;
    }
}
class Rook extends Piece{
    Rook(int x, int y, Color team) {
        super(3, x, y, team);
    }

    @Override
    boolean viableMove(int x, int y, Piece[][] pieces) {//CHECK
        if (!(x - this.x_m == 0 && y - this.y_m == 0)) {
            if (pieces[y][x] == null || pieces[y][x].team_m != this.team_m) {
                if(x_m - x == 0 | y_m - y == 0){
                    int jumpX = ((x_m < x)?1:(x_m > x)?-1:0);
                    int jumpY = ((y_m < y)?1:(y_m > y)?-1:0);
                    int currX = x_m + jumpX;
                    int currY = y_m + jumpY;
                    while(currX != x || currY != y){
                        if(pieces[currY][currX] != null){
                            return false;
                        }
                        currX += jumpX;
                        currY += jumpY;
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
enum Color {

    BLACK(1),WHITE(0);
    private int num;

    Color(int i) {
        num = i;
    }
    public int getNum(){
        return num;
    }
}
interface Print {
    public String signToPrint(Piece p);
}


