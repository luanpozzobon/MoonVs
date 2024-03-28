var BASE_URL = `${config.BASE_URL}/lists`;
var POSTER_URL = 'https://image.tmdb.org/t/p/w92';

function load() {
    const ID_LIST = JSON.parse(localStorage.getItem('id'));
    const OPTIONS = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': config.TOKEN
        }
    };

    getList(ID_LIST, OPTIONS);
    getContents(ID_LIST, OPTIONS);
}

function getList(ID_LIST, OPTIONS) {
    const SECTION = document.querySelector('section')
    const URL = `${BASE_URL}/${ID_LIST}/get`;

    fetch(URL, OPTIONS)
        .then(response => {
            if (response.status !== 200) {
                throw new Error(response.headers.get('message'));
            }

            return response.json();
        })
        .then(data => {
            SECTION.querySelector('h1').innerText = data.listName;
            SECTION.querySelector('p').innerText = data.listDescription;
        })
        .catch(error => {
            alert(error);
        });
}

function getContents(ID_LIST, OPTIONS) {
    const MAIN = document.querySelector('main');
    const URL = `${BASE_URL}/${ID_LIST}/get-contents`;

    fetch(URL, OPTIONS)
        .then(response => {
            if (response.status != 200) {
                throw new Error(response.headers.get('message'));
            }

            return response.json();
        })
        .then(data => {
            localStorage.setItem('list', JSON.stringify(data));
            data.forEach(element => {
                let content = document.createElement('div');
                content.id = element.idContent;
                content.innerHTML = `<img src="${getPoster(element.content.posterPath)}">
                                     <h2>${element.content.originalTitle}</h2>
                                     <p>${element.content.overview}</p>
                                     <span onclick="remove(this, ${ID_LIST})">Remove</span>
                                    `;
                MAIN.appendChild(content);
            });
        })
}

function getPoster(posterPath) {
    if (posterPath == 'null') {
        return '../assets/images/logos/Logo-B-Symbol.svg';
    }

    return `${POSTER_URL}${posterPath}`;
}

function remove(element, ID_LIST) {
    const MAIN = document.querySelector('main');

    const ID_CONTENT = element.parentNode.id;
    const PARAMS = new URLSearchParams({
        idContent: ID_CONTENT
    }).toString();
    const URL = `${BASE_URL}/${ID_LIST}/remove?${PARAMS}`;

    const OPTIONS = {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': config.TOKEN
        }
    };

    fetch(URL, OPTIONS)
        .then(response => {
            if (response.status !== 204) {
                throw new Error(response.headers.get('message'))
            }

            MAIN.removeChild(element.parentNode);
        })
        .catch(error => {
            alert(error);
        });
}