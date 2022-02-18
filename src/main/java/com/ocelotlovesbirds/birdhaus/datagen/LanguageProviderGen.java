package com.ocelotlovesbirds.birdhaus.datagen;

import com.ocelotlovesbirds.birdhaus.Core;
import com.ocelotlovesbirds.birdhaus.setup.ModSetup;
import com.ocelotlovesbirds.birdhaus.setup.Registration;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class LanguageProviderGen extends LanguageProvider {

    //TODO: Find a better way to retrieve language.
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

    /**
     * The addTranslations() allows us to be friendly to multiple languages. To add another language, add the language
     * code to the switch and a function with the same registration code.
     */
    @Override
    //TODO: Replace translation default case with a better error catch.
    protected void addTranslations() {
        switch (locale) {
        case "en_us" -> addEnglishTranslations();
        case "fr_ca", "fr_fr" -> addFrenchTranslations();
        default -> System.out.println("Language Error.");

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
