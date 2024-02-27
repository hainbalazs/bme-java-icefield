package IceField.model.Fields;

import IceField.model.Characters.Character;
import IceField.model.Game;
import IceField.model.Tools.Tool;

import java.util.logging.Logger;

/**
 * DangerousField osztály. Ez a osztály reprezentálja a jégmezőt alkotó veszélyes mezőket, azaz az olyan mezőket, amikről be lehet esni a vízbe
 * @author sch413
 */
public class DangerousField extends Field {

    /**
     * A mező kapatiását tároló adattag.
     */
    private int capacity;

    /**
     * Az instabil mező konstruktora.
     */
    public DangerousField(int id, int capacity, int snowlevel, boolean explored, Building building, Tool tool) {
        super(id, capacity, snowlevel, explored, building, tool);
        this.capacity = capacity;
    }

    /**
     * Hozzáfűzi a karakterek listájához a paraméterül kapott karaktert. Akkor használatos, amikor egy karakter erre a
     * mezőre lép, vagy idehúzzák kötéllel. Ezen felül, ha úgy lép ide egy karakter, hogy ezzel túllépik a mező
     * teherbírását, akkor az összes karaktert a vízbe borítja, aki itt áll.
     */
    public void addCharacter(Character c) {
        this.getCharacters().add(c);
        if (getCharacters().size() > capacity)
            for (Character chr:getCharacters()) {
                chr.fallenin();
            }
    }

    /**
     * visszaadja a mező kapacitását
     *      - pozitív szám: ez egy instabil mező, annyi embert bír el mint ez a szám(a visszatérési érték)
     *      - 0: a mezőn lyuk van, vagyis a kapacitása 0
     *      - -1: ez egy stabil mező, vagyis bárhányan ráálhatnak
     *      - -2: ennek a mezőnek a kapacitása még nem ismert a játékosok számára
     */
    public int getCapacity() {
        if (exploredCapacity) {
            return capacity;
        }
        return -2;
    }
}