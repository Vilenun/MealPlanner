package mealplanner;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MealPlaner {

    public void start() throws SQLException {
        String DB_URL = "jdbc:postgresql://localhost/meals_db";
        String USER = "postgres";
        String PASS = "1111";
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        connection.setAutoCommit(true);
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS meals (" +
                "category varchar(1024)," +
                "meal varchar(1024)," +
                "meal_id int primary key)");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS ingredients (" +
                "ingredient varchar(1024)," +
                "ingredient_id int primary key," +
                "meal_id integer references meals(meal_id) NOT NULL)");




        String input = "";
        Scanner sc = new Scanner(System.in);
        ResultSet rs0 = statement.executeQuery("select meal_id from meals");
        int point=0;
        int pointIng = 0;
        if(rs0.isBeforeFirst()){
            while(rs0.next()){
                point = rs0.getInt("meal_id");
            }
        }
        int showPoint=point;
        ResultSet rs = statement.executeQuery("select ingredient_id from ingredients");
        if(rs.isBeforeFirst()) {
            while (rs.next()) {
                pointIng = rs.getInt("ingredient_id");
            }
        }
        while (!input.equals("exit")){
            System.out.println("What would you like to do (add, show, plan, save, exit)?");
            input = sc.nextLine();
            switch (input){
                case "add":
                    MealPlan mealPlan = new MealPlan();
                    System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
                    mealPlan.setCategory(categoryBuilder(sc));
                    System.out.println("Input the meal's name:");
                    mealPlan.setName(nameBuilder(sc));
                    System.out.println("Input the ingredients:");
                    mealPlan.setIngredients(ingredientsBuilder(sc));
                    point++;
                    statement.executeUpdate(String.format("insert into meals  values ('%s', '%s', '%d')", mealPlan.getCategory(),mealPlan.getName(),point));
                    for (String ing: mealPlan.getIngredients()) {
                        pointIng++;
                        statement.executeUpdate(String.format("insert into ingredients  values ('%s', '%d', '%d')",ing,pointIng,point));
                    }
                    System.out.println("The meal has been added!");
                    showPoint++;
                    break;
                case "show":
                    ShowPlan.show(showPoint,statement,sc);
                    break;
                case "plan":
                    PlanThePlan.plan(statement,sc);
                    break;
                case "save":
                    SaveThePlan.save(statement,sc,connection);
                    break;
            }
        }
        System.out.println("Bye!");
    }

    static String categoryBuilder(Scanner sc){
        String category="";
        boolean check=false;
        while (!check){
            category = sc.nextLine();
                if (!(category.equals("breakfast")||category.equals("lunch")||category.equals("dinner"))){
                    System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
                } else {
                    check = true;
                }


        }

        return category;
    }


    static String nameBuilder(Scanner sc){
        String name="";
        boolean check=false;
        while (!check){
            name = sc.nextLine();
            if (name.matches("[a-zA-Z]+ [a-zA-Z]+|[a-zA-Z]+")){
               check = true;
            } else {
                System.out.println("Wrong format. Use letters only!");
            }

        }

        return name;
    }


    static ArrayList<String> ingredientsBuilder(Scanner sc){
        String ingredients = "";
        boolean check=false;
        ArrayList <String> list;
        list = new ArrayList<>();
        while (!check){
            ingredients = sc.nextLine();
            list = new ArrayList<>(Arrays.asList(ingredients.split(", ?")));
            for (String str:list) {
                if (!str.matches("[a-zA-Z]+ [a-zA-Z]+|[a-zA-Z]+")){
                    System.out.println("Wrong format. Use letters only!");
                    check = false;
                    break;
                }
                check = true;
            }
            if (ingredients.chars().filter(ch -> ch == ',').count() >= list.size()){
                System.out.println("Wrong format. Use letters only!");
                check = false;
            }
        }
        return list;
    }
}
