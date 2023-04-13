public class King extends Piece{
    King(int x, int y, Color team) {
        super(1, x, y, team);
    }

    @Override
    boolean viableMove(int x, int y, Piece[][] pieces) {
        if(!(x-this.x_m == 0 && y-this.y_m == 0)) {
            if (pieces[y][x] == null || pieces[y][x].team_m != this.team_m) {
                if (Math.abs(this.x_m - x) <=1 && Math.abs(this.y_m - y) <= 1) {
                    return true;
                }
            }
        }
        return false;
    }
}
