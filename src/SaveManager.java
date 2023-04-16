import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveManager {
    //TODO ALL
    String path_m;
    SaveManager(String path){
        path_m = path;
    }

    void saveGame(Piece[][] pieces){
        //savePieces(piecesToArray(pieces)); TODO
    }
    void savePieces(Piece[] p1) throws IOException {
        File plik = new File(path_m);
        FileOutputStream fos = new FileOutputStream(plik);
        for (int i = 0; i < p1.length; i++) {

            //saveBytes(createArrayOfBytes(p1[i]));
        }
        fos.close();
    }
    //byte[] createArrayOfBytes(Piece p1){}

    Piece[] piecesToArray(Piece[][] p1){
        int countPieces = 0;
        for (int i = 0; i < p1.length; i++) {
            for (int j = 0; j < p1[i].length; j++) {
                if(p1[i][j] != null){
                    countPieces++;
                }
            }
        }
        Piece[] pArray = new Piece[countPieces];
        int lastIndex = 0;
        for (int i = 0; i < p1.length; i++) {
            for (int j = 0; j < p1[i].length; j++) {
                pArray[lastIndex++] = p1[i][j];
            }
        }
        return pArray;
    }
}
