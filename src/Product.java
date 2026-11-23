import java.util.Objects;

public class Product {
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
