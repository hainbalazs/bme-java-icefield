package IceField.model.Fields;

/**
 * Building interface Interface Ez a mezőn lévő egy épületet szimbolizál, felelőssége, hogy tudassa a többi osztállyal, hogy az ő ottléte milyen befolyással van a játékra.
 * @author sch413
 */
public interface Building  {
    /**
     * Visszaadja, hogy az épület véd-e a hóvihartól.
     * @return
     */
    abstract boolean protectFromSnow();
    /**
     * Visszaadja, hogy az épület véd-e a medvétől.
     * @return
     */
    abstract boolean protectFromBear();
    /**
     * Visszaadja, hogy az épület eltűnik-e a kör végén.
     * @return
     */
    abstract boolean Destroy();
    /**
     * Viszzad egy stringben egy riportot az adott épületről. Teszteléshez, meg grafikus megjelenítéshez használatos.
     */
    abstract String show();
}
