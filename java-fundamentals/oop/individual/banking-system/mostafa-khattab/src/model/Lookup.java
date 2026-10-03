package model;

public class Lookup {
    private int id;
    private String categoryCode;
    private String itemCode;
    private String itemValue;
    private String itemName;
    private boolean isActive;

    public Lookup(int id, String categoryCode, String itemCode, String itemValue, String itemName, boolean isActive) {
        this.id = id;
        this.categoryCode = categoryCode;
        this.itemCode = itemCode;
        this.itemValue = itemValue;
        this.itemName = itemName;
        this.isActive = isActive;
    }

    public int getId() { return id; }
    public String getCategoryCode() { return categoryCode; }
    public String getItemCode() { return itemCode; }
    public String getItemValue() { return itemValue; }
    public String getItemName() { return itemName; }
    public boolean isActive() { return isActive; }

    @Override
    public String toString() {
        return String.format("Lookup[ID=%d, Category=%s, Code=%s, Value=%s, Name=%s, Active=%b]",
                id, categoryCode, itemCode, itemValue, itemName, isActive);
    }
}