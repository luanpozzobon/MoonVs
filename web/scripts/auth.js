const labels = document.getElementById('labels');
const forms = document.getElementsByClassName('forms');

const loginForm = document.getElementById('login-form');
const registerForm = document.getElementById('register-form');

const usernameRegister = document.getElementById('username-register');
const emailRegister = document.getElementById('email-register');
const birthdateRegister = document.getElementById('birthdate-register');
const passwordRegister = document.getElementById('password-register');
const confirmPasswordRegister = document.getElementById('confirmPassword-register')

const usernameLogin = document.getElementById('username-login');
const passwordLogin = document.getElementById('password-login');

const BASE_URL = 'https://moonvs.fly.dev/auth'


function authForms(element) {
    unselect();

    const id = element.id;
    const form = document.getElementById(`${id}-form`)

    element.classList.add('selected');
    form.classList.add('selected')
}

function unselect() {
    Array.from(labels.children).forEach(e => {
        e.classList.remove('selected');
    });

    Array.from(forms).forEach(e => {
        e.classList.remove('selected');
    });
}

function register() {
    const username = usernameRegister.value;
    const email = emailRegister.value;
    const birthdate = birthdateRegister.value;
    const password = passwordRegister.value;
    const confirmPassword = confirmPasswordRegister.value;

    if (password != confirmPassword) {
        alert("The passwords doesn't match");
        return false;
    }

    const body = {
        "username": username,
        "email": email,
        "birthDate": birthdate,
        "password": password
    }

    const options = {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    }

    fetch(BASE_URL + "/register", options)
        .then(response => response.json())
        .then(data => {
            session = generateToken(data.token);
            sessionStorage.setItem('session', JSON.stringify(session));
            window.location.href = './account.html';
        })
        .catch(error => {
            alert(error.message);
        });
}


function login() {
    const username = usernameLogin.value;
    const password = passwordLogin.value;

    const body = {
        "username": username,
        "password": password
    };

    const options = {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    };

    fetch(BASE_URL + "/login", options)
        .then(response => response.json())
        .then(data => {
            session = generateToken(data.token);
            sessionStorage.setItem('session', JSON.stringify(session));
            window.location.href = '../index.html';
        })
        .catch(error => {
            alert(error);
        });
}

function generateToken(token) {
    expiration = new Date().getTime() + (3 * 60 * 60 * 1000); // 3 Horas
    return { token, expiration }
}