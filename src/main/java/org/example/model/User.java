package org.example.model;

public class User  {
    private Integer id;
    private int age;
    private String name, email;

    public User(String name, String email, int age) {
        this.age = age;
        this.email = email;
        this.name = name;
    }

    public User(Integer id, String name, String email, int age) {
        this.id = id;
        this.age = age;
        this.email = email;
        this.name = name;
    }

    @Override
    public String toString() {
        return "id: " + id + " name: " + name + " age: " + age + " email: " + email;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
}
