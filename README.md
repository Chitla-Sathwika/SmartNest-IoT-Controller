#  SmartNest - IoT Home Controller

An Android application built in Java that simulates a Smart Home IoT Controller with real-time device management, Firebase integration, and MVVM architecture.

## 📱 Features
- 🔐 Login / Signup / Forgot Password (Firebase Auth)
- 🏠 Dashboard with 4 IoT devices (AC, Fan, Light, Door Lock)
- 🔄 Real-time ON/OFF sync (Firebase Realtime Database)
- 📋 Activity logs with timestamps
- 📊 Usage statistics (Bar, Line, Pie charts)
- 🔔 Push notifications on device toggle
- 🌙 Dark / Light mode toggle
- 💾 Offline caching (Room Database)

## 🛠️ Tech Stack
- **Language:** Java
- **Architecture:** MVVM (ViewModel + LiveData + Repository)
- **Backend:** Firebase Authentication + Realtime Database + FCM
- **Local DB:** Room Database
- **Charts:** MPAndroidChart
- **UI:** Material Design, RecyclerView, ViewBinding

## 📸 Screenshots
_Coming soon_

## 🚀 Setup
1. Clone the repo
2. Add your `google-services.json` to the `app/` folder
3. Enable Firebase Auth and Realtime Database in Firebase Console
4. Run on Android Studio with API 24+
