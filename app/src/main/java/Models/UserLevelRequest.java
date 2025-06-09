package Models;
public class UserLevelRequest {
    private String email;
    private String englishLevel;

    public UserLevelRequest(String email, String englishLevel) {
        this.email = email;
        this.englishLevel = englishLevel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnglishLevel() {
        return englishLevel;
    }

    public void setEnglishLevel(String englishLevel) {
        this.englishLevel = englishLevel;
    }
}
