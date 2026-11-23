import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandProductMaker extends JFrame {

    private JTextField idField;
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField costField;
    private JTextField countField;

    private final File dataFile =
            new File("ProductRandomAccess.txt");

    public RandProductMaker() {
        setTitle("Random Access Product Maker");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        idField = new JTextField(20);
        nameField = new JTextField(20);
        descriptionField = new JTextField(20);
        costField = new JTextField(20);
        countField = new JTextField(5);

        countField.setEditable(false);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(
                20, 20, 20, 20));

        panel.add(new JLabel("Product ID (6 characters):"));
        panel.add(idField);

        panel.add(new JLabel("Product Name (35 max):"));
        panel.add(nameField);

        panel.add(new JLabel("Description (75 max):"));
        panel.add(descriptionField);

        panel.add(new JLabel("Cost:"));
        panel.add(costField);

        panel.add(new JLabel("Records Entered:"));
        panel.add(countField);

        JButton addButton = new JButton("Add Product");
        JButton quitButton = new JButton("Quit");

        panel.add(addButton);
        panel.add(quitButton);

        add(panel);

        updateRecordCount();

        addButton.addActionListener(e -> addProduct());
        quitButton.addActionListener(e -> System.exit(0));
    }

    private void addProduct() {
        String ID = idField.getText().trim();
        String name = nameField.getText().trim();
        String description = descriptionField.getText().trim();
        String costText = costField.getText().trim();

        if (ID.isEmpty() || name.isEmpty() ||
                description.isEmpty() || costText.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please complete all fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (ID.length() != Product.ID_LENGTH) {
            JOptionPane.showMessageDialog(this,
                    "ID must be exactly 6 characters.");
            return;
        }

        if (name.length() > Product.NAME_LENGTH) {
            JOptionPane.showMessageDialog(this,
                    "Name cannot exceed 35 characters.");
            return;
        }

        if (description.length() > Product.DESCRIPTION_LENGTH) {
            JOptionPane.showMessageDialog(this,
                    "Description cannot exceed 75 characters.");
            return;
        }

        double cost;

        try {
            cost = Double.parseDouble(costText);

            if (!Double.isFinite(cost) || cost < 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid non-negative cost.");
            return;
        }

        Product product =
                new Product(ID, name, description, cost);

        try (RandomAccessFile file =
                     new RandomAccessFile(dataFile, "rw")) {

            file.seek(file.length());

            product.writeTo(file);

            JOptionPane.showMessageDialog(this,
                    "Product successfully added!");

            clearFields();
            updateRecordCount();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error writing product: " + ex.getMessage());
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        descriptionField.setText("");
        costField.setText("");
        idField.requestFocus();
    }

    private void updateRecordCount() {
        try (RandomAccessFile file =
                     new RandomAccessFile(dataFile, "rw")) {

            long count = file.length() / Product.RECORD_SIZE;
            countField.setText(String.valueOf(count));

        } catch (IOException ex) {
            countField.setText("0");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RandProductMaker maker = new RandProductMaker();
            maker.setVisible(true);
        });
    }
}
