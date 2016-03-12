package com.nhacks.share.Objects;

/**
 * Created by Sagar on 3/12/2016.
 */
public class UserBook {
    private int userId;
    private int bookId;
    private int id;
    private int bookAvailabilityId;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookAvailabilityId() {
        return bookAvailabilityId;
    }

    public void setBookAvailabilityId(int bookAvailabilityId) {
        this.bookAvailabilityId = bookAvailabilityId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
