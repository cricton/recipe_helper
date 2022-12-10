package TypeDefs;

public class Ingredient {

    public enum Unit {g, ml, pieces}

    private String name;
    private Unit unit;
    public Ingredient(String name){
        this.name = name;
    };

    @Override
    public String toString() {
        return name+" " +unit;
    }

    public Ingredient(String name, Unit unit){
        this.name = name;
        this.unit = unit;
    };
}
