package Models;

public class ProfileResponse {
    private String name;
    private String lastName;
    private String email;
    private String englishLevel;

    // Getter ve Setter'lar
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnglishLevel() {
        if (englishLevel == null || englishLevel.isEmpty()) {
            return "Henüz ölçülmedi"; // Null veya boş ise "" döner
        }
        return englishLevel; // Değer mevcutsa onu döner
    }

    public void setEnglishLevel(String englishLevel) {
        this.englishLevel = englishLevel;
    }
}
