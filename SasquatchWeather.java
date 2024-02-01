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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SasquatchWeather {
    public static void main(String[] args) {

        final String API_KEY = System.getenv("WEATHER_API_KEY");
        final String CITY_NAME = System.getenv("CITY_NAME");

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

        TemperatureUnit unit = (TemperatureUnit) JOptionPane.showInputDialog(
                null,
                "Choose temperature unit:",
                "Temperature Unit",
                JOptionPane.QUESTION_MESSAGE,
                null,
                TemperatureUnit.values(),
                TemperatureUnit.CELSIUS
        );

        JLabel weatherLabel = new JLabel(getWeatherInformation(CITY_NAME, API_KEY, unit));
        weatherLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        weatherPanel.add(weatherLabel);

        frame.getContentPane().add(weatherPanel);
        frame.setVisible(true);
        System.out.println(API_KEY);
        System.out.println(CITY_NAME);
    }

    private static String getWeatherInformation(String CITY_NAME, String API_KEY, TemperatureUnit unit) {
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

            // Manually extract relevant information from JSON string
            String jsonString = response.toString();
            String temperature = extractValue(jsonString, "\"temp\":", ",");
            String description = extractValue(jsonString, "\"description\":\"", "\"");

            // Convert temperature based on the selected unit
            double tempValue = Double.parseDouble(temperature);
            if (unit == TemperatureUnit.CELSIUS) {
                tempValue = kelvinToCelsius(tempValue);
            } else if (unit == TemperatureUnit.FAHRENHEIT) {
                tempValue = celsiusToFahrenheit(kelvinToCelsius(tempValue));
            }

            return "Temperature: " + String.format("%.2f", tempValue) + " " + unit.getSymbol() + " | Description: " + description;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error fetching weather information";
        }
    }

    private static String extractValue(String input, String startTag, String endTag) {
        Pattern pattern = Pattern.compile(Pattern.quote(startTag) + "(.*?)" + Pattern.quote(endTag));
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "N/A";
    }

    private static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    private static double celsiusToFahrenheit(double celsius) {
        return celsius * 9 / 5 + 32;
    }

    private enum TemperatureUnit {
        KELVIN("K"), CELSIUS("C"), FAHRENHEIT("F");

        private final String symbol;

        TemperatureUnit(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}
