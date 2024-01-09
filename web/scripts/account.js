const AUTH = JSON.parse(sessionStorage.getItem('auth'));
const TOKEN = AUTH.session.token;
const ID_USER = AUTH.id;
const BASE_URL = "https://moonvs.fly.dev/";

const biographyField = document.getElementById('biography');
const username = document.getElementById('username');

let profile;
getProfile();

function getProfile() {
    const URL = BASE_URL + 'profile/' + ID_USER;
    const options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + TOKEN
        }
    };

    fetch(URL, options)
        .then(response => response.json())
        .then(data => {
            username.innerHTML = `@ ${data.username}`;
            biographyField.value = data.biography;
            profile = true;
        })
        .catch(error => {
            profile = false;
        });
}

function saveProfile() {
    var url = BASE_URL;

    const biography = biographyField.value;

    const body = {
        'idUser': ID_USER,
        'biography': biography
    };

    var options = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + TOKEN
        },
        body: JSON.stringify(body)
    };

    if (profile == true) {
        options.method = 'PUT';
        url += 'profile/edit/' + ID_USER;
    } else {
        options.method = 'POST';
        url += 'profile/create/'  + ID_USER;
    }

    fetch(url, options)
        .then(response => {
            window.location.href = './account.html';
        })
        .catch(error => {
            alert(error.message);
        });
}

const usernameField = document.getElementById('editUsername');
const emailField = document.getElementById('editEmail');

function editAccount() {
    const URL = BASE_URL + 'account/update/username-and-email';
    const editUsername = usernameField.value;
    const editEmail = emailField.value;

    const body = {
        "idUser": ID_USER,
        "username": editUsername,
        "email": editEmail
    };

    const options = {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + TOKEN
        },
        body: JSON.stringify(body)
    };

    fetch(URL, options)
        .then(response => response.json())
        .then(data => {
            sessionStorage.removeItem('auth');
            window.location.href = './account.html';
        })

}
const passwordField = document.getElementById('editPassword');
const confirmPasswordField = document.getElementById('confirmPassword');

function changePassword() {
    const URL = BASE_URL + 'account/update/password'
    const password = passwordField.value;
    const confirmPassword = confirmPasswordField.value;

    if (password != confirmPassword) {
        alert("The passwords doesn't match")
        return false;
    }

    const body = {
        "idUser": ID_USER,
        "password": password
    };

    const options = {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + TOKEN
        },
        body: JSON.stringify(body)
    };

    fetch(URL, options)
        .then(response => response.json())
        .then(data => {
            sessionStorage.removeItem('auth');
            window.location.href = './account.html';
        })

}

const deleteUsernameField = document.getElementById('deleteUsername');
const deletePasswordField = document.getElementById('deletePassword');

function deleteAccount() {
    const URL = BASE_URL + 'account/delete-account'
    const deleteUsername = deleteUsernameField.value;
    const deletePassword = deletePasswordField.value;

    const body = {
        "idUser": ID_USER,
        "username": deleteUsername,
        "password": deletePassword
    };

    const options = {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + TOKEN
        },
        body: JSON.stringify(body)
    };

    fetch(URL, options)
        .then(response => response.json())
        .then(data => {
            sessionStorage.removeItem('auth');
            window.location.href = './account.html';
        })
}