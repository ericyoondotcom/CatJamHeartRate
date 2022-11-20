# CatJamHeartRate
WearOS + Web OBS element to sync a CatJam gif to your heart rate

# Getting Started with Development
- Create a Firebase project and enable Authentication, Sign In with Google, and Realtime Database
- Clone `catjam-wearos/app/src/main/res/values/secrets.template.xml` into `catjam-wearos/app/src/main/res/values/secrets.xml` and fill in the values
    - `firebase_web_client_id`: your Web application client ID in the GCP console ([info](https://firebase.google.com/docs/auth/android/google-signin#authenticate_with_firebase))
- Clone `catjam-web/env.template.js` into `catjam-web/env.js` and fill in the values
- Create a keystore to sign your app
    - Add your SHA-1 fingerprint to the Firebase console ([info](https://stackoverflow.com/a/49800546/4699945))
- (after adding SHA-1) Download your `google-services.json` file from Firebase and put it in `catjam-wearos/app/google-services.json`
- To deploy, make sure to create a keystore and set it up with Android Studio


## Process of getting cat film strip
1. Download CatJam gif
2. Split into frames using [this](https://ezgif.com/split)
3. Use [Strip Generator](https://www.wavesfactory.com/blog/posts/strip-generator/) to turn into a horizontal filmp strip
4. Open in Photoshop and replace black with transparent

Technique inspired by [Eelslap](http://eelslap.com/)