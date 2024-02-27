package IceField.model.Fields;

import IceField.model.Characters.Character;
import IceField.model.Fields.Field;
import IceField.model.Tools.Tool;

import java.util.logging.Logger;

/**
 * StableField osztály. Ez az osztály reprezentálja a jégmezőt alkotó stabil mezőket, azaz az olyan mezőket, amikről nem lehet beesni a vízbe.
 * @author sch413
 */
public class StableField extends Field {

    /**
     * konstruktor
     */
    public StableField(int id, int capacity, int snowlevel, boolean explored, Building building, Tool tool) {
        super(id, capacity, snowlevel, explored, building, tool);
    }

    /**
     * Hozzáfűzi a karakterek listájához a paraméterül kapott karaktert. Akkor használatos, amikor egy karakter erre a
     * mezőre lép, vagy idehúzzák kötéllel.
     */
    public void addCharacter(Character c) {
        this.getCharacters().add(c);
    }

    /**
     * visszaadja a mező kapacitását
     *      - pozitív szám: ez egy instabil mező, annyi embert bír el mint ez a szám(a visszatérési érték)
     *      - 0: a mezőn lyuk van, vagyis a kapacitása 0
     *      - -1: ez egy stabil mező, vagyis bárhányan ráállhatnak
     *      - -2: ennek a mezőnek a kapacitása még nem ismert a játékosok számára
     */
    public int getCapacity() {
        if (exploredCapacity) {
            return -1;
        }
        return -2;
    }


}