var config = JSON.parse(sessionStorage.getItem("config")) || {
    BASE_URL: "https://moonvs.fly.dev"
};

function setAuth(auth) {
    config.TOKEN      = `Bearer ${auth.session.token}`;
    config.EXPIRATION = auth.session.expiration;
    config.ID_USER    = auth.id;

    sessionStorage.setItem("config", JSON.stringify(config));
};

function checkAuth() {
    const NOW = new Date().getTime();

    if (config.TOKEN == null || config.EXPIRATION < NOW) {
        logout()
    };
};

function logout() {
    sessionStorage.clear();
    window.location.href = "/pages/auth.html";
}