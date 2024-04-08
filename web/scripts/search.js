var BASE_URL = `${CONFIG.BASE_URL}/content`;

var POSTER_URL = 'https://image.tmdb.org/t/p/w92'

var searchButton = document.getElementById('searchButton');

async function internalSearch() {
    var input = document.getElementById('searchInput');
    var SECTION = document.getElementById('content');
    const PARAMS = new URLSearchParams({
        searchType: 'INTERNAL',
        title: encodeURIComponent(input.value)
    }).toString();
    const URL = `${BASE_URL}/search?${PARAMS}`;

    await doSearch(URL);

    Array.from(SECTION.children).forEach(e => {
        e.addEventListener('click', function() {
            internalInfo(this)
        })
    })
    var span = document.createElement('span');
    span.id = "external";
    span.innerText = "Didn't find what you were looking for? Try searching using TMDB!";
    span.addEventListener('click', () => externalSearch());
    SECTION.appendChild(span);
}

async function externalSearch() {
    var input = document.getElementById('searchInput');
    var SECTION = document.getElementById('content');
    const PARAMS = new URLSearchParams({
        searchType: 'EXTERNAL',
        title: encodeURIComponent(input.value)
    }).toString();
    const URL = `${BASE_URL}/search?${PARAMS}`;

    await doSearch(URL);
    Array.from(SECTION.children).forEach(e => {
        e.addEventListener('click', function() {
            externalInfo(this)
        })
    })
}

async function doSearch(URL) {
    var SECTION = document.getElementById('content');
    const OPTIONS = {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": CONFIG.TOKEN
        }
    };
    const EXPECTED_STATUS = 200;
    SECTION.innerHTML = "";

    try {
        let data = await send(URL, OPTIONS, EXPECTED_STATUS);

        Array.from(data).forEach(function (obj) {
            var div = document.createElement('div');
            div.innerHTML = `
                            <span class="hidden">${obj.contentType}</span>
                            <img src="${getPoster(obj.posterPath)}">
                            <h2>${obj.originalTitle}</h2>
                            <p>${obj.overview}</p>
                            <p>TMDB Rating: ${obj.voteAverage.toFixed(2)}</p>
                        `;
            div.id = obj.id;
            SECTION.appendChild(div);
        });
    } catch (error) {
        return false;
    }
}

function getPoster(posterPath) {
    if (posterPath == 'null') {
        return '../assets/images/logos/Logo-B-Symbol.svg';
    }

    return `${POSTER_URL}${posterPath}`;
}

function internalInfo(element) {
    const ID = element.id;
    const PARAMS = new URLSearchParams({
        searchType: 'INTERNAL'
    }).toString();

    const URL = `${BASE_URL}/view/${ID}?${PARAMS}`;
    
    const OPTIONS = {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": CONFIG.TOKEN
        }
    }

    fetch(URL, OPTIONS)
    .then(response => response.json())
    .then(data => {
        sessionStorage.setItem('content', JSON.stringify(data));
        window.location.href = "./pages/content.html"
    })
}

function externalInfo(element) {
    const ID = element.id;
    const TYPE = element.children[0].innerText;
    const PARAMS = new URLSearchParams({
        searchType: 'EXTERNAL',
        contentType: TYPE
    }).toString();

    const URL = `${BASE_URL}/view/${ID}?${PARAMS}`;


    const options = {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": CONFIG.TOKEN
        }
    };

    fetch(URL, options)
    .then(response => response.json())
    .then(data => {
        sessionStorage.setItem('content', JSON.stringify(data));
        window.location.href = "./pages/content.html";
    })
}

