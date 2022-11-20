let app;
let auth;

window.addEventListener("load", () => {
    loadPage();
});

function loadPage() {
    app = firebase.initializeApp(firebaseConfig);
    auth = firebase.auth();

    auth.onAuthStateChanged(user => {
        if(user) {
            document.getElementById("authenticated").style.display = "block";
            document.getElementById("unauthenticated").style.display = "none";
            document.getElementById("id").innerText = user.uid;
            document.getElementById("iframe").src = `../widget/index.html?id=${user.uid}`;
        } else {
            document.getElementById("authenticated").style.display = "none";
            document.getElementById("unauthenticated").style.display = "block";
        }
    });
}

function signIn() {
    const provider = new firebase.auth.GoogleAuthProvider();
    auth.signInWithPopup(provider).then(res => {
    }, e => {
        console.error(e);
        alert("Error signing in: " + e.message);
    });
}

function signOut() {
    auth.signOut().then(() => {
    }, e => {
        console.error(e);
        alert("Error signing out: " + e.message);
    });
}