package com.ocelotslovebirds.birdhaus.datagen;

import java.util.Locale;

import com.ocelotslovebirds.birdhaus.Core;
import com.ocelotslovebirds.birdhaus.setup.ModSetup;
import com.ocelotslovebirds.birdhaus.setup.Registration;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class LanguageProviderGen extends LanguageProvider {

    private final String locale;

    /**
     * @param gen    The data generator, should be passed from the calling main DataGenerator class.
     * @param locale The language code. Look up a language code on the minecraft wiki. It should follow the "en_us"
     *               format where the first two letters are the language, followed by an underscore, followed by the
     *               region. If the language and region are the same, it should repeat, i.e. French France
     */
    public LanguageProviderGen(DataGenerator gen, String locale) {
        super(gen, Core.MODID, locale);
        this.locale = locale;
    }

    /** Overloaded constructor that gets the language from the system if none is provided.
     *
     * @param gen The data generator, should be passed from the calling main DataGenerator class.
     */
    public LanguageProviderGen(DataGenerator gen) {
        super(gen, Core.MODID, Locale.getDefault().toString().toLowerCase());
        this.locale = Locale.getDefault().toString().toLowerCase();
    }

    /**
     * The addTranslations() allows us to be friendly to multiple languages. To add another language, add the language
     * code to the switch and a function with the same registration code.
     */
    @Override
    protected void addTranslations() {
        switch (locale) {
        case "fr_ca", "fr_fr" -> addFrenchTranslations();
        default -> addEnglishTranslations();
        }
    }

    /**
     * Adds the English translation.
     */
    protected void addEnglishTranslations() {
        add("itemGroup." + ModSetup.TAB_NAME, "Birdhaus");
        add(Registration.BIRDHOUSE_BLOCK.get(), "Birdhouse");
    }

    /**
     * Adds the French translation. Apologies for my poor French. :(
     */
    protected void addFrenchTranslations() {
        add("itemGroup." + ModSetup.TAB_NAME, "Haus de Oiseau");
        add(Registration.BIRDHOUSE_BLOCK.get(), "Nichoir");
    }
}
