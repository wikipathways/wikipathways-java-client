package org.wikipathways.client; // Add this line

import java.net.URL;

public class Main {
    public static void main(String[] args) {
        try {
            // Specify the URL of the WikiPathways web service
            URL url = new URL("https://webservice.wikipathways.org/");
            
            // Create a client to interact with the WikiPathways API
            WikiPathwaysClient client = new WikiPathwaysClient(url);
            
            // Call the listOrganisms method
            String[] organisms = client.listOrganisms();
            
            // Print the list of organisms
            System.out.println("Available organisms in WikiPathways:");
            for (String organism : organisms) {
                System.out.println(organism);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
