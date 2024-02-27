package IceField.view;

import javax.swing.*;
import java.awt.*;

/**
 * Ez az absztrakt osztály reprezentálja a mezőkön lépkedhető karakterek nézetét.
 * Feladata, hogy megjelenítse az adott karakter jelenlétét a mezőkön (ezen implementációban: a megfelelő karikákkal).
 */
public abstract class CharacterView  implements Drawable{

    protected Color fColor;

    /**
     * az osztály alapértelmezett konstruktora
     * @param fC ilyen színű karakterként fog megjelenni a mezőn
     */
    public CharacterView(Color fC){
        fColor = fC;
    }

    /**
     * absztrakt metódus,
     * kirajzolja a megfelelő színnel, a megfelelő pozícióra
     * a karakter jelenlétét reprezentáló kört a mezőre.
     * @param fieldP erre a mezőt befoglaló panelre rajzolódik ki a kör
     */
    public abstract void drawDot(JPanel fieldP);

    /**
     * Kirajzolódik a teljes nézet. Absztrakt metódus.
     */
    public abstract void draw();

    /**
     * Ez a metódus akkor hívódik meg ha a modellben változik a karakter testhője,
     * absztrakt metódus, mivel nem minden karakternek kell rá reagálni.
     * @param nv erre az értékre változott a modellben a karakter értéke
     */
    public abstract void hpChanged(int nv);

}
