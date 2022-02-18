package com.ocelotlovesbirds.birdhaus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ocelotlovesbirds.birdhaus.setup.ClientSetup;
import com.ocelotlovesbirds.birdhaus.setup.ModSetup;
import com.ocelotlovesbirds.birdhaus.setup.Registration;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;



//Mod identity must match the meta-inf file.

/**
 * The core class is exactly that. It is the core of the mod that is responsible for calling registration and
 * subscribing to the mod event bus.
 * <p>
 * As a general practice, the core should be kept as clean as possible and contain calls to other package functions
 * to allow for highly modular, easy tracing code.
 */
@Mod("birdhaus")
public class Core {
    public static final String MODID = "birdhaus";
    //Event and Error Logging in an log4j logger.
    private static final Logger logger = LogManager.getLogger();

    /**
     * The central method called by minecraft. Equivalent to Main(), though main is particularly avoided here to avoid
     * confusion with the main minecraft process.
     */
    public Core() {
        //Initialize registration of all the mod components. Failure here will generate a failure in Minecraft's initial
        //checks
        Registration.init();

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(ModSetup::init);
        //Unsafe running is necessary at the moment due to the forge API's registration methods.
        //TODO: Investigate if safeRunWhenOn is possible in current version.
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(ClientSetup::init));
    }
}
