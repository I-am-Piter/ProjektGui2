import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class SaveManager {
    static String path_m;
    SaveManager(String path){
        path_m = path;
    }





    /* tutaj wchodzi element kontrowersji, ponieważ nie do końca wiedziałem czy każda figura ma mieć dopełnione pół bajta, tak jak w pliku który Pan wysłał,
    czy mają być dwie figury na sztywno na 3 bajtach, więc zrobiłem obie wersje, miedzy którymi przechodzi się komentując niepotrzebne fragmenty,
    obecna wersja działa na pliku wysłanym przez Pana. */

    //tutaj zaczac komentarz
    public static Piece[] openSavedGame() throws IOException{
        Piece[] pieces = new Piece[0];
        FileInputStream fis = new FileInputStream(path_m);
        byte[] byteArray = readToByteArray();
        pieces = byteArrayToPieceArray(byteArray);
        return pieces;
    }
    private static Piece[] byteArrayToPieceArray(byte[] byteArray) {

        Piece[] toReturn = new Piece[0];
        int type1 = 0;
        int x1 = 0;
        int y1 = 0;
        int team1 = 0;
        int data = 0;
        int tmp = 0;
        int index = 0;
        for (int i = 0; i < byteArray.length; i+=2) {
            data = ((byteArray[i]) << 8) | (byteArray[i+1] & 0xFF);
            team1 = (data>>11)&0b0001;
            y1 = (data>>7)&0b1111;
            x1 = (data>>3)&0b1111;
            type1 = (data)&0b0111;
            Piece[] tmpArr = new Piece[toReturn.length+1];
            for (int j = 0; j < toReturn.length; j++) {
                tmpArr[j] = toReturn[j];
            }
            tmpArr[index++] = returnPieceObject(type1,x1,y1,team1);
            toReturn = tmpArr;
        }
        return toReturn;
    }
    //tutaj skończyć i odkomentować wszystko w dół
//    public static Piece[] openSavedGame() throws IOException {
//        Piece[] pieces = new Piece[0];
//        FileInputStream fis = new FileInputStream(path_m);
//        byte[] byteArray = readToByteArray();
//        pieces = validateOutput(byteArrayToPieceArray(byteArray));
//        return pieces;
//    }
//    private static Piece[] validateOutput(Piece[] arr) {
//        int countValid = 0;
//        for (Piece pionek:
//             arr) {
//            if(pionek.x_m < 9&& pionek.y_m < 9){
//                countValid++;
//            }
//        }
//        Piece[] validPieces = new Piece[countValid];
//        int index = 0;
//        for (Piece pionek:
//             arr) {
//            if(pionek.x_m < 9&& pionek.y_m < 9){
//                validPieces[index++] = pionek;
//            }
//        }
//        return validPieces;
//    }
//    private static Piece[] byteArrayToPieceArray(byte[] byteArray) {
//
//        Piece[] toReturn = new Piece[0];
//        int type1 = 0;
//        int x1 = 0;
//        int y1 = 0;
//        int team1 = 0;
//        int type2 = 0;
//        int x2 = 0;
//        int y2 = 0;
//        int team2 = 0;
//        int data = 0;
//        int tmp = 0;
//        int index = 0;
//        for (int i = 0; i < byteArray.length; i+=3) {
//            data = ((byteArray[i] & 0xFF) << 16) | ((byteArray[i+1] & 0xFF) << 8) | (byteArray[i+2] & 0xFF);
//            team1 = (data>>23)&0b0001;
//            y1 = (data>>19)&0b1111;
//            x1 = (data>>15)&0b1111;
//            type1 = (data>>12)&0b0111;
//            team2 = (data>>11)&1;
//            y2 = (data>>7)&0b1111;
//            x2 = (data>>3)&0b1111;
//            type2 = (data)&0b0111;
//            Piece[] tmpArr = new Piece[toReturn.length+2];
//            for (int j = 0; j < toReturn.length; j++) {
//                tmpArr[j] = toReturn[j];
//            }
//            tmpArr[index++] = returnPieceObject(type1,x1,y1,team1);
//            tmpArr[index++] = returnPieceObject(type2,x2,y2,team2);
//            toReturn = tmpArr;
//        }
//        return toReturn;
//    }

    private static Piece returnPieceObject(int type, int x, int y, int team) {
        Color color = null;
        switch (team){
            case 1 -> color = Color.BLACK;
            case 0 -> color = Color.WHITE;
        }

        switch (type) {
            case 0 -> {
                return new Pawn(x,y,color);
            }
            case 1 -> {
                return new King(x,y,color);
            }
            case 2 -> {
                return new Queen(x,y,color);
            }
            case 3 -> {
                return new Rook(x,y,color);
            }
            case 4 -> {
                return new Bishop(x,y,color);
            }
            case 5 -> {
                return new Knight(x,y,color);
            }
        }
        return new Pawn(x,y,color);
    }

    private static byte[] readToByteArray() throws IOException {
        byte[] arr = Files.readAllBytes(Path.of(path_m));
        return arr;
    }
    void saveGame(Piece[][] pieces) throws IOException {
        savePieces(piecesToByteArray(piecesToArray(pieces)));

    }

    private void savePieces(byte[] pieces) throws IOException {
        FileOutputStream fos = new FileOutputStream(path_m);


        for (byte b:
                pieces) {
            fos.write(b);
        }


        fos.flush();
        fos.close();
    }
    private byte[] piecesToByteArray(Piece[] arr){
        byte[] toReturn = new byte[arr.length*2];
        int index = 0;
        byte[] bufferArray = new byte[2];
        short toSave = 0;
        for (int i = 0; i < arr.length; i++) {
            ByteBuffer buff = ByteBuffer.allocate(2);
            toSave = (short) arr[i].team_m.getNum();
            toSave = (short) (toSave<<4);
            toSave = (short) (toSave|arr[i].y_m);
            toSave = (short) (toSave<<4);
            toSave = (short) (toSave|arr[i].x_m);
            toSave = (short) (toSave<<3);
            toSave = (short) (toSave|arr[i].type_m);
            buff.putShort(toSave);
            bufferArray = buff.array();
            for (int j = 0; j < 2; j++) {
                toReturn[index++] = bufferArray[j];
            }
        }
        return toReturn;
    }
    //ODKOMENTOWAĆ W DÓŁ DLA DRUGIEGO SPOSOBU ZAPISU

    //    private byte[] piecesToByteArray(Piece[] arr){
//        int toSave = 0;
//        Piece p1 = null;
//        Piece p2 = null;
//        boolean fillRest = false;
//        byte[] final1 = new byte[0];
//        for (int i = 0; i < arr.length; i+=2) {
//            p1 = arr[i];
//            if(i+1 < arr.length){
//                p2 = arr[i+1];
//            }else{
//                fillRest = true;
//            }
//
//            toSave = p1.team_m.getNum();
//            toSave = toSave<<4;
//            toSave = toSave|p1.y_m;
//            toSave = toSave<<4;
//            toSave = toSave|p1.x_m;
//            toSave = toSave<<3;
//            toSave = toSave|p1.type_m;
//            if(!fillRest){
//                toSave = toSave<<1;
//                toSave = toSave|p2.team_m.getNum();
//                toSave = toSave<<4;
//                toSave = toSave|p2.y_m;
//                toSave = toSave<<4;
//                toSave = toSave|p2.x_m;
//                toSave = toSave<<3;
//                toSave = toSave|p2.type_m;
//            }else{
//                toSave = toSave<<12;
//                toSave = toSave|4095;
//            }
//
//            byte[] bytes = ByteBuffer.allocate(4).putInt(toSave).array();
//            byte[] toReturn = new byte[3];
//            for (int j = 0; j < toReturn.length; j++) {
//                toReturn[j] = bytes[j+1];
//            }
//            byte[] tmp = new byte[final1.length+3];
//            for (int j = 0; j < final1.length; j++) {
//                tmp[j] = final1[j];
//            }
//            for (int j = 0; j < 3; j++) {
//                tmp[tmp.length-3+j] = toReturn[j];
//            }
//            final1 = tmp;
//        }
//        return final1;
//    }

    //TU KOŃCZY SIĘ METODA
    Piece[] piecesToArray(Piece[][] p1){
        int countPieces = 0;
        for (int i = 1; i < p1.length; i++) {
            for (int j = 1; j < p1[i].length; j++) {
                if(p1[i][j] != null){
                    countPieces++;
                }
            }
        }
        Piece[] pArray = new Piece[countPieces+Gameboard.zbitePionki.size()];
        int lastIndex = 0;
        for (int i = 1; i < p1.length; i++) {
            for (int j = 1; j < p1[i].length; j++) {
                if(p1[i][j]!=null) {
                    pArray[lastIndex++] = p1[i][j];
                }
            }
        }
        for (Piece piece:Gameboard.zbitePionki) {
            pArray[lastIndex++] = piece;
        }

        return pArray;
    }
}
