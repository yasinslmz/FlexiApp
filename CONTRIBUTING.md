# Contributing to FlexiApp

Thank you for your interest in contributing to FlexiApp! We welcome contributions from the community and are grateful for your help in making this project better.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [How to Contribute](#how-to-contribute)
- [Development Setup](#development-setup)
- [Coding Standards](#coding-standards)
- [Pull Request Process](#pull-request-process)
- [Issue Guidelines](#issue-guidelines)
- [Security Vulnerabilities](#security-vulnerabilities)

## 🤝 Code of Conduct

This project adheres to a code of conduct. By participating, you are expected to uphold this standard. Please report unacceptable behavior to [your-email@example.com].

### Our Standards

- **Be respectful**: Treat everyone with respect and kindness
- **Be inclusive**: Welcome newcomers and help them get started
- **Be constructive**: Provide helpful feedback and suggestions
- **Be patient**: Remember that everyone is learning

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or newer
- Android SDK (API level 24+)
- Java 8 or higher
- Git for version control
- GitHub account

### First Time Setup

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/yourusername/FlexiApp.git
   cd FlexiApp
   ```
3. **Add upstream remote**:
   ```bash
   git remote add upstream https://github.com/originalowner/FlexiApp.git
   ```
4. **Open in Android Studio** and sync the project

## 🛠️ How to Contribute

### Types of Contributions We Welcome

- 🐛 **Bug fixes**
- ✨ **New features**
- 📚 **Documentation improvements**
- 🎨 **UI/UX enhancements**
- 🔧 **Code refactoring**
- 🌐 **Translations**
- 🧪 **Tests**

### Before You Start

1. **Check existing issues** to see if someone is already working on it
2. **Create an issue** to discuss your proposed changes
3. **Wait for approval** before starting major features
4. **Keep changes focused** - one feature per pull request

## 💻 Development Setup

### Configuration

1. **Configure API settings** in `ApiConfig.java`:
   ```java
   // Use test/development endpoints
   private static final String BASE_URL = "https://your-test-api.com";
   ```

2. **Set up environment variables** (optional):
   ```bash
   export FLEXI_API_URL="your-development-api-url"
   export FLEXI_API_KEY="your-development-api-key"
   ```

### Running the App

1. **Sync project** with Gradle files
2. **Connect device** or start emulator
3. **Run app** using Android Studio or:
   ```bash
   ./gradlew installDebug
   ```

### Testing

- **Unit tests**: `./gradlew test`
- **Instrumented tests**: `./gradlew connectedAndroidTest`

## 📝 Coding Standards

### Java Style Guide

- **Indentation**: 4 spaces (no tabs)
- **Line length**: 120 characters maximum
- **Naming conventions**:
  - Classes: `PascalCase`
  - Methods/Variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
  - Resources: `snake_case`

### Code Quality

- **Comments**: Write clear, concise comments for complex logic
- **Method length**: Keep methods focused and reasonably short
- **Error handling**: Always handle exceptions appropriately
- **Null safety**: Check for null values where appropriate

### Example Code Style

```java
public class ExampleActivity extends AppCompatActivity {
    
    private static final String TAG = "ExampleActivity";
    private static final int REQUEST_CODE = 1001;
    
    private TextView titleText;
    private Button actionButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        
        initializeViews();
        setupClickListeners();
    }
    
    private void initializeViews() {
        titleText = findViewById(R.id.titleText);
        actionButton = findViewById(R.id.actionButton);
    }
    
    private void setupClickListeners() {
        actionButton.setOnClickListener(view -> handleButtonClick());
    }
    
    private void handleButtonClick() {
        // Handle button click logic
        Log.d(TAG, "Button clicked");
    }
}
```

## 🔄 Pull Request Process

### Before Submitting

1. **Update your fork**:
   ```bash
   git fetch upstream
   git checkout main
   git merge upstream/main
   ```

2. **Create feature branch**:
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make your changes** and commit:
   ```bash
   git add .
   git commit -m "feat: add new feature description"
   ```

### Commit Message Format

Use conventional commits format:

- `feat:` - New feature
- `fix:` - Bug fix
- `docs:` - Documentation changes
- `style:` - Code style changes
- `refactor:` - Code refactoring
- `test:` - Adding tests
- `chore:` - Maintenance tasks

Examples:
```bash
feat: add AI chat functionality
fix: resolve login authentication issue
docs: update API configuration guide
style: improve button styling consistency
```

### Submitting Pull Request

1. **Push your changes**:
   ```bash
   git push origin feature/your-feature-name
   ```

2. **Create pull request** on GitHub with:
   - Clear title and description
   - Reference to related issues
   - Screenshots (for UI changes)
   - Testing instructions

3. **Respond to feedback** promptly and make requested changes

### Pull Request Checklist

- [ ] Code follows project style guidelines
- [ ] Changes are tested thoroughly
- [ ] Documentation is updated if needed
- [ ] No hardcoded secrets or sensitive data
- [ ] Commit messages follow conventional format
- [ ] PR description explains what and why

## 🐛 Issue Guidelines

### Reporting Bugs

Use the bug report template and include:

- **Steps to reproduce** the issue
- **Expected behavior** vs actual behavior
- **Device/OS information**
- **App version** or commit hash
- **Screenshots** if applicable
- **Logs** or error messages

### Feature Requests

For new features, please:

- **Check existing issues** first
- **Describe the problem** you're trying to solve
- **Propose a solution** with implementation details
- **Consider alternatives** and their trade-offs

### Good Issue Examples

**Bug Report:**
```
Title: App crashes when selecting B2 level test

Device: Samsung Galaxy S21 (Android 12)
App Version: 1.0.0

Steps to reproduce:
1. Open app and login
2. Navigate to Level Test
3. Select "B2" level
4. App crashes immediately

Expected: Level test should start
Actual: App crashes with NullPointerException

Error log:
[Attach crash log]
```

**Feature Request:**
```
Title: Add dark mode support

Problem: App is difficult to use in low-light conditions

Proposed solution: Implement dark theme that:
- Uses Material Design dark colors
- Preserves app branding
- Follows system theme preference
- Includes toggle in settings

Benefits: Better user experience, reduced eye strain
```

## 🔒 Security Vulnerabilities

If you discover a security vulnerability, please:

1. **DO NOT** create a public issue
2. **Email** [security@example.com] with details
3. **Wait** for acknowledgment before disclosure
4. **Provide** reasonable time for fixes

We appreciate responsible disclosure and will credit security researchers appropriately.

## 🎉 Recognition

Contributors will be acknowledged in:

- **README.md** contributors section
- **Release notes** for significant contributions
- **Hall of Fame** for exceptional contributions

## 📞 Getting Help

If you need help:

- **GitHub Discussions** for general questions
- **Issues** for bug reports and feature requests
- **Email** [maintainer@example.com] for other inquiries

## 📄 License

By contributing to FlexiApp, you agree that your contributions will be licensed under the same [MIT License](LICENSE) that covers the project.

---

Thank you for contributing to FlexiApp! 🎉 