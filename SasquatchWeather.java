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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SasquatchWeather {
    public static void main(String[] args) {
        final String API_KEY = System.getenv("WEATHER_API_KEY");
        final String CITY_NAME = System.getenv("CITY_NAME");

        // Set Nimbus look and feel with a dark theme
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

            UIManager.put("control", new Color(45, 45, 45));
            UIManager.put("info", new Color(45, 45, 45));
            UIManager.put("nimbusBase", new Color(51, 51, 51));
            UIManager.put("nimbusFocus", new Color(115, 164, 209));
            UIManager.put("nimbusLightBackground", new Color(35, 35, 35));
            UIManager.put("nimbusSelectionBackground", new Color(115, 164, 209));
            UIManager.put("text", new Color(200, 200, 200));

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Create the main frame
        JFrame frame = new JFrame("Sasquatch Weather");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Load the background image
        ImageIcon backgroundImage = createImageIcon("assets/background.jpg");

        // Create a layered pane to hold components in layers
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(frame.getSize());

        // Create a label to hold the background image
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setSize(800, 600); // Adjust the size to make the image smaller
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // Show a dialog to choose temperature unit
        TemperatureUnit unit = (TemperatureUnit) JOptionPane.showInputDialog(
                null,
                "Choose temperature unit:",
                "Temperature Unit",
                JOptionPane.QUESTION_MESSAGE,
                null,
                TemperatureUnit.values(),
                TemperatureUnit.CELSIUS
        );

        // Display weather information label
        JLabel weatherLabel = new JLabel(getWeatherInformation(CITY_NAME, API_KEY, unit));
        weatherLabel.setFont(new Font("Helvetica", Font.BOLD, 30));
        weatherLabel.setForeground(Color.WHITE);
        weatherLabel.setHorizontalAlignment(SwingConstants.CENTER);
        weatherLabel.setBounds(0, 50, frame.getWidth(), 30);
        layeredPane.add(weatherLabel, JLayeredPane.PALETTE_LAYER);

        // Set up a timer to periodically update weather information
        Timer timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update weather information and refresh the GUI
                weatherLabel.setText(getWeatherInformation(CITY_NAME, API_KEY, unit));
            }
        });
        timer.start();

        // Set up the frame content        
        frame.setContentPane(layeredPane);
        frame.setResizable(false);  // Disable resizing
        frame.pack();
        frame.setVisible(true);

    }

    private static ImageIcon createImageIcon(String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

            return String.format("%.1f", tempValue) + " " + unit.getSymbol() + " | " + capitalizeFirstLetter(description);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error fetching weather information";
        }
    }

    /**
     * This is just to make the returned description look better, may not be efficient
     */
    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char ch : str.toCharArray()) {
            if (Character.isWhitespace(ch)) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                ch = Character.toUpperCase(ch);
                capitalizeNext = false;
            }

            result.append(ch);
        }

        return result.toString();
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
