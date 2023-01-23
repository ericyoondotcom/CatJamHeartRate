# CatJamHeartRate
[catjam.yoonicode.com](https://catjam.yoonicode.com)

WearOS + Web OBS element to sync a CatJam gif to your heart rate.

Inspired by [Domi2803's HeartWear](https://github.com/Domi2803/HeartWear)

Idea by [hex-key](https://github.com/hex-key) :)

## Instructions For Use
1. [Get the WearOS app](https://play.google.com/store/apps/details?id=com.yoonicode.catjam_wearos), or compile from source
2. Visit [catjam.yoonicode.com](https://catjam.yoonicode.com) and sign in with Google
3. Follow instructions to add the OBS Browser Source

## Getting Started with Development
- Create a Firebase project and enable Authentication, Sign In with Google, and Realtime Database
- Clone `catjam-wearos/app/src/main/res/values/secrets.template.xml` into `catjam-wearos/app/src/main/res/values/secrets.xml` and fill in the values
    - `firebase_web_client_id`: your Web application client ID in the Google Cloud console ([info](https://firebase.google.com/docs/auth/android/google-signin#authenticate_with_firebase))
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