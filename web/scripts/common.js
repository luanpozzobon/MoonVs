const CONFIG = JSON.parse(sessionStorage.getItem("config")) || {
    BASE_URL: "http://localhost:8080"
    // BASE_URL: "https://moonvs.fly.dev"
};

function setAuth(auth) {
    CONFIG.TOKEN = `Bearer ${auth.session.token}`;
    CONFIG.EXPIRATION = auth.session.expiration;
    CONFIG.ID_USER = auth.id;

    sessionStorage.setItem("config", JSON.stringify(CONFIG));
};

function checkAuth() {
    const NOW = new Date().getTime();

    if (CONFIG.TOKEN == null || CONFIG.EXPIRATION < NOW) {
        logout()
    };
};

function logout() {
    sessionStorage.clear();
    window.location.href = "/pages/auth.html";
}

const ROUTES = {
    auth: `${CONFIG.BASE_URL}/auth`,
    account: `${CONFIG.BASE_URL}/account`,
    content: `${CONFIG.BASE_URL}/content`,
    lists: `${CONFIG.BASE_URL}/lists`,
    profile: `${CONFIG.BASE_URL}/profile`,
    rating: `${CONFIG.BASE_URL}/rating`
};

async function send(URL, OPTIONS, EXPECTED_STATUS) {
    return await fetch(URL, OPTIONS)
        .then(response => {
            if (response.status != EXPECTED_STATUS) {
                throw new Error(response.headers.get('message'));
            }

            if (response.status == 204) {
                return;
            }

            return response.json();
        })
}