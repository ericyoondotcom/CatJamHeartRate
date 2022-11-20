const FRAME_WIDTH = 540;
const NUM_FRAMES = 6;
const INTERVAL = 5;

window.addEventListener("load", () => {
    registerListeners();
});

let cycleStartTime = Date.now();
let hrForThisCycle = 0; // Cat display will not update until one cycle is completed
let frame = 0;
let hr = 0;
let accuracy = "";
let timeout;
let image;
let text;
let app;
let db;

function registerListeners() {
    image = document.getElementById("main-image");
    text = document.getElementById("number");

    app = firebase.initializeApp(firebaseConfig);
    db = firebase.database();
    const hrRef = db.ref("users/test/heart_rate");
    const accRef = db.ref("users/test/accuracy");

    hrRef.on("value", (snap) => {
        hr = snap.val();
    });

    accRef.on("value", (snap) => {
        accuracy = snap.val();
    });

    setInterval(poll, INTERVAL);
}

function poll() {
    function newCycle() {
        cycleStartTime = Date.now();
        hrForThisCycle = hr;
    }
    if(accuracy.length > 0) {
        text.innerHTML = accuracy;
        newCycle();
        return;
    } else if(hr <= 0) {
        text.innerHTML = "...";
        newCycle();
        return;
    }

    const TRUE_NUM_FRAMES = NUM_FRAMES * 2 - 2;
    const fps = hr / 60 * TRUE_NUM_FRAMES;
    const millisPerFrame = Math.round(1 / fps * 1000);

    const elapsedTime = Date.now() - cycleStartTime;
    const frameNumber = Math.floor(elapsedTime / millisPerFrame);
    if(frameNumber >= TRUE_NUM_FRAMES) newCycle();

    const trueFrame = frameNumber < NUM_FRAMES ? frameNumber : NUM_FRAMES - (frameNumber - NUM_FRAMES + 2);
    image.style.left = -trueFrame * FRAME_WIDTH + "px";

    text.innerHTML = hr;
}
