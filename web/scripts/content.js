const POSTER_URL = 'https://image.tmdb.org/t/p/w185'
const CONTENT = JSON.parse(sessionStorage.getItem("content"));

function load() {
    document.title = CONTENT.originalTitle;
    
    fillContentSection(CONTENT);
    fillWatchSection(CONTENT);
    getLists();
    hideLoading();
}

function fillContentSection(CONTENT) {
    const POSTER_CONTAINER = document.getElementById('poster');
    const POSTER = getPoster(POSTER_URL, CONTENT.posterPath);
    POSTER_CONTAINER.src = POSTER;

    const TITLE = document.getElementById('title');
    TITLE.innerText = CONTENT.originalTitle;

    const OVERVIEW = document.getElementById('overview');
    OVERVIEW.innerText = CONTENT.overview;

    const TMDB_RATING = document.getElementById('tmdbRating');
    TMDB_RATING.innerText += ` ${CONTENT.tmdbVoteAvg.toFixed(2)}`;

    getRating(CONTENT.idContent)

    const GENRES = document.getElementById('genres');
    const GENRES_ARRAY = Array.from(CONTENT.genres);
    GENRES_ARRAY.forEach((genre, index) => {
        if (index !== GENRES_ARRAY.length - 1) {
            GENRES.innerText += ` ${genre} |`;
        } else {
            GENRES.innerText += ` ${genre}`;
        }
    })
}

function getRating(ID_CONTENT) {
    const OPTIONS = {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": CONFIG.TOKEN
        }
    };

    getUserRating(ID_CONTENT, OPTIONS);
    getAvgRating(ID_CONTENT, OPTIONS);

}

async function getUserRating(ID_CONTENT, OPTIONS) {
    const URL = `${ROUTES.rating}/${ID_CONTENT}`
    const EXPECTED_STATUS = 200;

    try {
        let data = await send(URL, OPTIONS, EXPECTED_STATUS);

        sessionStorage.setItem("userRating", JSON.stringify(data));
        userRating.innerText += ` ${data.ratingValue.toFixed(2)}`;
    } catch (error) {
        sessionStorage.setItem("userRating", JSON.stringify({ratingValue : -1}));
        userRating.innerText = `Your Rating: 0.00`;
    }
}

async function getAvgRating(ID_CONTENT, OPTIONS) {
    const URL = `${ROUTES.rating}/avg-rating/${ID_CONTENT}`;
    const EXPECTED_STATUS = 200;

    try {
        let data = await send(URL, OPTIONS, EXPECTED_STATUS);

        moonvsRating.innerText += ` ${data.toFixed(2)}`;
    } catch (error) {
        moonvsRating.innerText += ' 0.00'
    }
}

function fillWatchSection(CONTENT) {
    const WATCH_SECTION = document.getElementById('watch');
    const WATCH_PROVIDERS = CONTENT.watchProvider ?? null;

    if (!WATCH_PROVIDERS) {
        document.querySelector('main').removeChild(WATCH_SECTION)
    } else {
        const BUY_PROVIDERS = WATCH_PROVIDERS.buy;
        const RENT_PROVIDERS = WATCH_PROVIDERS.rent;
        const STREAMING_PROVIDERS = WATCH_PROVIDERS.flatrate;

        fillProviders(BUY_PROVIDERS, "Buy");
        fillProviders(RENT_PROVIDERS, "Rent");
        fillProviders(STREAMING_PROVIDERS, "Streaming");

    }
}

function fillProviders(PROVIDERS, PROVIDERS_TITLE) {
    if (PROVIDERS == "") return;

    const PROVIDERS_ARRAY = Array.from(PROVIDERS);
    const CONTAINER = document.createElement('div');
    const TITLE = document.createElement('h3');
    const WATCH_SECTION = document.getElementById("watch");

    TITLE.innerText = PROVIDERS_TITLE
    CONTAINER.appendChild(TITLE);
    PROVIDERS_ARRAY.forEach(p => {
        let provider = document.createElement('p');
        provider.innerText = p;
        CONTAINER.appendChild(provider);
    })

    WATCH_SECTION.lastElementChild.before(CONTAINER);
}

const RATING_CONTAINER = document.getElementById('rating');

const RATING_VALUE = document.getElementById('ratingValue');
const COMMENTARY = document.getElementById('commentary');

async function rating() {
    const USER_RATING = JSON.parse(sessionStorage.getItem('userRating'));
    if (USER_RATING.ratingValue !== -1) {
        RATING_VALUE.value = USER_RATING.ratingValue;
        COMMENTARY.value = USER_RATING.commentary;
    }

    RATING_CONTAINER.classList.remove('hidden');
}

async function doRate() {
    const URL = `${ROUTES.rating}/rate/${CONTENT.idContent}`

    let ratingValue = RATING_VALUE.value;
    if (ratingValue < 0 || ratingValue > 10) {
        alert("The rating value must be between 0.0 and 10.0");
        return false;
    }

    let commentary = COMMENTARY.value;

    const BODY = {
        "ratingValue": ratingValue,
        "commentary": commentary
    };

    const OPTIONS = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": CONFIG.TOKEN
        },
        body: JSON.stringify(BODY)
    };
    const EXPECTED_STATUS = 200;

    try {
        await send(URL, OPTIONS, EXPECTED_STATUS);

        window.location.href = "./content.html";
    } catch (error) {
        alert (error);
    }
}

async function getLists() {
    const PARAMS = new URLSearchParams({
        idUser: CONFIG.ID_USER
    }).toString();
    const URL = `${ROUTES.lists}/get?${PARAMS}`;
    const OPTIONS = {
        method: 'GET',
        headers: {
            "Content-Type": "application/json",
            "Authorization": CONFIG.TOKEN
        }
    }
    const CONTAINER = document.getElementById('list');

    try {
        let data = await send(URL, OPTIONS, 200);
        if (data !== undefined) {
            Array.from(data).forEach(list => {
                let listContainer = document.createElement('div');
                listContainer.id = list.idList;
                listContainer.innerText = list.listName;
                listContainer.addEventListener('click', function () {
                    addToList(this);
                })

                CONTAINER.appendChild(listContainer)
            })
        }
    } catch (error) {
        alert(error);
    }
}

function showLists() {
    const LISTS_CONTAINER = document.getElementById('list');
    if (LISTS_CONTAINER.classList.contains('hidden')) {
        LISTS_CONTAINER.classList.remove('hidden');
        LISTS_CONTAINER.classList.add('visible');
    } else {
        LISTS_CONTAINER.classList.add('hidden');
        LISTS_CONTAINER.classList.remove('visible');
    }
}

async function addToList(element) {
    const ID = element.id;
    const PARAMS = new URLSearchParams({
        idContent: CONTENT.idContent
    }).toString();
    const URL = `${ROUTES.lists}/${ID}/add?${PARAMS}`;
    const OPTIONS = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": CONFIG.TOKEN
        }
    }

    try {
        const data = await send(URL, OPTIONS, 201);
        window.location.href = `/pages/content.html`;
    } catch (error) {
        alert(error);
    }
}