package com.example.atlas;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.ConnectionString;

public class MyMongoDatabase {

    public static MongoClient mongoClient = MongoClients.create("mongodb+srv://deni:");
    public static com.mongodb.client.MongoDatabase database = mongoClient.getDatabase("MyApp");
    public static MongoCollection collectionUser = database.getCollection("User");
    public static MongoCollection collectionData = database.getCollection("Data");
    public static MongoCollection collectionSaves = database.getCollection("Custom Saves");

}
