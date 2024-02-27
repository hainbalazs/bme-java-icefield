package IceField.model.Fields;

/**
 * DeployedTent osztály. Ez egy sátor, olyan építmény, ami megvédi a karaktereket a hóvihartól, de egy kör
 * @author sch413
 */
public class DeployedTent implements Building {
    /**
     * Default konstruktor
     */
    public DeployedTent(){}

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
        return false;
    }
    /**
     * Visszaadja, hogy az épület eltűnik-e a kör végén.
     * @return
     */
    public boolean Destroy () {
        return true;
    }
    /**
     * Viszzad egy stringben egy riportot az adott épületről. Teszteléshez, meg grafikus megjelenítéshez használatos.
     */
    public String show(){
        return "<tent>";
    }
}
