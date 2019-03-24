package com.example.application;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurant implements Parcelable {
    private String business_id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String postal_code;
    private String latitude;
    private String longitude;
    private String stars;
    private String review_count;
    private String is_open;
    private String categories;
    private String hours;

    public Restaurant(String id, String name, String address, String city, String state,
                      String postal_code, String latitude, String longitude, String stars,
                      String review_count, String is_open, String categories, String hours) {
        this.business_id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postal_code = postal_code;
        this.latitude = latitude;
        this.longitude = longitude;
        this.stars = stars;
        this.review_count = review_count;
        this.is_open = is_open;
        this.categories = categories;
        this.hours = hours;
    }

    protected Restaurant(Parcel in) {
        business_id = in.readString();
        name = in.readString();
        address = in.readString();
        city = in.readString();
        state = in.readString();
        postal_code = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        stars = in.readString();
        review_count = in.readString();
        is_open = in.readString();
        categories = in.readString();
        hours = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(business_id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(postal_code);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(stars);
        dest.writeString(review_count);
        dest.writeString(is_open);
        dest.writeString(categories);
        dest.writeString(hours.toString());
    }

    public String getBusiness_id() {
        return business_id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getStars() {
        return stars;
    }

    public String getReview_count() {
        return review_count;
    }

    public String getIs_open() {
        return is_open;
    }

    public String getCategories() {
        return categories;
    }

    public String getHours() {
        return hours;
    }

    public static Creator<Restaurant> getCREATOR() {
        return CREATOR;
    }
}
