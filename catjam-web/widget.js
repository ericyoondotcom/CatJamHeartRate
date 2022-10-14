const FRAME_WIDTH = 540;

window.addEventListener("load", () => {
    registerListeners();
});

function registerListeners() {
    const image = document.getElementById("main-image");
    const input = document.getElementById("frame-input");
    
    input.onchange = () => {
        console.log("ONCHANGE")
        image.style.left = -input.value * FRAME_WIDTH + "px";
    }
    
}
