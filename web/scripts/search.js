async function internalSearch() {
    const INPUT = document.getElementById('searchInput').value;
    const PARAMS = new URLSearchParams({
        searchType: 'INTERNAL',
        title: encodeURIComponent(INPUT)
    }).toString();
    const URL = `${ROUTES.content}/search?${PARAMS}`;

    const SECTION = document.getElementById('content');
    await doSearch(URL, SECTION);

    Array.from(SECTION.children).forEach(e => {
        e.addEventListener('click', function () {
            internalInfo(this)
        })
    })
    const SPAN = document.createElement('span');
    SPAN.id = "external";
    SPAN.innerText = "Didn't find what you were looking for? Try searching using TMDB!";
    SPAN.addEventListener('click', () => externalSearch(INPUT));
    SECTION.appendChild(SPAN);
}

async function externalSearch(INPUT) {
    const PARAMS = new URLSearchParams({
        searchType: 'EXTERNAL',
        title: encodeURIComponent(INPUT)
    }).toString();
    const URL = `${ROUTES.content}/search?${PARAMS}`;

    const SECTION = document.getElementById('content');
    await doSearch(URL, SECTION);
    Array.from(SECTION.children).forEach(e => {
        e.addEventListener('click', function () {
            externalInfo(this)
        })
    })
}

async function doSearch(URL, SECTION) {
    const OPTIONS = {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": CONFIG.TOKEN
        }
    };
    const EXPECTED_STATUS = 200;
    SECTION.innerHTML = "";

    showLoading();
    try {
        const DATA = await send(URL, OPTIONS, EXPECTED_STATUS);

        const POSTER_URL = 'https://image.tmdb.org/t/p/w92';
        Array.from(DATA).forEach(obj => {
            var div = document.createElement('div');
            div.innerHTML = `
                            <span class="hidden">${obj.contentType}</span>
                            <img src="${getPoster(POSTER_URL, obj.posterPath)}">
                            <h2>${obj.originalTitle}</h2>
                            <p>${obj.overview}</p>
                            <p>TMDB Rating: ${obj.voteAverage.toFixed(2)}</p>
                        `;
            div.id = obj.id;
            SECTION.appendChild(div);
        });
        hideLoading();
    } catch (error) {
        hideLoading();
        return false;
    }

}

function internalInfo(element) {
    const ID = element.id;
    const PARAMS = new URLSearchParams({
        searchType: 'INTERNAL'
    }).toString();

    openContent(ID, PARAMS);
}

function externalInfo(element) {
    const ID = element.id;
    const TYPE = element.children[0].innerText;
    const PARAMS = new URLSearchParams({
        searchType: 'EXTERNAL',
        contentType: TYPE
    }).toString();

    openContent(ID, PARAMS);
}

async function openContent(ID, PARAMS) {
    const OPTIONS = {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": CONFIG.TOKEN
        }
    };
    const URL = `${ROUTES.content}/view/${ID}?${PARAMS}`;
    const EXPECTED_STATUS = 200;

    showLoading();
    try {
        const DATA = await send(URL, OPTIONS, EXPECTED_STATUS);

        sessionStorage.setItem('content', JSON.stringify(DATA));
        window.location.href = "/pages/content.html";
    } catch (error) {
        alert(error);
    }
    hideLoading();
}