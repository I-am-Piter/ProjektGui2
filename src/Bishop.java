public class Bishop extends Piece{
    Bishop(int x, int y, int team) {
        super(4, x, y, team);
    }

    @Override
    boolean viableMove(int x, int y, Piece[][] pieces) {
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
