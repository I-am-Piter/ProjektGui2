public class Knight extends Piece{
    Knight(int x, int y, int team) {
        super(5, x, y, team);
    }

    @Override
    boolean viableMove(int x, int y, Piece[][] pieces) {
        if(!(x-this.x_m == 0 && y-this.y_m == 0)) {
            if (pieces[y][x] == null || pieces[y][x].team_m != this.team_m) {

            }
        }
        return false;
    }
}
