/*
 *  Author: Carson Morris
 *  Date of Creation: January 30, 2024
 *  Sasquatch Weather - A personal project for using a weather API
 */

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import io.github.cdimascio.dotenv.Dotenv;

public class SasquatchWeather {
    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure().filename("env").load();

        private static final String API_KEY = dotenv.get("API_KEY");
        private static final String CITY_NAME = dotenv.get("CITY_NAME");

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

            UIManager.put("control", new Color(45, 45, 45));
            UIManager.put("info", new Color(45, 45, 45));
            UIManager.put("nimbusBase", new Color(51, 51, 51));
            UIManager.put("nimbusFocus", new Color(115, 164, 209));
            UIManager.put("nimbusLightBackground", new Color(35, 35, 35));
            UIManager.put("nimbusSelectionBackground", new Color(115, 164, 209));
            UIManager.put("text", new Color(200, 200, 200));

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Sasquatch Weather");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Create a panel to hold weather information
        JPanel weatherPanel = new JPanel();
        JLabel weatherLabel = new JLabel(getWeatherInformation());
        weatherLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        weatherPanel.add(weatherLabel);

        frame.getContentPane().add(weatherPanel);
        frame.setVisible(true);
        System.out.println(API_KEY);
        System.out.println(CITY_NAME);
    }

    private static String getWeatherInformation() {
        try {
            // Make API request to OpenWeatherMap
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + CITY_NAME + "&appid=" + API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            // Parse the JSON response and extract relevant weather information

            String temperature = "Temperature: " + response.toString();
            String description = "Description: " + response.toString();

            return temperature + " | " + description;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error fetching weather information";
        }
    }
}
