const CONFIG = JSON.parse(sessionStorage.getItem("config")) || {
    BASE_URL: "https://moonvs.fly.dev"
};

function setAuth(auth) {
    if (auth.session.token) {
        CONFIG.TOKEN = auth.session.token;
    }

    CONFIG.EXPIRATION = auth.session.expiration;
    CONFIG.ID_USER = auth.idUser;

    sessionStorage.setItem("config", JSON.stringify(CONFIG));
};

function checkAuth() {
    const NOW = new Date().getTime();

    if (!CONFIG.TOKEN || CONFIG.EXPIRATION < NOW) {
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
    showLoading();
    return await fetch(URL, OPTIONS)
        .then(response => {
            hideLoading();
            if (response.status != EXPECTED_STATUS) {
                throw new Error(response.headers.get('message'));
            }

            if (response.status == 204) {
                return;
            }

            return response.json();
        })
}

function getPoster(POSTER_URL, POSTER_PATH) {
    if (POSTER_PATH == 'null') {
        return '/assets/images/logos/Logo-B-Symbol.svg';
    }

    return `${POSTER_URL}/${POSTER_PATH}`;
}

function showLoading() {
    document.getElementById('loading-overlay').style.display = 'flex';
}

function hideLoading() {
    setTimeout(() => {
        document.getElementById('loading-overlay').style.display = 'none';
    }, 500);
}
