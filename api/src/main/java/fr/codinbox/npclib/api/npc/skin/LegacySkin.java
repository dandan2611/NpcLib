package fr.codinbox.npclib.api.npc.skin;

public class LegacySkin implements Skin {

    private final String value;
    private final String signature;

    protected LegacySkin(String value, String signature) {
        this.value = value;
        this.signature = signature;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getSignature() {
        return this.signature;
    }

}
