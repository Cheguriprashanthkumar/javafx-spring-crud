package com.example.frontend;

import javafx.beans.property.*;

import java.math.BigDecimal;

public class Product {

    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final ObjectProperty<BigDecimal> unitPrice = new SimpleObjectProperty<>();
    private final StringProperty category = new SimpleStringProperty();
    private final IntegerProperty hsnCode = new SimpleIntegerProperty();

    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id != null ? id : 0L);
    }

    public LongProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice.get();
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice.set(unitPrice);
    }

    public ObjectProperty<BigDecimal> unitPriceProperty() {
        return unitPrice;
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public int getHsnCode() {
        return hsnCode.get();
    }

    public void setHsnCode(int hsnCode) {
        this.hsnCode.set(hsnCode);
    }

    public IntegerProperty hsnCodeProperty() {
        return hsnCode;
    }
}
