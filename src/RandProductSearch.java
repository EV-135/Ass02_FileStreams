import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandProductSearch extends JFrame {

    private JTextField searchField;
    private JTextArea resultsArea;

    private final File dataFile =
            new File("ProductRandomAccess.txt");

    public RandProductSearch() {
        setTitle("Random Access Product Search");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel searchPanel = new JPanel(new FlowLayout());

        searchField = new JTextField(25);
        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");
        JButton quitButton = new JButton("Quit");

        searchPanel.add(new JLabel("Product Name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        searchPanel.add(quitButton);

        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane scrollPane =
                new JScrollPane(resultsArea);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> searchProducts());

        searchField.addActionListener(e -> searchProducts());

        clearButton.addActionListener(e -> {
            searchField.setText("");
            resultsArea.setText("");
            searchField.requestFocus();
        });

        quitButton.addActionListener(e -> System.exit(0));
    }

    private void searchProducts() {
        String searchText = searchField.getText().trim();

        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a product name to search.");
            return;
        }

        resultsArea.setText("");

        if (!dataFile.exists()) {
            resultsArea.setText("Product data file not found.");
            return;
        }

        int matches = 0;

        try (RandomAccessFile file =
                     new RandomAccessFile(dataFile, "r")) {

            long recordCount =
                    file.length() / Product.RECORD_SIZE;

            for (int i = 0; i < recordCount; i++) {

                Product product = Product.readFrom(file);

                // Case-insensitive partial name search.
                if (product.getName().toLowerCase()
                        .contains(searchText.toLowerCase())) {

                    resultsArea.append(
                            "Product ID: " + product.getID() + "\n" +
                                    "Name: " + product.getName() + "\n" +
                                    "Description: " + product.getDescription() + "\n" +
                                    "Cost: $" + String.format("%.2f",
                                    product.getCost()) + "\n" +
                                    "----------------------------------------\n"
                    );

                    matches++;
                }
            }

            if (matches == 0) {
                resultsArea.setText(
                        "No products matched your search.");
            } else {
                resultsArea.append(
                        "\nTotal matching products: " + matches);
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error reading product file: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RandProductSearch search = new RandProductSearch();
            search.setVisible(true);
        });
    }
}
