import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.BitSet;

public class SaveManager {
    //TODO ALL
    String path_m;
    SaveManager(String path){
        path_m = path;
    }

    void saveGame(Piece[][] pieces){
        piecesToByteArray(piecesToArray(pieces));

    }
    private byte[] piecesToByteArray(Piece[] arr){
        int toSave = 0;
        Piece p1 = null;
        Piece p2 = null;
        boolean fillRest = false;
        for (int i = 0; i < arr.length; i++) {
            p1 = arr[i];
            if(i+1 < arr.length){
                p2 = arr[i+1];
            }else{
                fillRest = true;
            }

            toSave = p1.team_m.ordinal();
            toSave = toSave<<4;
            toSave = toSave|p1.y_m;
            toSave = toSave<<4;
            toSave = toSave|p1.x_m;
            toSave = toSave<<3;
            toSave = toSave|p1.type_m;
            if(!fillRest){
                toSave = toSave<<1;
                toSave = toSave|p2.team_m.ordinal();
                toSave = toSave<<4;
                toSave = toSave|p2.y_m;
                toSave = toSave<<4;
                toSave = toSave|p2.x_m;
                toSave = toSave<<3;
                toSave = toSave|p2.type_m;
            }else{
                toSave = toSave<<12;
            }

        }
        System.out.println(Integer.toBinaryString(toSave));
        byte[] tab = new byte[(arr.length%2==0?arr.length: arr.length+1)];
        byte[] bytes = ByteBuffer.allocate(4).putInt(toSave).array();
        return new byte[0];
    }
//    void savePieces(Piece[] p1) throws IOException {
//        File plik = new File(path_m);
//        FileOutputStream fos = new FileOutputStream(plik);
//        for (int i = 0; i < p1.length; i++) {
//
//            //saveBytes(createArrayOfBytes(p1[i]));
//        }
//        fos.close();
//    }
//    //byte[] createArrayOfBytes(Piece p1){}

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
        for (Piece piece:Gameboard.zbitePionki
             ) {
            pArray[lastIndex++] = piece;
        }

        return pArray;
    }
}
