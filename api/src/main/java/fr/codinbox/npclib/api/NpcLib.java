package fr.codinbox.npclib.api;

public abstract class NpcLib {

    private static NpcLib API;

    public static NpcLib api() {
        if (API == null)
            throw new IllegalStateException("NpcLib is not initialized");
        return API;
    }

    public static void setApi(NpcLib api) {
        if (API != null)
            throw new IllegalStateException("NpcLib is already initialized");
        API = api;
    }

}
