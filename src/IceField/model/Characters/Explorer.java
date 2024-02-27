package IceField.model.Characters;

import IceField.model.Fields.Field;
import IceField.model.Game;


/**
 * Ez az osztály reprezentálja a játékban a sarkkutatókat, akik FriendlyCharacterek.
 * Képességük a talajkutatás, amit csak a saját vagy a vele szomszédos mezőkön tud végrehajtani.
 * Maximális élete 4.
 */
public class Explorer extends FriendlyCharacter {

    /**
     * Alapértelmezett konstruktor
     * 4 maxhp-val
     */
    public Explorer(int id_, Game g_) {
        super(id_, 4, g_);
    }
    public Explorer(int id_, Game g_, int currhp) {super(id_, g_, 4, currhp);}

    /**
     * Adott View-tól függően megjeleníti az adott karakterről a tudnivalókat. Absztrakt függvény.
     * Gui esetén kirajzolja magát
     * Promptos indításnál kiírja a tulajdonságait <char_id><field_id><in_water><health><actionpoints><tools [..]><character_type> formátumban
     */
    @Override
    public String show() {
        StringBuilder sTools = new StringBuilder();
        int cnt = 0;
        for(int i = 0; i < ktools.size(); i++, cnt++){
            sTools.append("{").append(cnt).append(",").append(ktools.get(i).show()).append("}");
            if(!(i == ktools.size() - 1))
                sTools.append(",");
        }
        cnt = 3;
        if(hasDivingGear()){
            sTools.append("{").append(cnt).append(",").append(gear.show()).append("}");
        }
        cnt = 4;
        for (int j = 0; j < stools.size(); j++, cnt++){
            sTools.append("{").append(cnt).append(",").append(stools.get(j).show()).append("}");
            if(!(j == stools.size() - 1))
                sTools.append(",");
        }
        return "<"+ id +">"+"<"+ standingOn.getID()+">"+"<"+ health +">"+"<"+ actionPoint+">"+"<"+sTools+">"+"<explorer>";
    }

    /**
     * A sarkkutató felkutatja a saját vagy vele szomszédos mezőnek a kapacitását.
     * Ezután a getCapacity() hívás a már felkutatott mezőn, visszatudja adni az ismert értéket.
     * @param f ezen a Field-en használjuk a képességet; muszáj az a mező legyen ahol a Karakter jelenleg áll, vagy annak a szomszédja
     */
    public void useAbility(Field f) {
        boolean validField = f.equals(getField());
        for (Field n : f.getNeighbour()) {
            if(f.equals(n)) {
                validField = true;
                break;
            }
        }

        if(!validField){
            gRef.showError("The explorer can only investigate their own or neighbouring fields.");
            return;
        }

        f.exploredField();
        actionPoint--;
        if (actionPoint == 0)
            gRef.endTurn();

    }

}