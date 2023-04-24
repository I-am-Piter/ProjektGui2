public abstract class Piece implements Cloneable{
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
