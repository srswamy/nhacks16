package com.nhacks.share.Objects;

/**
 * Created by SagarkumarDave on 10/19/2015.
 */
public class RecyclerViewRow {
    public int iconId;
    public String title;
    private String category;
    private String userBookId;
    private String edition;
    private Double price;
    private Integer rentedCount;

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getUserBookId() {
        return userBookId;
    }

    public void setUserBookId(String userBookId) {
        this.userBookId = userBookId;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {return price;}

    public void setPrice(Double price) { this.price = price; }

    public Integer getRentedCount() {return rentedCount;}

    public void setRentedCount(Integer rentedCount) { this.rentedCount = rentedCount;}
}
