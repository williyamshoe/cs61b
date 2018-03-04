package byog.Core;

import byog.TileEngine.TETile;

import java.io.*;

public class SaveAndLoadStream {
    protected static void saveGameState(TETile[][] world) {
        File f = new File("./gameState.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(world);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    protected static TETile[][] loadGameState(FileInputStream fs) {
        try {
            ObjectInputStream os = new ObjectInputStream(fs);
            TETile[][] loadWorld = (TETile[][]) os.readObject();
            os.close();
            return loadWorld;
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        } catch (ClassNotFoundException e) {
            System.out.println("class not found");
            System.exit(0);
        }
        return null;
    }
}
