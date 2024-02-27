package IceField.view;

import javax.swing.*;
import java.awt.*;

/**
 * Ez az osztály a(z adott) medve jelenlétét reprezentálja mezőkön.
 */
public class BearView extends CharacterView {
    private JPanel currentP;

    /**
     * az osztály alapértelmezett konstruktora
     * fekete színű karakterként fog megjelenni a mezőn
     */
    public BearView() {
        super(Color.BLACK);
    }

    /**
     * absztrakt metódus,
     * kirajzolja fekete színnel, a jobb alsó pozícióra
     * a karakter jelenlétét reprezentáló kört a mezőre.
     * @param fieldP erre a mezőt befoglaló panelre rajzolódik ki a kör
     */
    @Override
    public void drawDot(JPanel fieldP) {
        currentP = fieldP;
    }

    /**
     * Kirajzolódik a teljes nézet. A nézet jelenesetben csak a mezőn lévő körből áll,
     * ezért hatása egyenértékű a drawDot() metódus hívásával.
     */
    @Override
    public void draw() {
    }

    /**
     * Ez a metódus akkor hívódik meg ha a modellben változik a karakter testhője,
     * a medve nem reagál rá, üres metódus.
     * @param nv erre az értékre változott a modellben a karakter értéke
     */
    @Override
    public void hpChanged(int nv) {

    }
}
