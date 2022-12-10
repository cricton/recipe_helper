package TypeDefs;

import java.util.HashMap;

public class Recipe {

    enum Difficulty {LOW, MEDIUM, HIGH}

    private String name;
    private HashMap<Ingredient, Integer> ingredients;
    private int difficulty;
    private int prepTime;

    @Override
    public String toString() {
        return name + ingredients;
    }

    public Recipe(String name){
        this.name = name;
        ingredients = new HashMap<Ingredient, Integer>();
    }

    public Recipe(String name, int difficulty, int prepTime){
        this.name = name;
        this.difficulty = difficulty;
        this.prepTime = prepTime;
        ingredients = new HashMap<Ingredient, Integer>();
    }

    public void addIngredient(Ingredient ingredient, Integer amount){
        this.ingredients.put(ingredient, amount);
    }

    public Integer getIngredientAmount(Ingredient ingredient){
        return this.ingredients.get(ingredient);
    }

    public void setIngredientAmount(Ingredient ingredient, Integer amount){
        this.ingredients.put(ingredient, amount);
    }

    public String getName() {
        return name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }
}
