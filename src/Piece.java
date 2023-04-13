public abstract class Piece {
    //1 - król, 2- hetman, 3 - wieża, 4- goniec, 5 - skoczek, 6 - pion. Do zapisu zmienię pion na 0, teraz robię planszę na tablicy int dla ułatwienia
    int type_m;
    int x_m;
    int y_m;
    int team_m;

    Piece(int type, int x, int y, int team){
        type_m = type;
        x_m = x;
        y_m = y;
        team_m = team;
    }
    abstract boolean viableMove(int x, int y, Piece[][] pieces);
    boolean isMoveStillOnBoard(int x,int y, Piece[][] pieces){
        if(x < pieces.length && x>=0 && y < pieces.length && y>=0){
            return this.viableMove(x,y,pieces);
        }
        return false;
    }


}
