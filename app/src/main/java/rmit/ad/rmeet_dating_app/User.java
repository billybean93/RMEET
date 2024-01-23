package rmit.ad.rmeet_dating_app;

public class User {
    private String name;
    private String phoneNumber;
    private String imageURL;
    private int age;
    private String education;
    private String sex;

    public User(String name, String phoneNumber, String imageURL, int age, String education, String sex) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.imageURL = imageURL;
        this.age = age;
        this.education = education;
        this.sex = sex;
    }

    public User(String name, String imageURL){
        this.name = name;
        this.imageURL = imageURL;
    }

    // Getter and setter methods for each attribute

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
