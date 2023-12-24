package mealplanner;

import java.util.ArrayList;

public class MealPlan {

        @Override
        public String toString() {
            return String.format("""
                Category: %s
                Name: %s
                Ingredients:
                %s
                """, this.category, this.name, String.join("\n ", ingredients));
        }


    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    private String category;
    private String name;
    private ArrayList<String> ingredients;

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }
}
