import java.util.Scanner;

public class Gameboard {
    Piece[][] pieces_m;

    Gameboard(int x){
        pieces_m = new Piece[x+1][x+1];
    }
    void setGame(){
        //Black
        pieces_m[1][1] = new Rook(1,1, Color.BLACK);
        pieces_m[1][8] = new Rook(8,1, Color.BLACK);
        pieces_m[1][2] = new Knight(2,1,Color.BLACK);
        pieces_m[1][7] = new Knight(7,1,Color.BLACK);
        pieces_m[1][3] = new Bishop(3,1,Color.BLACK);
        pieces_m[1][6] = new Bishop(6,1,Color.BLACK);
        pieces_m[1][4] = new Queen(4,1,Color.BLACK);
        pieces_m[1][5] = new King(5,1,Color.BLACK);
        for (int i = 1; i < pieces_m[2].length; i++) {
            pieces_m[2][i] = new Pawn(i,2,Color.BLACK);
        }

        //White
        pieces_m[8][1] = new Rook(1,8, Color.WHITE);
        pieces_m[8][8] = new Rook(8,8, Color.WHITE);
        pieces_m[8][2] = new Knight(2,8,Color.WHITE);
        pieces_m[8][7] = new Knight(7,8,Color.WHITE);
        pieces_m[8][3] = new Bishop(3,8,Color.WHITE);
        pieces_m[8][6] = new Bishop(6,8,Color.WHITE);
        pieces_m[8][5] = new Queen(5,8,Color.WHITE);
        pieces_m[8][4] = new King(4,8,Color.WHITE);
        for (int i = 1; i < pieces_m[7].length; i++) {
            pieces_m[7][i] = new Pawn(i,7,Color.WHITE);
        }
    }
    void showBoard(){
        System.out.print(" " + "\t");
        for (int i = 1; i < pieces_m.length;i++) {
            System.out.print(i + "\t");
        }
        System.out.println();
        int counterForRows = 1;
        for (int i = 1; i < pieces_m.length; i++) {
            System.out.print(counterForRows++ + "\t");
            for (int j = 1; j < pieces_m[i].length; j++) {
                if(pieces_m[i][j] == null){
                    System.out.print("□" + "\t");
                }else {
                    System.out.print(signToPrint(pieces_m[i][j]) + "\t");
                }
            }
            System.out.println();
        }
    }
    String signToPrint(Piece p1){
        int type = p1.type_m;
        Color color = p1.team_m;
        if(color == Color.WHITE){
            switch (type){
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
        }else{
            switch (type){
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
    boolean stillOnBoard(int x,int y,int x1,int y1){
        if(x < pieces_m.length && x >= 0 && y < pieces_m.length && y >= 0 && x1 < pieces_m.length && x1 >= 0 && y1 < pieces_m.length && y1 >= 0){
            return true;
        }
        return false;
    }
    boolean checkIfCheck(Piece[][] p1,Color color){
        int x = 0;
        int y = 0;
        for(Piece[] pp:p1){
            for(Piece ppp:pp){
                if(ppp != null) {
                    if (ppp.team_m == color && ppp.type_m == 1) {
                        x = ppp.x_m;
                        y = ppp.y_m;
                    }
                }
            }
        }
        for(Piece[] pp:p1){
            for(Piece ppp:pp){
                if(ppp != null) {
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
//    boolean checkIfMate(Piece[][] p1,Color color){ TODO
//        if(checkIfCheck(p1,color)){
//
//        }
//        return false
//    }
//    void bicieWPrzelocie(Piece[][] p1,int x1){ TODO
//        checkIfAnyCanditates(p1,x1);
//
//    }
//    void roszada(){ TODO
//
//    }
//    void promocjaPiona(){ TODO
//
//    }
//    void poddanie(){ TODO
//
//    }

    public static void main(String[] args) {
        Color gracz = Color.WHITE;
        Gameboard gameboard = new Gameboard(8);
        gameboard.setGame();
        Scanner scanner = new Scanner(System.in);
        boolean work = true;
        while(work){
            gameboard.showBoard();
            System.out.println("Wprowadź kordynaty figury którą chcesz się ruszyć, oraz po spacji jej kordynaty docelowe zgodnie z formatem \"x1y1 x2y2\"");
            String kordy;
            boolean wrong = false;
            int x1 = 0;
            int y1 = 0;
            int x2 = 0;
            int y2 = 0;
            do {
                kordy = scanner.nextLine();
                wrong = false;
                try {
                    x1 = kordy.charAt(0) - 48;
                    y1 = kordy.charAt(1) - 48;
                    x2 = kordy.charAt(3) - 48;
                    y2 = kordy.charAt(4) - 48;
                }catch (StringIndexOutOfBoundsException sioobe){
                    wrong = true;
                    System.out.println("trzymaj się formatu.");
                }
            }while(wrong);
            boolean wlascicielPionka;
            boolean stillOnBoard;
            boolean poleZPionkiem;
            boolean dobryRuch;
            boolean wrongFormat;

            do{
                    wrongFormat = false;
                    poleZPionkiem = (gameboard.pieces_m[y1][x1] != null);
                    if (poleZPionkiem) {
                        wlascicielPionka = (gameboard.pieces_m[y1][x1].team_m == gracz);
                    } else {
                        wlascicielPionka = true;
                    }
                    try {
                        dobryRuch = gameboard.pieces_m[y1][x1].viableMove(x2, y2, gameboard.pieces_m);
                    } catch (NullPointerException npe) {
                        dobryRuch = false;
                    }
                    stillOnBoard = gameboard.stillOnBoard(x1, y1, x2, y2);
                try {
                    if (!wlascicielPionka || !stillOnBoard || !poleZPionkiem || !dobryRuch) {
                        System.out.println("Coś źle wpisałeś, wprowadz kordynaty jeszcze raz \"x1y1 x2y2\"");
                        kordy = scanner.nextLine();
                        x1 = kordy.charAt(0) - 48;
                        y1 = kordy.charAt(1) - 48;
                        x2 = kordy.charAt(3) - 48;
                        y2 = kordy.charAt(4) - 48;
                    }
                }catch (StringIndexOutOfBoundsException sioobe){
                    wrongFormat = true;
                    System.out.println("Trzymaj sie formatu.");
                }
            }while(!wlascicielPionka || !stillOnBoard || !poleZPionkiem || !dobryRuch || wrongFormat);
//            if(y1 == 2 || y1 == 7){
//                gameboard.bicieWPrzelocie(gameboard.pieces_m,x1);
//            }
            gameboard.pieces_m[y2][x2] = gameboard.pieces_m[y1][x1];
            gameboard.pieces_m[y1][x1] = null;
            gameboard.pieces_m[y2][x2].x_m = x2;
            gameboard.pieces_m[y2][x2].y_m = y2;
            System.out.println("następny gracz");
            gracz = (gracz == Color.BLACK?Color.WHITE:Color.BLACK);
            if(gameboard.checkIfCheck(gameboard.pieces_m,gracz)){
                System.out.println("SZACH");
            }


        }


    }
}

