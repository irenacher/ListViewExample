package com.example.irenachernyak.listviewexample;

/**
 * Created by irenachernyak on 1/14/16.
 */
public class Physician {
    public String name;
    public String clinic;
    public String logo;
    public String photo;
    public String smallphoto;
    public String phone;
    public String info;
    public String email;
    public Address address;

    public class Address
    {
        public String housestreet;
        public String suite;
        public String city;
        public String state;
        public String zipcode;
        public String country;

        public double latitude;
        public double longitude;

    }
}
