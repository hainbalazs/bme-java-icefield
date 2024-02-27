package IceField.model.Fields;

import java.util.*;
import java.util.logging.Logger;

/**
 * Iglu osztály. Ez egy iglu, olyan építmény, ami megvédi a karaktereket a hóvihartól és a medvétől is.
 * @author sch413
 */
public class Igloo implements Building {

    /**
     * Default konstruktor
     */
    public Igloo() {

    }
    /**
     * Visszaadja, hogy az épület véd-e a hóvihartól.
     * @return
     */
    public boolean protectFromSnow () {
        return true;
    }
    /**
     * Visszaadja, hogy az épület véd-e a medvétől.
     * @return
     */
    public boolean protectFromBear () {
        return true;
    }
    /**
     * Visszaadja, hogy az épület eltűnik-e a kör végén.
     * @return
     */
    public boolean Destroy () {
        return false;
    }
    /**
     * Viszzad egy stringben egy riportot az adott épületről. Teszteléshez, meg grafikus megjelenítéshez használatos.
     */
    public String show(){
        return "<igloo>";
    }



}