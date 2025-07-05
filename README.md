# notification-android

This document provides instructions on how to set up and run the `notification-android` project.

## Setup Instructions

### 1. Add Firebase Configuration File

- Obtain your `google-services.json` file from the Firebase console.
- Place this file in the `./androidApp/` directory of the project.

### 2. Update Application ID

- Open the `androidApp/build.gradle.kts` file.
- Locate the `defaultConfig` block.
- Update the `applicationId` with the one you created in the Firebase console. For example:
  ```kotlin
  android {
      // ...
      defaultConfig {
          applicationId = "your.firebase.project.application.id" // <-- UPDATE THIS LINE
          minSdk = 30
          targetSdk = 35
          versionCode = 1
          versionName = "1.0"
      }
      // ...
  }
  ```

### 3. Testing with `rest-api.http`

The `rest-api.http` file allows you to send test push notifications to your device. To use it, you need to configure the following:

#### a. Obtain Google Access Token

You need an access token from your Google account to authorize the request. You can generate this using the Google Cloud CLI:

1.  **Install gcloud CLI:** If you haven't already, [install the Google Cloud CLI](https://cloud.google.com/sdk/docs/install).
2.  **Login to your account:**
    ```bash
    gcloud auth login
    ```
3.  **Print the access token:**
    ```bash
    gcloud auth print-access-token
    ```
4.  **Update `rest-api.http`:** Copy the generated token and replace `TOKEN_FROM_GOOGLE_ACCOUNT` in the `rest-api.http` file:
    ```http
    ### Push Notification FCM

    POST https://fcm.googleapis.com/v1/projects/notifications-kmm/messages:send
    Authorization: Bearer YOUR_COPIED_ACCESS_TOKEN_HERE # <-- UPDATE THIS LINE
    Content-Type: application/json

    {
      "message": {
        // ...
      }
    }
    ```

#### b. Obtain FCM Token from Device

You need the Firebase Cloud Messaging (FCM) token from the Android device where the app is installed.

1.  **Run the application** on your Android device or emulator.
2.  **Get the FCM token:** The application should log the FCM token to Logcat when it starts or when the token is refreshed. Look for a log message similar to "FCM token: [YOUR_DEVICE_FCM_TOKEN]".
    *   *Note for developers:* If the token is not automatically logged, you might need to add logging for it in the `FirebaseMessagingService` implementation (usually in a method like `onNewToken(String token)`).
3.  **Update `rest-api.http`:** Copy the obtained FCM token and replace `FCM_TOKEN_FROM_DEVICE` in the `rest-api.http` file:
    ```http
    {
      "message": {
        "token": "YOUR_DEVICE_FCM_TOKEN_HERE", // <-- UPDATE THIS LINE
        "notification": {
          "title": "Title Notification",
          "body": "Body Notification"
        },
        "data": {
          "key_1": "Notification",
          "key_2": "Test"
        }
      }
    }
    ```

#### c. Send the Test Notification

Once both tokens are updated in `rest-api.http`, you can use an HTTP client (like the one built into IntelliJ IDEA or VS Code, or a tool like Postman) to execute the `POST` request defined in the file. This will send a push notification to the specified device.

The project ID used in the `rest-api.http` file is `notifications-kmm`. If your Firebase project has a different ID, you'll need to update the URL as well:
`POST https://fcm.googleapis.com/v1/projects/YOUR_FIREBASE_PROJECT_ID/messages:send`
