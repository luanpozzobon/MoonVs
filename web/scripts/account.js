const SESSION = JSON.parse(sessionStorage.getItem('session'));
const TOKEN = SESSION.token;
const BASE_URL = "https://moonvs.fly.dev/";

const biographyField = document.getElementById('biography');
const username = document.getElementById('username');

let profile;
getProfile();

function getProfile() {
    const options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + TOKEN
        }
    };

    fetch(BASE_URL + 'profile/', options)
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
        'biography': biography,
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
        url += 'profile/edit';
    } else {
        options.method = 'POST';
        url += 'profile/new';
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
    const editUsername = usernameField.value;
    const editEmail = emailField.value;

    const body = {
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

    fetch(BASE_URL + "account/update", options)
        .then(response => response.json())
        .then(data => {
            alert(data.message);
            sessionStorage.removeItem('session');
            window.location.href = './account.html';
        })

}
const passwordField = document.getElementById('editPassword');
const confirmPasswordField = document.getElementById('confirmPassword');

function changePassword() {
    const password = passwordField.value;
    const confirmPassword = confirmPasswordField.value;

    if (password != confirmPassword) {
        alert("The passwords doesn't match")
        return false;
    }

    const body = {
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

    fetch(BASE_URL + "account/update-password", options)
        .then(response => response.text())
        .then(data => {
            alert(data);
            sessionStorage.removeItem('session');
            window.location.href = './account.html';
        })

}

const deleteUsernameField = document.getElementById('deleteUsername');
const deletePasswordField = document.getElementById('deletePassword');

function deleteAccount() {
    const deleteUsername = deleteUsernameField.value;
    const deletePassword = deletePasswordField.value;

    const body = {
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

    fetch(BASE_URL + 'account/delete', options)
        .then(response => response.text())
        .then(data => {
            alert(data.message);
            sessionStorage.removeItem('session');
            window.location.href = './account.html';
        })
}