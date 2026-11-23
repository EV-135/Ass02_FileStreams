import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;

public class Product {
    public static final int NAME_LENGTH = 35;
    public static final int DESCRIPTION_LENGTH = 75;
    public static final int ID_LENGTH = 6;
    public static final int RECORD_SIZE = (NAME_LENGTH + DESCRIPTION_LENGTH + ID_LENGTH) * 2 + 8;

    String ID;
    String name;
    String description;
    double cost;

    public Product(String ID, String name, String description, double cost) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.cost = cost;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    private static String padRight(String value, int length){
        if(value.length() > length){
            throw new IllegalArgumentException("Text exceeds maximum length of " + length);
        }
        return String.format("%-" + length + "s", value);
    }

    public String getPaddedName(){
        return padRight(name, NAME_LENGTH);
    }

    public String getPaddedDescription(){
        return padRight(description, DESCRIPTION_LENGTH);
    }

    public String getPaddedID(){
        return padRight(ID, ID_LENGTH);
    }

    public String getRandomAccessRecord(){
        return getPaddedName() + getPaddedDescription() + getPaddedID();
    }

    public void writeTo(RandomAccessFile file) throws IOException {
        file.writeChars(getPaddedName());
        file.writeChars(getPaddedDescription());
        file.writeChars(getPaddedID());
        file.writeDouble(cost);
    }

    public static Product readFrom(RandomAccessFile file) throws IOException {
        String name = readFixedString(file, NAME_LENGTH).trim();
        String description = readFixedString(file, DESCRIPTION_LENGTH).trim();
        String ID = readFixedString(file, ID_LENGTH).trim();
        double cost = file.readDouble();

        return new Product(ID, name, description, cost);
    }

    private static String readFixedString(RandomAccessFile file, int length) throws IOException {

        StringBuilder value = new StringBuilder();

        for (int i = 0; i < length; i++) {
            value.append(file.readChar());
        }

        return value.toString();
    }

    @Override
    public String toString() {
        String ret = "Product{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                '}';
        return ret;
    }

    public  String toCSV() {
        String ret = ID + "," + name + "," + description + "," + cost;
        return ret;
    }

    public String toJson(){
        String ret = "{\"ID\":\"" + this.ID +
                "\",\"name\":\"" + this.name +
                "\",\"description\":\"" + this.description +
                "\",\"cost\":\"" + this.cost + "}";
        return ret;
    }

    public String toXML(){
        String ret = "<Product>" +
                "<ID>" + this.ID + "</ID>" +
                "<name>" + this.name + "</name>" +
                "<description>" + this.description + "</description>" +
                "<cost>" + this.cost + "</cost>" +
                "</Product>";
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(cost, product.cost) == 0 && Objects.equals(ID, product.ID) && Objects.equals(name, product.name) && Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, name, description, cost);
    }
}
