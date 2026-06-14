import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class CurrencyConverterGUI {

    static String apiKey = "47f025ed692470ea65b8f70c"; // 🔑 your API key

    public static void main(String[] args) {

        JFrame frame = new JFrame("💱 Currency Converter");
        frame.setSize(500, 400);
        frame.setLayout(new GridLayout(7, 2, 10, 10));

        // 🎨 Background
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);

        JTextField amountField = new JTextField();

        String[] currencies = {
                "USD","INR","EUR","GBP","JPY","AUD","CAD","CHF","CNY","SGD"
        };

        JComboBox<String> fromBox = new JComboBox<>(currencies);
        JComboBox<String> toBox = new JComboBox<>(currencies);

        JButton convertBtn = new JButton("Convert");
        JButton swapBtn = new JButton("Swap");

        JLabel resultLabel = new JLabel("Result: ");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historyArea);

        // 🎨 Button style
        convertBtn.setBackground(Color.BLUE);
        convertBtn.setForeground(Color.WHITE);

        swapBtn.setBackground(Color.DARK_GRAY);
        swapBtn.setForeground(Color.WHITE);

        // 🧱 Layout
        frame.add(new JLabel("Amount:"));
        frame.add(amountField);

        frame.add(new JLabel("From:"));
        frame.add(fromBox);

        frame.add(new JLabel("To:"));
        frame.add(toBox);

        frame.add(swapBtn);
        frame.add(convertBtn);

        frame.add(resultLabel);
        frame.add(new JLabel("")); // empty space

        frame.add(new JLabel("History:"));
        frame.add(scrollPane);

        // 🔄 Swap logic
        swapBtn.addActionListener(e -> {
            String temp = (String) fromBox.getSelectedItem();
            fromBox.setSelectedItem(toBox.getSelectedItem());
            toBox.setSelectedItem(temp);
        });

        // 💱 Convert logic
        convertBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String from = fromBox.getSelectedItem().toString();
                String to = toBox.getSelectedItem().toString();

                String urlStr = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + from;

                URL url = new URL(urlStr);
                HttpURLConnection request = (HttpURLConnection) url.openConnection();
                request.connect();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(request.getInputStream())
                );

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                JSONObject json = new JSONObject(response.toString());
                double rate = json.getJSONObject("conversion_rates").getDouble(to);

                double result = amount * rate;

                // 📊 Show result + rate
                resultLabel.setText("1 " + from + " = " + rate + " " + to +
                        " | Result: " + result);

                // 📜 Add to history
                historyArea.append(amount + " " + from + " → " + result + " " + to + "\n");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "❌ Invalid input or API error!");
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}