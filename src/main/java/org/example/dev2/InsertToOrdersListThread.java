package org.example.dev2;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

import static org.example.dev2.DBConnexion.LOGGER;

public class InsertToOrdersListThread extends Thread {

    private final DBConnexion db;
    private final Object mutex;
    private final List<Order> orders;

    public InsertToOrdersListThread(Object mutex, List<Order> orders) {
        db = new DBConnexion();  // Assurez-vous que DBConnexion est correctement initialisée
        this.mutex = mutex;
        this.orders = orders;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (mutex) {
                try {
                    FileReader fileReader = new FileReader("data/input.json");
                    JsonArray jsonArray = JsonParser.parseReader(fileReader).getAsJsonArray();
                    fileReader.close();

                    JsonWriter jsonWriter = new JsonWriter(new FileWriter("data/output.json"));
                    JsonWriter jsonWriterError = new JsonWriter(new FileWriter("data/error.json"));

                    jsonWriter.beginArray();
                    jsonWriter.setIndent("  ");
                    jsonWriterError.beginArray();
                    jsonWriterError.setIndent("  ");

                    for (JsonElement element : jsonArray) {
                        JsonObject jsonObject = element.getAsJsonObject();
                        Order order = fromJsonToOrder(jsonObject);
                        int orderId = order.getId();
                        java.sql.Date sqlDate = new java.sql.Date(order.getDate().getTime()); // Convertit correctement le Date en java.sql.Date
                        double amount = order.getAmount();
                        int customerId = jsonObject.get("customerId").getAsInt();
                        String status = order.getStatus();

                        if (verifyExistenceInDb(jsonObject)) {
                            orders.add(order);
                            db.insertToOrders(orderId, sqlDate, amount, customerId, status);

                            jsonWriter.beginObject();
                            jsonWriter.name("id").value(orderId);
                            jsonWriter.name("date").value(sqlDate.toString());
                            jsonWriter.name("amount").value(amount);
                            jsonWriter.name("customerId").value(customerId);
                            jsonWriter.name("status").value(status);
                            jsonWriter.endObject();

                        } else {
                            jsonWriterError.beginObject();
                            jsonWriterError.name("id").value(orderId);
                            jsonWriterError.name("date").value(sqlDate.toString());
                            jsonWriterError.name("amount").value(amount);
                            jsonWriterError.name("customerId").value(customerId);
                            jsonWriterError.name("status").value(status);
                            jsonWriterError.endObject();
                        }
                    }

                    jsonWriter.endArray();
                    jsonWriter.close();
                    jsonWriterError.endArray();
                    jsonWriterError.close();

                    // Vider le fichier input.json après avoir traité chaque objet
                    try (FileWriter fw = new FileWriter("data/input.json", false)) {
                        fw.write(""); // Écrire un contenu vide pour vider le fichier
                        LOGGER.info("Fichier input.json vidé.");
                    } catch (IOException e) {
                        LOGGER.severe("Erreur lors de la suppression du contenu de input.json : " + e.getMessage());
                    }

                    mutex.notify();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(60 * 60 * 1000); // Sleep for 1 hour
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean verifyExistenceInDb(JsonObject jsonObject) {
        int customerId = jsonObject.get("customerId").getAsInt();
        return db.selectFromDb(customerId);
    }

    private Order fromJsonToOrder(JsonObject jsonObject) {
        Gson gson = new Gson();
        return gson.fromJson(jsonObject, Order.class);
    }
}
