var POSTER_URL = 'https://image.tmdb.org/t/p/w92';

function load() {
    const ID_LIST = JSON.parse(localStorage.getItem('idList'));
    const OPTIONS = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': CONFIG.TOKEN
        }
    };

    getList(ID_LIST, OPTIONS);
    getContents(ID_LIST, OPTIONS);
    hideLoading();
}

async function getList(ID_LIST, OPTIONS) {
    const EXPECTED_STATUS = 200;
    const SECTION = document.querySelector('section')
    const URL = `${ROUTES.lists}/${ID_LIST}/get`;

    try {
        let data = await send(URL, OPTIONS, EXPECTED_STATUS);
        if (data !== undefined) {
            SECTION.querySelector('h1').innerText = data.listName;
            SECTION.querySelector('p').innerText = data.listDescription;
        }
    } catch (error) {
        alert(error)
    }
}

async function getContents(ID_LIST, OPTIONS) {
    const EXPECTED_STATUS = 200;
    const MAIN = document.querySelector('main');
    const URL = `${ROUTES.lists}/${ID_LIST}/get-contents`;

    try {
        let data = await send(URL, OPTIONS, EXPECTED_STATUS);

        if (data !== undefined) {
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

            localStorage.setItem('list', JSON.stringify(data));
        }
    } catch(error) {
        alert(error)
    }
}

function getPoster(posterPath) {
    if (posterPath == 'null') {
        return '../assets/images/logos/Logo-B-Symbol.svg';
    }

    return `${POSTER_URL}${posterPath}`;
}

function remove(element, ID_LIST) {
    const EXPECTED_STATUS = 204;
    const MAIN = document.querySelector('main');

    const ID_CONTENT = element.parentNode.id;
    const PARAMS = new URLSearchParams({
        idContent: ID_CONTENT
    }).toString();
    const URL = `${ROUTES.lists}/${ID_LIST}/remove?${PARAMS}`;

    const OPTIONS = {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': CONFIG.TOKEN
        }
    };

    try {
        send(URL, OPTIONS, EXPECTED_STATUS);
        MAIN.removeChild(element.parentNode);
    } catch(error) {
        alert(error)
    }
}