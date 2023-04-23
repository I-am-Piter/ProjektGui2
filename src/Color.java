public enum Color {

    BLACK(1),WHITE(0);
    private int num;

    Color(int i) {
        num = i;
    }
    public int getNum(){
        return num;
    }
}
