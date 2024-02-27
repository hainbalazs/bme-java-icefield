package IceField.controller;
/**
 * Ez az osztály felel a projekt naplózásáért. Képes a metódusok kilépési és belépési pontjait
 * feljegyezni, továbbá a user inputok előtti kérdéseket is kezeli
 *
 * Használata:
 * A main függvényben fontos meghívni a setup() statikus függvényt.
 *
 * A metódus első sorára a következő parancs elhelyezése:
 *loggr.info(">>");
 * A metódus utolsó sorára (vagy a visszatérés előtti utolsó sorára) a következő parancs:
 * loggr.info("<<");
 *
 * Megjegyzés: A helyes működés érdekében fontos, hogy return után ne történjen metódus
 * meghívása, ott csak valamilyen objektummal térhetünk vissza csak.
 *
 * User input kérése során:
 * SchLogger.opt(new String[]{"kérdés", "opció1", "opció2", ..., "opcióN"});
 * Ha csak igaz-hamis kérdés áll fenn, akkor csak a kérdést kell megadni.
 */

import java.util.logging.*;

public class SchLogger {
    static private ConsoleHandler ch;

    /**
     * Ez a metódus inicializálja a main-ben létrehozott loggert
     * A reset() metódus kitörli a az alapértelmezett beállításokat
     * Utána létrehozzuk sch413logger néven a loggerünket
     * Ha egy adott névvel csinálunk egy loggert, akkor ha bárhol máshol az ugyanolyan nevű loggerek is ugyanarra
     * a logger objektumra fognak referálni
     * Megadjuk, hogy minden szintű üzenet kiírásra kerüljön (de valójában erre nem lesz szükség)
     * Majd beállítjuk az általunk létrehozott Formattert a Logger Formatterjének
     * A Formatter adja meg a logok formátumát (milyen inormációk hogyan kerüljenek kiírásra)
     */
    static public void setup() {
        LogManager.getLogManager().reset();
        Logger logger = Logger.getLogger("sch413logger");
        logger.setLevel(Level.ALL);
        ch = new ConsoleHandler();
        ch.setFormatter(new CsFormatter());
        logger.addHandler(ch);
    }

    /**
     * Ez a metódus felel a user inputok előtti kérdések kiíratásáért
     * Beállítjuk először az ilyen célra létrehozott Formattert, majd neki továbbadjuk a stringtömböt
     * Utána, hogy ezzel ne a user-nek kelljen törödni vele, visszaállítjuk az alapértelmezett CsFormatter-t
     * a Loggerünk Formatterének
     * @param str Egy Stringtömb, amiben az első string a kérdés, a többi a válaszok
     */
    static public void opt(String[] str)
    {
        ch.setFormatter(new OptFormatter());
        Logger logger = Logger.getLogger("sch413logger");
        OptFormatter.opt(str);
        logger.info("");
        ch.setFormatter(new CsFormatter());
    }
}
