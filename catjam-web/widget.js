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

function registerListeners() {
    image = document.getElementById("main-image");
    text = document.getElementById("number");
    nextFrame();
    const input = document.getElementById("hr-input");
    input.onchange = () => {
        hr = input.value;
        clearTimeout(timeout);
        nextFrame();
        text.innerHTML = hr;
    }
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
