package lab.poly.demoasm_and103;

public class fruitModel {
    //Các thuộc tính của model giống mongodb
    private String _id;
    private String name;
    private Double price;
    private String origin;
    private String image;
    private int quantity;


    public fruitModel() {
    }

    public fruitModel(String _id, String name, Double price, String origin, String image, int quantity) {
        this._id = _id;
        this.name = name;
        this.price = price;
        this.origin = origin;
        this.image = image;
        this.quantity = quantity;
    }

    public fruitModel(String name, Double price, String origin, String image, int quantity) {
        this.name = name;
        this.price = price;
        this.origin = origin;
        this.image = image;
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
