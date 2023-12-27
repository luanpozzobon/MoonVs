const BASE_URL = 'https://moonvs.fly.dev/content'
const POSTER_URL = 'https://image.tmdb.org/t/p/w92'
const SESSION = JSON.parse(sessionStorage.getItem('session'));
const TOKEN = SESSION.token;

const input = document.getElementById('searchInput');
const searchButton = document.getElementById('searchButton');
const main = document.querySelector('main');

async function internalSearch() {
    const URL = BASE_URL + '/internal-search?title=';

    await doSearch(URL);

    Array.from(main.children).forEach(e => {
        e.addEventListener('click', function() {
            internalInfo(this)
        })
    })
    var span = document.createElement('span');
    span.id = "external";
    span.innerText = "Didn't find what you were looking for? Try searching using TMDB!";
    span.addEventListener('click', () => externalSearch());
    main.appendChild(span);
}

async function externalSearch() {
    const URL = BASE_URL + '/external-search?title=';
    await doSearch(URL);
    Array.from(main.children).forEach(e => {
        e.addEventListener('click', function() {
            externalInfo(this)
        })
    })
}

async function doSearch(URL) {
    const value = input.value.replace(' ', '%20');
    const options = {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${TOKEN}`
        }
    };

    main.innerHTML = "";

    await fetch(URL + value, options)
        .then(response => response.json())
        .then(data => {
            Array.from(data).forEach(function (obj) {
                var div = document.createElement('div');
                div.innerHTML = `
                                <span class="hidden">${obj.id}</span>
                                <span class="hidden">${obj.contentType}</span>
                                <img src="${POSTER_URL}${obj.posterPath}">
                                <h2>${obj.originalTitle}</h2>
                                <p>${obj.overview}</p>
                                <p>TMDB Rating: ${obj.voteAverage.toFixed(2)}</p>
                            `;
                main.appendChild(div);
            });
        })
        .catch(error => {
            return false;
        }) 
}

function internalInfo(element) {
    const ID = element.children[0].innerText;
    const URL = BASE_URL + '/internal/' + ID;
    
    const options = {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + TOKEN
        }
    }

    fetch(URL, options)
    .then(response => response.json())
    .then(data => {
        sessionStorage.setItem('content', JSON.stringify(data));
        window.location.href = "./pages/content.html"
    })
}

function externalInfo(element) {
    const ID = element.children[0].innerText;
    const TYPE = element.children[1].innerText;
    const URL = BASE_URL + '/external?id=' + ID + '&contentType=' + TYPE;


    const options = {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${TOKEN}`
        }
    };

    fetch(URL, options)
    .then(response => response.json())
    .then(data => {
        sessionStorage.setItem('content', JSON.stringify(data));
        window.location.href = "./pages/content.html";
    })
}

