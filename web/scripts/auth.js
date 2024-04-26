function authForms(element) {
    unselect();
    
    const ID = element.id;
    const FORM = document.getElementById(`${ID}-form`)
    
    element.classList.add('selected');
    FORM.classList.add('selected')
}

function unselect() {
    const LABELS = document.getElementById('labels');
    const FORMS = document.getElementsByClassName('forms');

    Array.from(LABELS.children).forEach(e => {
        e.classList.remove('selected');
    });

    Array.from(FORMS).forEach(e => {
        e.classList.remove('selected');
    });
}

async function register() {
    const USERNAME = document.getElementById('username-register').value;
    const EMAIL = document.getElementById('email-register').value;
    const BIRTHDATE = document.getElementById('birthdate-register').value;
    const PASSWORD = document.getElementById('password-register').value;
    const CONFIRM_PASSWORD = document.getElementById('confirmPassword-register').value;

    if (PASSWORD != CONFIRM_PASSWORD) {
        alert("The passwords doesn't match");
        return false;
    }

    const BODY = {
        username: USERNAME,
        email: EMAIL,
        birthDate: BIRTHDATE,
        password: PASSWORD
    }
    const OPTIONS = {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(BODY)
    }
    const URL = `${ROUTES.auth}/sign-up`;
    const EXPECTED_STATUS = 200;

    showLoading();
    try {
        const DATA = await send(URL, OPTIONS, EXPECTED_STATUS);

        authenticate(DATA);
        window.location.href = '/pages/account.html';
    } catch (error) {
        alert(error);
    }
    hideLoading();
}


async function login() {
    const USERNAME = document.getElementById('username-login').value;
    const PASSWORD = document.getElementById('password-login').value;

    const BODY = {
        "username": USERNAME,
        "password": PASSWORD
    };
    const OPTIONS = {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(BODY)
    };
    const URL = `${ROUTES.auth}/sign-in`;
    const EXPECTED_STATUS = 200;

    showLoading();
    try {
        const DATA = await send(URL, OPTIONS, EXPECTED_STATUS);

        authenticate(DATA);
        window.location.href = '/index.html';
    } catch (error) {
        alert(error);
    }
    hideLoading();
}

function authenticate(DATA) {
    const SESSION = generateToken(DATA.token);
    const AUTH = {
        session: SESSION,
        idUser: DATA.idUser
    };
    setAuth(AUTH);
}

function generateToken(token) {
    expiration = new Date().getTime() + (3 * 60 * 60 * 1000); // 3 Horas
    return { token, expiration }
}