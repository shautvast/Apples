package nl.sander.reflective.compare;

import java.util.List;

@SuppressWarnings("unused") // used by generated code
public class PlumBean {
    private final String core;
    private final String peel;
    private final boolean juicy;
    private final int number;
    private final float price;
    private final Storage storage;
    private final byte cores;

    private final List<Shop> shops;

    public PlumBean(String core, String peel, boolean juicy, int number, float price, Storage storage, byte cores, List<Shop> shops) {
        this.core = core;
        this.peel = peel;
        this.juicy = juicy;
        this.number = number;
        this.price = price;
        this.storage = storage;
        this.cores = cores;
        this.shops = shops;
    }

    public String getCore() {
        return core;
    }

    public String getPeel() {
        return peel;
    }

    public boolean isJuicy() {
        return juicy;
    }

    public int getNumber() {
        return number;
    }

    public float getPrice() {
        return price;
    }

    public Storage getStorage() {
        return storage;
    }

    public byte getCores() {
        return cores;
    }

    public List<Shop> getShops() {
        return shops;
    }
}
