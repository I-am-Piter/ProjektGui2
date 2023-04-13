public class Pawn extends Piece{
    Pawn(int x, int y, Color team) {
        super(0, x, y, team);
    }
    //zaimplementuj możliwość przesunięcia o 2 pola z pozycji startowej

    @Override
    boolean viableMove(int x, int y, Piece[][] pieces) {
        if(!(x-this.x_m == 0 && y-this.y_m == 0)) {
            if (pieces[y][x] == null || pieces[y][x].team_m != this.team_m) {
                if(team_m == Color.BLACK){
                    if(y_m == 1){
                        if(Math.abs(y_m - y) == 2 && x_m - x == 0) {
                            if(pieces[y][x] == null){
                                return true;
                            }
                        }
                    }
                }
                if(team_m == Color.WHITE){
                    if(y_m == 6){
                        if(Math.abs(y_m - y) == 2 && x_m - x == 0) {
                            if(pieces[y][x] == null){
                                return true;
                            }
                        }
                    }
                }
                if(team_m == Color.BLACK){
                    if(x_m - x > 0){
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
                if((Math.abs(y_m - y) == 1) && (Math.abs(x_m - x) == 1) && ((pieces[y][x] != null?pieces[y][x].team_m != this.team_m: false))){

                    return true;
                }
            }
        }
        return false;
    }
}
