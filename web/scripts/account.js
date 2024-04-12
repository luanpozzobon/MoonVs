const PROFILE_URL = `${CONFIG.BASE_URL}/profile`;
const ACCOUNT_URL = `${CONFIG.BASE_URL}/account`;

const biographyField = document.getElementById('biography');
const username = document.getElementById('username');

let profile;
getProfile();

function getProfile() {
    const URL = `${PROFILE_URL}/${CONFIG.ID_USER}`;
    
    const options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': CONFIG.TOKEN
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
    var url = PROFILE_URL;

    const biography = biographyField.value;

    const body = {
        'idUser': CONFIG.ID_USER,
        'biography': biography
    };

    var options = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': CONFIG.TOKEN
        },
        body: JSON.stringify(body)
    };

    if (profile == true) {
        options.method = 'PUT';
        url += `/edit/${CONFIG.ID_USER}`;
    } else {
        options.method = 'POST';
        url += `/create/${CONFIG.ID_USER}`;
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
    const URL = `${ACCOUNT_URL}/update/username-and-email`;
    
    const editUsername = usernameField.value;
    const editEmail = emailField.value;

    const body = {
        "idUser": CONFIG.ID_USER,
        "username": editUsername,
        "email": editEmail
    };

    const options = {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            "Authorization": CONFIG.TOKEN
        },
        body: JSON.stringify(body)
    };

    fetch(URL, options)
        .then(response => response.json())
        .then(data => {
            sessionStorage.removeItem('config');
            window.location.href = './auth.html';
        })

}
const passwordField = document.getElementById('editPassword');
const confirmPasswordField = document.getElementById('confirmPassword');

function changePassword() {
    const URL = `${ACCOUNT_URL}/update/password`;
    
    const password = passwordField.value;
    const confirmPassword = confirmPasswordField.value;

    if (password != confirmPassword) {
        alert("The passwords doesn't match")
        return false;
    }

    const body = {
        "idUser": CONFIG.ID_USER,
        "password": password
    };

    const options = {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            "Authorization": CONFIG.TOKEN
        },
        body: JSON.stringify(body)
    };

    fetch(URL, options)
        .then(response => response.json())
        .then(data => {
            sessionStorage.removeItem('config');
            window.location.href = './auth.html';
        })

}

const deleteUsernameField = document.getElementById('deleteUsername');
const deletePasswordField = document.getElementById('deletePassword');

function deleteAccount() {
    const URL = `${ACCOUNT_URL}/delete-account`;

    const deleteUsername = deleteUsernameField.value;
    const deletePassword = deletePasswordField.value;

    const body = {
        "idUser": CONFIG.ID_USER,
        "username": deleteUsername,
        "password": deletePassword
    };

    const options = {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            "Authorization": CONFIG.TOKEN
        },
        body: JSON.stringify(body)
    };

    fetch(URL, options)
        .then(response => response.json())
        .then(data => {
            sessionStorage.removeItem('config');
            window.location.href = './auth.html';
        })
}