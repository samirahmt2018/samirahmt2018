package com.gundan.terragold.util;


import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiResponse<T> {
    private Map<String, String> _links;
    private List<String> _warning;
    private Map<String, Object> _attributes;
    private List<List<Map<String, String>>> _errors;
    private List<String> _info;
    private Generated _generated;
    private T payload;

    // Constructor
    public ApiResponse() {
        this._links = new java.util.LinkedHashMap<>();
        this._warning = new ArrayList<>();
        this._attributes = new java.util.LinkedHashMap<>();
        this._errors = new ArrayList<>();
        this._info = new ArrayList<>();
        this._generated = new Generated(ZonedDateTime.now());
        this.payload = null;
    }

    // Getters and setters
    public Map<String, String> get_links() { return _links; }
    public void set_links(Map<String, String> _links) { this._links = _links; }
    public List<String> get_warning() { return _warning; }
    public void set_warning(List<String> _warning) { this._warning = _warning; }
    public Map<String, Object> get_attributes() { return _attributes; }
    public void set_attributes(Map<String, Object> _attributes) { this._attributes = _attributes; }
    public List<List<Map<String, String>>> get_errors() { return _errors; }
    public void set_errors(List<List<Map<String, String>>> _errors) { this._errors = _errors; }
    public List<String> get_info() { return _info; }
    public void set_info(List<String> _info) { this._info = _info; }
    public Generated get_generated() { return _generated; }
    public void set_generated(Generated _generated) { this._generated = _generated; }
    public T getPayload() { return payload; }
    public void setPayload(T payload) { this.payload = payload; }

    // Nested class for _generated
    public static class Generated {
        private String date;
        private int timezone_type;
        private String timezone;

        public Generated(ZonedDateTime dateTime) {
            this.date = dateTime.toString();
            this.timezone_type = 3; // As per your example
            this.timezone = dateTime.getZone().toString();
        }

        // Getters and setters
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public int getTimezone_type() { return timezone_type; }
        public void setTimezone_type(int timezone_type) { this.timezone_type = timezone_type; }
        public String getTimezone() { return timezone; }
        public void setTimezone(String timezone) { this.timezone = timezone; }
    }
}