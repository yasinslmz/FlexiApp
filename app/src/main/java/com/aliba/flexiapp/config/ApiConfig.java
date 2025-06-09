package com.aliba.flexiapp.config;

/**
 * API Configuration Class
 * 
 * IMPORTANT: Before using this app, you MUST:
 * 1. Replace "your-api-domain.com" with your actual API domain
 * 2. Make sure your API supports HTTPS
 * 3. Update all endpoint paths according to your API documentation
 * 
 * DO NOT commit real API URLs or secrets to version control!
 */
public class ApiConfig {
    
    // TODO: Replace with your actual API base URL
    // Example: https://api.yourapp.com or https://your-backend.herokuapp.com
    private static final String BASE_URL = "https://your-api-domain.com";
    
    // API Endpoints - Update these according to your API
    public static final String SIGNIN_ENDPOINT = BASE_URL + "/api/auth/signin";
    public static final String SIGNUP_ENDPOINT = BASE_URL + "/api/auth/signup";
    public static final String PROFILE_ENDPOINT = BASE_URL + "/api/user/profile";
    public static final String SAVE_LEVEL_ENDPOINT = BASE_URL + "/api/user/savelevel";
    
    // API Keys and Tokens
    // TODO: Move these to environment variables or secure storage
    // NEVER commit real API keys to version control!
    public static final String API_KEY = "your-api-key-here";
    public static final String API_SECRET = "your-api-secret-here";
    
    // Request Headers
    public static final String CONTENT_TYPE = "application/json; charset=utf-8";
    public static final String USER_AGENT = "FlexiApp/1.0 Android";
    
    /**
     * Get base URL for API requests
     * @return Base URL string
     */
    public static String getBaseUrl() {
        return BASE_URL;
    }
    
    /**
     * Check if API configuration is properly set
     * @return true if configuration looks valid
     */
    public static boolean isConfigured() {
        return !BASE_URL.contains("your-api-domain.com") && 
               !API_KEY.equals("your-api-key-here");
    }
} 