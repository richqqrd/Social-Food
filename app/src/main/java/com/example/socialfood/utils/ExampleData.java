package com.example.socialfood.utils;

import com.example.socialfood.R;
import com.example.socialfood.controller.Post.PostController;
import com.example.socialfood.controller.User.UserController;
import com.example.socialfood.model.entities.User;

public class ExampleData {

    public static void populateDatabase(PostController postController,
            UserController userController) {


        // Test User 1 (Main)
        User user1 = new User();
        user1.setUsername("TestUser1");
        user1.setPassword("test");
        user1.setProfilImage("default.png");
        user1.setBio("Ich bin der erste Testbenutzer");
        user1.setFollowersCount(2);
        user1.setPostsCount(2);
        int user1id = userController.createUser(user1);
        user1.setUid(user1id);

        // Test User 2
        User user2 = new User();
        user2.setUsername("TestUser2");
        user2.setPassword("test");
        user2.setProfilImage("default.png");
        user2.setBio("Ich bin der zweite Testbenutzer");
        user2.setFollowersCount(1);
        user2.setPostsCount(2);
        int user2id = userController.createUser(user2);
        user2.setUid(user2id);

        // Test User 3
        User user3 = new User();
        user3.setUsername("TestUser3");
        user3.setPassword("test");
        user3.setProfilImage("default.png");
        user3.setBio("Ich bin der dritte Testbenutzer");
        user3.setFollowersCount(1);
        user3.setPostsCount(2);
        int user3id = userController.createUser(user3);
        user3.setUid(user3id);

        try {
            Thread.sleep(100); // Pause zwischen Posts
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Posts für User 1
        userController.setCurrentUser(user1);
        createTestPost(postController, "Leckere Pizza",
                "Pizza Rezept:\n1. Teig ausrollen\n2. Belegen\n3. Backen",
                "- Mehl\n- Hefe\n- Tomaten\n- Käse", 52.4891, 13.5221);
        createTestPost(postController, "Frischer Salat",
                "Salat Rezept:\n1. Waschen\n2. Schneiden\n3. Anrichten",
                "- Salat\n- Tomaten\n- Gurke", 52.4892, 13.5222);

        // Posts für User 2
        userController.setCurrentUser(user2);
        createTestPost(postController, "Pasta Carbonara",
                "Carbonara Rezept:\n1. Nudeln kochen\n2. Sauce machen\n3. Mischen",
                "- Spaghetti\n- Eier\n- Speck", 52.4893, 13.5223);
        createTestPost(postController, "Burger",
                "Burger Rezept:\n1. Patty formen\n2. Braten\n3. Zusammenbauen",
                "- Hackfleisch\n- Brötchen\n- Salat", 52.4894, 13.5224);

        // Posts für User 3
        userController.setCurrentUser(user3);
        createTestPost(postController, "Smoothie Bowl",
                "Bowl Rezept:\n1. Früchte mixen\n2. Toppings\n3. Dekorieren",
                "- Banane\n- Beeren\n- Joghurt", 52.4895, 13.5225);
        createTestPost(postController, "Sushi",
                "Sushi Rezept:\n1. Reis kochen\n2. Rollen\n3. Schneiden",
                "- Sushi Reis\n- Nori\n- Lachs", 52.4896, 13.5226);

        userController.setCurrentUser(user1);// Reset auf User 1

        userController.setCurrentUser(user1);
        postController.onCommentPost(1, "Sieht super lecker aus!");
        postController.onCommentPost(2, "Das muss ich auch mal probieren");

        userController.setCurrentUser(user2);
        postController.onCommentPost(1, "Tolles Rezept, danke fürs Teilen!");
        postController.onCommentPost(3, "Perfekt für den Sommer");

        userController.setCurrentUser(user3);
        postController.onCommentPost(2, "Sehr gesund!");
        postController.onCommentPost(4, "Klassiker!");

        // Likes hinzufügen
        userController.setCurrentUser(user1);
        postController.onLikePost(3); // User1 liked Post von User2
        postController.onLikePost(5); // User1 liked Post von User3

        userController.setCurrentUser(user2);
        postController.onLikePost(1); // User2 liked Post von User1
        postController.onLikePost(6); // User2 liked Post von User3

        userController.setCurrentUser(user3);
        postController.onLikePost(2); // User3 liked Post von User1
        postController.onLikePost(4); // User3 liked Post von User2

        // Zurück zu User1
        userController.setCurrentUser(user1);
    }

    private static void createTestPost(PostController postController,
            String description, String recipe,
            String ingredients, double lat, double lon) {
        String photoPath = String.valueOf(R.drawable.food);
        postController.createPost(photoPath, description, recipe, ingredients, lat, lon);
        try {
            Thread.sleep(100); // Pause zwischen Posts
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
