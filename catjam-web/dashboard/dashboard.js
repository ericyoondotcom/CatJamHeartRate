let app;
let auth;
let db;

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

function deleteData() {
    db = firebase.database();
    const user = auth.currentUser;
    if(!user) return;
    const ref = db.ref(`users/${user.uid}`);
    ref.remove().then(() => {
        alert("Data deleted");
        window.location.replace("../");
    }, e => {
        console.error(e);
    });
    return false;
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