import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Gameboard {
    Piece[][] pieces_m;
    static ArrayList<Piece> zbitePionki;


    Gameboard(int x) {
        pieces_m = new Piece[x + 1][x + 1];
        zbitePionki = new ArrayList<>();
    }

    void setGame() {
        //Black
        pieces_m[1][1] = new Rook(1, 1, Color.BLACK);
//        pieces_m[1][8] = new Rook(8, 1, Color.BLACK);
//        pieces_m[1][2] = new Knight(2, 1, Color.BLACK);
//        pieces_m[1][7] = new Knight(7, 1, Color.BLACK);
//        pieces_m[1][3] = new Bishop(3, 1, Color.BLACK);
//        pieces_m[1][6] = new Bishop(6, 1, Color.BLACK);
        pieces_m[1][4] = new Queen(4, 1, Color.BLACK);
//        pieces_m[1][5] = new King(5, 1, Color.BLACK);
//        for (int i = 1; i < pieces_m[2].length; i++) {
//            pieces_m[2][i] = new Pawn(i, 2, Color.BLACK);
//        }

//        White
//        pieces_m[8][1] = new Rook(1, 8, Color.WHITE);
//        pieces_m[8][8] = new Rook(8, 8, Color.WHITE);
//        pieces_m[8][2] = new Knight(2, 8, Color.WHITE);
//        pieces_m[8][7] = new Knight(7, 8, Color.WHITE);
//        pieces_m[8][3] = new Bishop(3, 8, Color.WHITE);
//        pieces_m[8][6] = new Bishop(6, 8, Color.WHITE);
//        pieces_m[8][5] = new Queen(5, 8, Color.WHITE);
        pieces_m[8][4] = new King(4, 8, Color.WHITE);
//        for (int i = 1; i < pieces_m[7].length; i++) {
//            pieces_m[7][i] = new Pawn(i, 7, Color.WHITE);
//        }

    }

    void showBoard() {
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
                    System.out.print(signToPrint(pieces_m[i][j]) + "\t");
                }
            }
            System.out.println();
        }
    }

    String signToPrint(Piece p1) {
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
    }

    boolean stillOnBoard(int x, int y, int x1, int y1) {
        if (x < pieces_m.length && x >= 1 && y < pieces_m.length && y >= 1 && x1 < pieces_m.length && x1 >= 1 && y1 < pieces_m.length && y1 >= 1) {
            return true;
        }
        return false;
    }

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
    }
    public boolean isMoveLegal(Piece[][] p1, Color color, int x1, int y1, int x2,int y2){
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

    static Piece[][] makeCopy(Piece[][] p1) {
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
    void openFromSave(String path) throws IOException {
        SaveManager.path_m = path;
        Piece[] piecesTmp = SaveManager.openSavedGame();
        fillPiece2dArray(piecesTmp);
        fillZbitePionki(piecesTmp);
        if(checkIfMate(pieces_m,Color.BLACK) || checkIfMate(pieces_m,Color.WHITE)){
            System.out.println("Wczytałeś zakończoną grę.");
        }
    }

    private void fillZbitePionki(Piece[] piecesTmp) {
        zbitePionki = new ArrayList<>();
        for (Piece p:
             piecesTmp) {
            if(p.x_m == 0 && p.y_m == 0){
                zbitePionki.add(p);
            }
        }
    }

    private void fillPiece2dArray(Piece[] piecesTmp) {
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

    boolean checkIfMate(Piece[][] p1, Color color) {
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


    boolean bicieWPrzelocie(Piece[][] p1,int x,int y1, int y2, Color team){
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

    private boolean checkIfAnyCanditates(Piece[][] p1) {
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

    boolean roszada(Color team, boolean ls) {
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

    void makeRoszada(Color team, boolean ls) {
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

    void promocjaPiona(int x, int y, Color color) {
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

    void zbicie(int x, int y) {
        pieces_m[y][x].x_m = 0;
        pieces_m[y][x].y_m = 0;
        zbitePionki.add(pieces_m[y][x]);
        for (Piece pionek :
                zbitePionki) {
        }
    }

    public int[] getMove(Color team_t) {
        Scanner scanner = new Scanner(System.in);
        int[] cords = new int[4];
        String input;
        System.out.println("Wprowadź kordynaty figury którą chcesz się ruszyć, oraz jej kordynaty docelowe zgodnie z formatem \"x1y1x2y2\", jeśli chcesz się poddać wpisz SURRENDER, aby wykonać roszadę wpisz ROSZADAL, albo ROSZADAS (long, short)");
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
                    System.out.println("Wprowadź kordynaty figury którą chcesz się ruszyć, oraz jej kordynaty docelowe zgodnie z formatem \"x1y1x2y2\",tym razem poprawnie, jeśli chcesz się poddać wpisz SURRENDER");
                    continue;
                }
            }
            if (input.equals("ROSZADAS")) {
                if (roszada(team_t, false)) {
                    return new int[]{10, 10, 10, 10};
                } else {
                    error = true;
                    System.out.println("Wprowadź kordynaty figury którą chcesz się ruszyć, oraz jej kordynaty docelowe zgodnie z formatem \"x1y1x2y2\",tym razem poprawnie, jeśli chcesz się poddać wpisz SURRENDER");
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
                System.out.println("Wprowadź kordynaty figury którą chcesz się ruszyć, oraz jej kordynaty docelowe zgodnie z formatem \"x1y1x2y2\",tym razem poprawnie, jeśli chcesz się poddać wpisz SURRENDER");
            }

        } while (error);
        return cords;
    }

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
            copy2 = makeCopy(gameboard.makeCopy(gameboard.pieces_m));
            if (gameboard.checkIfCheck(gameboard.pieces_m, gracz)) {
                if (gameboard.checkIfMate(copy2, gracz)) {
                    System.out.println("Gra została zakończona zwycięstwem: " + (gracz == Color.BLACK ? Color.WHITE : Color.BLACK));
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


