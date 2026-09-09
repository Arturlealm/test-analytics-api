package tech.magicbook.analytics.api.util;

import java.util.UUID;
import com.github.f4b6a3.uuid.UuidCreator;

public final class UuidGenerator {
    
    private UuidGenerator(){

    }

    public static UUID generateV7(){
        return UuidCreator.getTimeOrderedEpoch();
    }
}
