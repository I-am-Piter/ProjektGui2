public class Knight extends Piece{
    Knight(int x, int y, Color team) {
        super(5, x, y, team);
    }

    @Override
    boolean viableMove(int x, int y, Piece[][] pieces) {
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
