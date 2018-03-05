package byog.Core;

import byog.TileEngine.TETile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import java.util.Random;

public class SaveAndLoadStream {
    protected static void saveGameState(TETile[][] world) {
        Object[] details = new Object[] {HelperMethods.seed, HelperMethods.flagcount1,
            HelperMethods.flagcount2, HelperMethods.blockedleft1,
            HelperMethods.blockedleft2, HelperMethods.blockedtrig1,
            HelperMethods.blockedtrig2, HelperMethods.stunned1,
            HelperMethods.stunned2, HelperMethods.stunCountdown1,
            HelperMethods.stunCountdown2, HelperMethods.ran, world};
        File f = new File("./gameState.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(details);
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
            Object[] details = (Object[]) os.readObject();
            HelperMethods.seed = (long) details[0];
            HelperMethods.flagcount1 = (int) details[1];
            HelperMethods.flagcount2 = (int) details[2];
            HelperMethods.blockedleft1 = (int) details[3];
            HelperMethods.blockedleft2 = (int) details[4];
            HelperMethods.blockedtrig1 = (boolean) details[5];
            HelperMethods.blockedtrig2 = (boolean) details[6];
            HelperMethods.stunned1 = (boolean) details[7];
            HelperMethods.stunned2 = (boolean) details[8];
            HelperMethods.stunCountdown1 = (int) details[9];
            HelperMethods.stunCountdown2 = (int) details[10];
            HelperMethods.ran = (Random) details[11];
            TETile[][] loadWorld = (TETile[][]) details[12];
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
