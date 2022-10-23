const FRAME_WIDTH = 540;
const NUM_FRAMES = 6;
const INTERVAL = 5;

window.addEventListener("load", () => {
    registerListeners();
});

let frame = 0;
let hr = 1;
let timeout;
let image;
let text;
let app;
let db;

function registerListeners() {
    image = document.getElementById("main-image");
    text = document.getElementById("number");

    const firebaseConfig = {
        apiKey: "AIzaSyCUMiOVnY3nzKu93XtywbYMtRspR6JG2XQ",
        authDomain: "cat-jam-heart-rate.firebaseapp.com",
        databaseURL: "https://cat-jam-heart-rate-default-rtdb.firebaseio.com",
        projectId: "cat-jam-heart-rate",
        storageBucket: "cat-jam-heart-rate.appspot.com",
        messagingSenderId: "904243714533",
        appId: "1:904243714533:web:c54e1da6e2e3961336770c"
    };
    app = firebase.initializeApp(firebaseConfig);
    db = firebase.database();
    const hrRef = db.ref("users/test/heart_rate");

    hrRef.on("value", (snap) => {
        hr = snap.val();
        clearTimeout(timeout);
        nextFrame();
        text.innerHTML = hr;
    });
}


function nextFrame() {
    const TRUE_NUM_FRAMES = NUM_FRAMES * 2 - 2;

    const fps = hr / 60 * TRUE_NUM_FRAMES;
    const millisPerFrame = Math.round(1 / fps * 1000);

    const trueFrame = frame < NUM_FRAMES ? frame : NUM_FRAMES - (frame - NUM_FRAMES + 2);
    image.style.left = -trueFrame * FRAME_WIDTH + "px";

    frame++;
    if (frame >= TRUE_NUM_FRAMES) {
        frame = 0;
    }

    timeout = setTimeout(nextFrame, millisPerFrame);
}
