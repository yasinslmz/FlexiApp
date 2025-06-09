# FlexiApp Setup Guide

This guide will help you configure FlexiApp with your own API backend and get it running on your development environment.

## 🔧 Initial Configuration

### Step 1: Configure API Endpoints

1. **Open the API configuration file**:
   ```
   app/src/main/java/com/aliba/flexiapp/config/ApiConfig.java
   ```

2. **Replace placeholder values** with your actual API information:
   ```java
   // Replace with your actual API base URL
   private static final String BASE_URL = "https://your-api-domain.com";
   
   // Add your API credentials (if required)
   public static final String API_KEY = "your-actual-api-key";
   public static final String API_SECRET = "your-actual-api-secret";
   ```

3. **Update endpoint paths** if needed:
   ```java
   public static final String SIGNIN_ENDPOINT = BASE_URL + "/api/auth/signin";
   public static final String SIGNUP_ENDPOINT = BASE_URL + "/api/auth/signup";
   public static final String PROFILE_ENDPOINT = BASE_URL + "/api/user/profile";
   public static final String SAVE_LEVEL_ENDPOINT = BASE_URL + "/api/user/savelevel";
   ```

### Step 2: Network Security Configuration

1. **Open network security config**:
   ```
   app/src/main/res/xml/network_security_config.xml
   ```

2. **Add your domain** (for HTTPS - recommended):
   ```xml
   <network-security-config>
       <domain-config cleartextTrafficPermitted="false">
           <domain includeSubdomains="true">your-api-domain.com</domain>
       </domain-config>
   </network-security-config>
   ```

3. **For HTTP (development only)**:
   ```xml
   <network-security-config>
       <domain-config cleartextTrafficPermitted="true">
           <domain includeSubdomains="true">your-api-domain.com</domain>
       </domain-config>
   </network-security-config>
   ```

   ⚠️ **Warning**: Never use HTTP in production!

## 🔒 Security Best Practices

### Environment Variables (Recommended)

Instead of hardcoding sensitive information, use environment variables:

1. **Create a config file** (not tracked by git):
   ```
   app/src/main/assets/config.properties
   ```

2. **Add to .gitignore**:
   ```
   app/src/main/assets/config.properties
   ```

3. **Load configuration dynamically**:
   ```java
   // In ApiConfig.java
   private static String loadConfigProperty(String key) {
       try {
           InputStream inputStream = context.getAssets().open("config.properties");
           Properties properties = new Properties();
           properties.load(inputStream);
           return properties.getProperty(key);
       } catch (IOException e) {
           return null;
       }
   }
   ```

### Gradle Configuration

You can also use gradle properties:

1. **Add to gradle.properties** (local only):
   ```properties
   FLEXI_API_URL="https://your-api-domain.com"
   FLEXI_API_KEY="your-api-key"
   ```

2. **Use in build.gradle.kts**:
   ```kotlin
   android {
       defaultConfig {
           buildConfigField("String", "API_URL", "\"${project.findProperty("FLEXI_API_URL")}\"")
           buildConfigField("String", "API_KEY", "\"${project.findProperty("FLEXI_API_KEY")}\"")
       }
   }
   ```

3. **Access in code**:
   ```java
   private static final String BASE_URL = BuildConfig.API_URL;
   public static final String API_KEY = BuildConfig.API_KEY;
   ```

## 📡 Required API Endpoints

Your backend API must implement these endpoints:

### Authentication Endpoints

#### POST /api/auth/signin
```json
Request:
{
  "data": [
    {
      "email": "user@example.com",
      "password": "userpassword"
    }
  ]
}

Response:
{
  "ID": 1,
  "OperationMessage": "Login successful",
  "Data": { ... }
}
```

#### POST /api/auth/signup
```json
Request:
{
  "data": [
    {
      "Name": "John",
      "LastName": "Doe",
      "Email": "john@example.com",
      "Password": "password123",
      "EnglishLevel": ""
    }
  ]
}

Response:
{
  "ID": 1,
  "OperationMessage": "Registration successful"
}
```

### User Management Endpoints

#### GET /api/user/profile?email={email}
```json
Response:
{
  "Name": "John",
  "LastName": "Doe",
  "Email": "john@example.com",
  "EnglishLevel": "B2"
}
```

#### POST /api/user/savelevel
```json
Request:
{
  "data": [
    {
      "email": "user@example.com",
      "englishLevel": "B2"
    }
  ]
}

Response:
{
  "ID": 1,
  "OperationMessage": "Level saved successfully"
}
```

## 🧪 Testing Your Configuration

### 1. Configuration Validation

Add this method to test your configuration:

```java
public static boolean validateConfiguration() {
    if (BASE_URL.contains("your-api-domain.com")) {
        Log.e("ApiConfig", "❌ Base URL not configured!");
        return false;
    }
    
    if (API_KEY.equals("your-api-key-here")) {
        Log.e("ApiConfig", "❌ API Key not configured!");
        return false;
    }
    
    Log.i("ApiConfig", "✅ Configuration looks good!");
    return true;
}
```

### 2. Network Connectivity Test

Test if your API is reachable:

```java
private void testApiConnection() {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
        .url(ApiConfig.getBaseUrl() + "/health") // If you have a health endpoint
        .build();
        
    client.newCall(request).enqueue(new Callback() {
        @Override
        public void onResponse(Call call, Response response) {
            Log.i("API Test", "✅ API is reachable: " + response.code());
        }
        
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e("API Test", "❌ API not reachable: " + e.getMessage());
        }
    });
}
```

## 🚀 Development vs Production

### Development Configuration
```java
// Development - use test servers
private static final String BASE_URL = "https://test-api.yourapp.com";
private static final boolean DEBUG_MODE = true;
```

### Production Configuration
```java
// Production - use live servers
private static final String BASE_URL = "https://api.yourapp.com";
private static final boolean DEBUG_MODE = false;
```

### Using Build Variants

Create different configurations for different environments:

```kotlin
// In build.gradle.kts
android {
    buildTypes {
        debug {
            buildConfigField("String", "API_URL", "\"https://test-api.yourapp.com\"")
            buildConfigField("boolean", "DEBUG_MODE", "true")
        }
        release {
            buildConfigField("String", "API_URL", "\"https://api.yourapp.com\"")
            buildConfigField("boolean", "DEBUG_MODE", "false")
        }
    }
}
```

## ⚠️ Common Issues

### 1. Network Security Exception
```
Error: Cleartext HTTP traffic not permitted
Solution: Use HTTPS or configure network_security_config.xml
```

### 2. API Not Reachable
```
Error: Failed to connect to API
Solutions:
- Check if API server is running
- Verify URL is correct
- Check network permissions in AndroidManifest.xml
```

### 3. SSL Certificate Issues
```
Error: SSL handshake failed
Solutions:
- Ensure your API has valid SSL certificate
- Don't use self-signed certificates in production
- Check certificate chain
```

## 🔍 Debugging Tips

### Enable Network Logging
```java
// In debug builds only
if (BuildConfig.DEBUG) {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    
    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(logging)
        .build();
}
```

### Check Logs
Use these log tags to debug:
- `ApiConfig` - Configuration issues
- `NetworkCall` - API call debugging
- `AuthManager` - Authentication issues

## 📞 Need Help?

If you encounter issues:

1. **Check the logs** for error messages
2. **Verify your API** with tools like Postman
3. **Test network connectivity** on your device
4. **Create an issue** on GitHub with:
   - Error logs
   - Steps to reproduce
   - Device/OS information

---

**Remember**: Never commit real API keys or sensitive information to version control!