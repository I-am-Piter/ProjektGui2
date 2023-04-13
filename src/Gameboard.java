import java.util.Scanner;

public class Gameboard {
    Piece[][] pieces_m;

    Gameboard(int x){
        pieces_m = new Piece[x][x];
    }
    void setGame(){
        //Black
        pieces_m[0][0] = new Rook(0,0, Color.BLACK);
        pieces_m[0][7] = new Rook(7,0, Color.BLACK);
        pieces_m[0][1] = new Knight(1,0,Color.BLACK);
        pieces_m[0][6] = new Knight(6,0,Color.BLACK);
        pieces_m[0][2] = new Bishop(2,0,Color.BLACK);
        pieces_m[0][5] = new Bishop(5,0,Color.BLACK);
        pieces_m[0][3] = new Queen(3,0,Color.BLACK);
        pieces_m[0][4] = new King(4,0,Color.BLACK);
        for (int i = 0; i < pieces_m[1].length; i++) {
            pieces_m[1][i] = new Pawn(i,1,Color.BLACK);
        }

        //White
        pieces_m[7][0] = new Rook(0,7, Color.WHITE);
        pieces_m[7][7] = new Rook(7,7, Color.WHITE);
        pieces_m[7][1] = new Knight(1,7,Color.WHITE);
        pieces_m[7][6] = new Knight(6,7,Color.WHITE);
        pieces_m[7][2] = new Bishop(2,7,Color.WHITE);
        pieces_m[7][5] = new Bishop(5,7,Color.WHITE);
        pieces_m[7][4] = new Queen(4,7,Color.WHITE);
        pieces_m[7][3] = new King(3,7,Color.WHITE);
        for (int i = 0; i < pieces_m[6].length; i++) {
            pieces_m[6][i] = new Pawn(i,6,Color.WHITE);
        }
    }
    void showBoard(){
        System.out.print(" " + "\t");
        for (int i = 0; i < pieces_m.length;i++) {
            System.out.print(i + "\t");
        }
        System.out.println();
        int counterForRows = 0;
        for(Piece[] p1:pieces_m){
            System.out.print(counterForRows++ + "\t");
            for(Piece p2:p1){
                if(p2 == null){
                    System.out.print("□" + "\t");
                }else {
                    System.out.print(signToPrint(p2) + "\t");
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
    boolean stillOnBoard(int x,int y){
        if(x < pieces_m.length && x >= 0 && y < pieces_m.length && y >= 0){
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
//    boolean checkIfMate(Piece[][] p1,Color color){
//        if(checkIfCheck(p1,color)){
//
//        }
//    }

    public static void main(String[] args) {
        Color gracz = Color.WHITE;
        Gameboard gameboard = new Gameboard(8);
        gameboard.setGame();
        Scanner scanner = new Scanner(System.in);
        boolean work = true;
        while(work){
            gameboard.showBoard();
            System.out.println("Wprowadź kordynaty figury którą chcesz się ruszyć (x,y)");
            String kordy = scanner.nextLine();
            int x1 = kordy.charAt(0)-48;
            int y1 = kordy.charAt(1)-48;
            boolean wlascicielPionka;
            boolean stillOnBoard;
            boolean poleZPionkiem;
            boolean jeszczeRaz;
            do{
                poleZPionkiem = (gameboard.pieces_m[y1][x1] != null);
                if(poleZPionkiem) {
                    wlascicielPionka = (gameboard.pieces_m[y1][x1].team_m == gracz);
                }else{
                    wlascicielPionka = true;
                }
                stillOnBoard = gameboard.stillOnBoard(x1,y1);
                if(!wlascicielPionka){
                    System.out.println("Nie jesteś wlascicielem pionka, wprowadz kordynaty figury jeszcze raz (x,y)");
                    kordy = scanner.nextLine();
                    x1 = kordy.charAt(0)-48;
                    y1 = kordy.charAt(1)-48;
                }
                if(!stillOnBoard){
                    System.out.println("Wyszedles poza pole gry, wprowadz kordynaty figury jeszcze raz (x,y)");
                    kordy = scanner.nextLine();
                    x1 = kordy.charAt(0)-48;
                    y1 = kordy.charAt(1)-48;
                }
                if(!poleZPionkiem){
                    System.out.println("Puste pole, wprowadz kordynaty figury jeszcze raz (x,y)");
                    kordy = scanner.nextLine();
                    x1 = kordy.charAt(0)-48;
                    y1 = kordy.charAt(1)-48;
                }
            }while(!wlascicielPionka || !stillOnBoard || !poleZPionkiem);

            System.out.println("wprowadź kordynaty docelowe (x,y)");
            kordy = scanner.nextLine();
            int x2 = kordy.charAt(0)-48;
            int y2 = kordy.charAt(1)-48;
            boolean dobryRuch;
            do{
                dobryRuch = gameboard.pieces_m[y1][x1].viableMove(x2,y2, gameboard.pieces_m);
                if(!dobryRuch){
                    System.out.println("niedobry ruch, wprowadź jeszcze raz kordynaty docelowe (x,y)");
                    kordy = scanner.nextLine();
                    x2 = kordy.charAt(0)-48;
                    y2 = kordy.charAt(1)-48;
                }
                do{
                    stillOnBoard = gameboard.stillOnBoard(x1,y1);
                    if(!stillOnBoard){
                        System.out.println("Wyszedles poza pole gry, wprowadz kordynaty figury jeszcze raz (x,y)");
                        kordy = scanner.nextLine();
                        x2 = kordy.charAt(0)-48;
                        y2 = kordy.charAt(1)-48;
                    }
                }while(!stillOnBoard);
            }while(!dobryRuch);
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

