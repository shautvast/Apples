package nl.sander.apples;

public class Shop {
    private final String name;

    public Shop(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "name='" + name + '\'' +
                '}';
    }
}
