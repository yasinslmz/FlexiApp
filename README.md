# FlexiApp - English Learning Mobile Application

[![Android API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

FlexiApp is a simple Android application that I developed as a learning project to explore mobile app development. As someone curious about mobile applications, I created this basic English learning app that includes interactive features like level testing, flash cards, multiple choice quizzes, and AI-powered conversations. This project helped me understand Android development fundamentals while building something useful for language learners.

## 🌟 Features

- **Level Assessment**: Comprehensive English level testing (B1, B2, C1)
- **Flash Cards**: Interactive vocabulary learning with flash cards
- **Multiple Choice Quizzes**: Test your knowledge with multiple choice questions
- **Fill in the Blanks**: Complete sentences to improve grammar
- **AI Chat Assistant**: Practice conversations with AI-powered chat
- **User Profiles**: Track your progress and manage your learning journey
- **Secure Authentication**: User registration and login system

## 📱 Screenshots

<div align="center">
 <img src="https://github.com/user-attachments/assets/6b79df4d-b450-4ef7-bfb5-e9cb34d3d659" alt="Seviye Testi" width="250">
  &nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/9e954f78-6f4a-4ab1-9956-81afc7a43913" alt="Giriş Ekranı" width="250">
  &nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/f44a10e1-d022-4b76-a6d8-10e37903f00e" alt="Kayıt Ekranı" width="250">
</div>

<br>

<div align="center">
 <img src="https://github.com/user-attachments/assets/4b769702-e7f8-4b1c-b451-aa245afe579d" alt="Ana Ekran" width="250">
  &nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/8b806a7a-14cc-4160-96ba-aaf6c1e1fa68" alt="Flash Cards" width="250">
  &nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/71bb6fb1-082a-416e-92ce-12126a17fce2" alt="Çoktan Seçmeli" width="250">
</div>

<br>

<div align="center">
  <img src="https://github.com/user-attachments/assets/db34f629-9680-4749-84d0-70d0daa3b065" alt="AI Chat" width="250">
  &nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/61b45149-76a3-4b59-9567-a013cf8be4ea" alt="Profil" width="250">
</div>


## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or newer
- Android SDK with API level 24 (Android 7.0) or higher
- Java 8 or higher
- An active API backend (see Configuration section)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/FlexiApp.git
   cd FlexiApp
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned directory and select it

3. **Configure API Settings**
   - Navigate to `app/src/main/java/com/aliba/flexiapp/config/ApiConfig.java`
   - Replace placeholder values with your actual API configuration:
   ```java
   private static final String BASE_URL = "https://your-api-domain.com";
   public static final String API_KEY = "your-actual-api-key";
   ```

4. **Update Network Security Config**
   - Open `app/src/main/res/xml/network_security_config.xml`
   - Add your API domain to the configuration

5. **Build and Run**
   - Sync the project with Gradle files
   - Connect your Android device or start an emulator
   - Click "Run" or press Shift+F10

## ⚙️ Configuration

### API Configuration

Before running the app, you **MUST** configure the following in `ApiConfig.java`:

```java
// Replace with your actual API base URL
private static final String BASE_URL = "https://your-api-domain.com";

// Add your API credentials
public static final String API_KEY = "your-api-key-here";
public static final String API_SECRET = "your-api-secret-here";
```

### Required API Endpoints

Your backend API should implement the following endpoints:

- `POST /api/auth/signin` - User authentication
- `POST /api/auth/signup` - User registration
- `GET /api/user/profile` - Get user profile
- `POST /api/user/savelevel` - Save user's English level

### Network Security

The app uses HTTPS by default. If you need to use HTTP for development:

1. Update `network_security_config.xml`
2. Add your domain to the cleartext traffic permitted list
3. **Never use HTTP in production!**

## 🏗️ Project Structure

```
app/
├── src/main/
│   ├── java/com/aliba/flexiapp/
│   │   ├── config/
│   │   │   └── ApiConfig.java          # API configuration
│   │   ├── LoginActivity.java          # User authentication
│   │   ├── RegisterActivity.java       # User registration
│   │   ├── MainActivity.java           # Main dashboard
│   │   ├── ProfileActivity.java        # User profile
│   │   ├── LevelTestActivity.java      # English level testing
│   │   ├── FlashCardsActivity.java     # Vocabulary flash cards
│   │   ├── MultipleChoiceActivity.java # Multiple choice quizzes
│   │   ├── FulfillWordsActivity.java   # Fill in the blanks
│   │   ├── AskAiChat.java             # AI chat assistant
│   │   └── models/                     # Data models
│   ├── res/
│   │   ├── layout/                     # UI layouts
│   │   ├── values/                     # Strings, colors, styles
│   │   ├── drawable/                   # Images and icons
│   │   └── xml/                        # Configuration files
│   └── assets/                         # JSON data files
└── build.gradle.kts                    # App-level build configuration
```

## 🛠️ Built With

- **Java** - Primary programming language
- **Android SDK** - Android development framework
- **OkHttp** - HTTP client for API communication
- **Gson** - JSON parsing library
- **Material Design Components** - Modern UI components

## 🔐 Security Notes

- All API communications use HTTPS
- SSL certificate validation is properly implemented
- User credentials are securely handled
- No sensitive data is hardcoded in the application

## 🤝 Contributing

We welcome contributions to FlexiApp! Please read our [Contributing Guidelines](CONTRIBUTING.md) before submitting pull requests.

### Development Workflow

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🐛 Known Issues

- None currently known

## 📞 Contact & Support

- **Issues**: [GitHub Issues](https://github.com/yourusername/FlexiApp/issues)
- **Discussions**: [GitHub Discussions](https://github.com/yourusername/FlexiApp/discussions)

## 🎯 Roadmap

- [ ] Kotlin migration
- [ ] Offline mode support
- [ ] Multi-language UI support
- [ ] Advanced AI features
- [ ] Progress analytics
- [ ] Social learning features

## 📋 Requirements

### Minimum Requirements
- Android 7.0 (API level 24)
- 2GB RAM
- 100MB storage space
- Internet connection

### Recommended Requirements
- Android 10.0 (API level 29) or higher
- 4GB RAM
- Stable internet connection

---

**Note**: This app requires a backend API to function properly. Make sure to configure your API endpoints before running the application.

## 🙏 Acknowledgments

- Material Design for UI components
- OkHttp team for reliable networking
- Google for Android development tools

---
*Made with ❤️ for English language learners* 
