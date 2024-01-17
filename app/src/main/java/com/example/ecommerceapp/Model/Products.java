package com.example.ecommerceapp.Model;

public class Products
{
    private String Pid ,category,date,description,image,pname,price,time,productState;

    public Products() {
    }

    public Products(String pid, String category, String date, String description, String image, String pname, String price, String time, String productState) {
        Pid = pid;
        this.category = category;
        this.date = date;
        this.description = description;
        this.image = image;
        this.pname = pname;
        this.price = price;
        this.time = time;
        this.productState = productState;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProductState() {
        return productState;
    }

    public void setProductState(String productState) {
        this.productState = productState;
    }
}
