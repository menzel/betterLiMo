package de.thm.spring.model;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Loads and serves the list of lichen for the gui from the file ARTENLISE.INI
 */
public class Lichen {
    private File initfile;

    private Map<String, List<String>> lichen;
    private static Lichen instance;

    /**
     * Returns a map of super-group as key and the species names as a list of Strings
     *
     * @return
     */
    public Map<String, List<String>> getLichen() {
        return lichen;
    }

    public static Lichen getInstance(){
        if(instance == null)
            instance = new Lichen();
        return instance;
    }

    private Lichen(){
        lichen = new HashMap<>();

        if(System.getenv("HOME").contains("menzel")){
            initfile = new File("ARTENLISTE.INI");
        } else{
            initfile = new File("/home/mmnz21/ARTENLISTE.INI");
        }

        init(initfile);
    }

    private void init(File initfile) {
        try {
            Wini ini  = new Wini(initfile);

            Set<String> sections = ini.keySet();

            for(String section: sections){

                if(!ini.get(section).containsKey("Anzahl"))
                    continue; // jump over "programm"

                int anzahl = Integer.parseInt(ini.get(section, "Anzahl"));
                List<String> art = new ArrayList<>();


                for(int i = 1; i <= anzahl; i++){
                    art.add(ini.get(section, Integer.toString(i)));
                }

                lichen.put(section, art);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
