package com.royken.teknik.entities;

/**
 * Created by royken on 12/01/17.
 */
public class CahierDummy {

    public static String getCahierByCode(String code){
        if(code.equalsIgnoreCase("OTE"))
            return "Traitement de l'eau";
        if(code.equalsIgnoreCase("GPE"))
            return "Groupe Electrogene";
        if(code.equalsIgnoreCase("ELE"))
            return "Electricité";
        if(code.equalsIgnoreCase("CHA"))
            return "Chaud";
        if(code.equalsIgnoreCase("AIR"))
            return "AIR";
        if(code.equalsIgnoreCase("FRO"))
            return "Froid";
        if(code.equalsIgnoreCase("EAU"))
            return "EAU";
        if(code.equalsIgnoreCase("UAG"))
            return "Usine A Glace";
        if(code.equalsIgnoreCase("COM"))
            return "Compresseurs 40b";
        if(code.equalsIgnoreCase("COH"))
            return "Compresseur Haffmans";
        return "CO2";
    }
}
