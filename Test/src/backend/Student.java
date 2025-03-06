package backend;

import com.google.gson.Gson;

public class Student {
    private int id;
    private String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Student(String id, String name) {
        this.id = Integer.parseInt(id);
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCsv() {
        return id + "," + name;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}