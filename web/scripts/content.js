const BASE_URL = `${CONFIG.BASE_URL}`;

const POSTER_URL = 'https://image.tmdb.org/t/p/w185'

const CONTENT_SECTION = document.getElementById("content");
const CONTENT = JSON.parse(sessionStorage.getItem("content"));

var poster = document.createElement('img');
poster.src = POSTER_URL + CONTENT.posterPath;

var title = document.createElement('h1');
title.innerText = CONTENT.originalTitle;

var overview = document.createElement('p');
overview.innerText = CONTENT.overview;

var ratingContainer = document.createElement('div');
var tmdbRating = document.createElement('p');
tmdbRating.innerText = `Tmdb Rating: ${CONTENT.tmdbVoteAvg.toFixed(2)}`

var moonvsRating = document.createElement('p');
var userRating = document.createElement('p');
userRating.id = "userRating";
userRating.addEventListener('click', () => rating());

getRating(CONTENT.idContent)
ratingContainer.appendChild(tmdbRating);
ratingContainer.appendChild(moonvsRating);
ratingContainer.appendChild(userRating);

var genres = document.createElement('p');
genres.innerText = "Genres: ";
Array.from(CONTENT.genres).forEach(g => {
    genres.innerText += `${g} | `;
})

var addToListBtn = document.createElement('span');
addToListBtn.style.cursor = 'pointer';
addToListBtn.innerText = 'Add To List'
addToListBtn.addEventListener('click', function() {
    showLists();
});

CONTENT_SECTION.appendChild(poster);
CONTENT_SECTION.appendChild(title);
CONTENT_SECTION.appendChild(overview);
CONTENT_SECTION.appendChild(ratingContainer);
CONTENT_SECTION.appendChild(addToListBtn);
CONTENT_SECTION.appendChild(genres);

const WATCH_SECTION = document.getElementById("watch");

if (!CONTENT.watchProvider) {
    document.querySelector('main').removeChild(WATCH_SECTION);
} else {
    const BUY = CONTENT.watchProvider.buy ?? "";
    const RENT = CONTENT.watchProvider.rent ?? "";
    const STREAMING = CONTENT.watchProvider.flatrate ?? "";

    if (BUY != "") {
        var buyContainer = document.createElement('div');
        var buyTitle = document.createElement('h3');
        buyTitle.innerText = "Buy";
        buyContainer.appendChild(buyTitle);

        Array.from(BUY).forEach(p => {
            var buy = document.createElement('p');
            buy.innerText = p;
            buyContainer.appendChild(buy)
        })

        WATCH_SECTION.lastElementChild.before(buyContainer);
    }

    if (RENT != "") {
        var rentContainer = document.createElement('div');
        var rentTitle = document.createElement('h3');
        rentTitle.innerText = "Rent";
        rentContainer.appendChild(rentTitle);

        Array.from(RENT).forEach(p => {
            var rent = document.createElement('p');
            rent.innerText = p;
            rentContainer.appendChild(rent)
        })

        WATCH_SECTION.lastElementChild.before(rentContainer);
    }

    if (STREAMING != "") {
        var streamingContainer = document.createElement('div');
        var streamingTitle = document.createElement('h3');
        streamingTitle.innerText = "Streaming";
        streamingContainer.appendChild(streamingTitle);

        Array.from(STREAMING).forEach(p => {
            var streaming = document.createElement('p');
            streaming.innerText = p;
            streamingContainer.appendChild(streaming)
        })

        WATCH_SECTION.lastElementChild.before(streamingContainer);
    }
}

function getRating(idContent) {
    const URL = `${BASE_URL}/rating`;

    const options = {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": CONFIG.TOKEN
        }
    };

    fetch(`${URL}/${idContent}`, options)
        .then(response => {
            if (response.ok) {
                return response.json()
            }
            throw new Error("Network response was not ok.");
        })
        .then(data => {
            sessionStorage.setItem("userRating", JSON.stringify(data.ratingValue));
            userRating.innerText = `Your Rating: ${data.ratingValue.toFixed(2)}`;
        })
        .catch(error => {
            sessionStorage.setItem("userRating", JSON.stringify(-1));
            userRating.innerText = `Your Rating: 0.00`;
            console.error(error);
        });

    fetch(`${URL}/avg-rating/${idContent}`, options)
        .then(response => {
            if (response.ok) {
                return response.text();
            } else if (response.status === 404) {
                moonvsRating.innerText = `MoonVs Rating: 0.00`;
                return Promise.resolve();
            }

            throw new Error('Network response was not ok.');
        })
        .then(data => {
            if (data !== undefined) {
                moonvsRating.innerText = `MoonVs Rating: ${data}`;
            }
        })
        .catch(error => {
            moonvsRating.innerText = `MoonVs Rating: 0.00`
            console.error(error);
        })
}

const RATING_CONTAINER = document.getElementById('rating');
const RATING_FORM = RATING_CONTAINER.firstChild;

const RATING_VALUE = document.getElementById('ratingValue');
const COMMENTARY = document.getElementById('commentary');

function rating() {
    const URL = `${BASE_URL}/rating/${CONTENT.idContent}`;

    const USER_RATING = JSON.parse(sessionStorage.getItem('userRating'));
    if (USER_RATING !== -1) {
        const options = {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": CONFIG.TOKEN
            }
        };

        fetch(URL, options)
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
            })
            .then(data => {
                RATING_VALUE.value = data.ratingValue;
                COMMENTARY.value = data.commentary;
            })
    }
    RATING_CONTAINER.classList.remove('hidden');
}

function doRate() {
    const URL = `${BASE_URL}/rating/rate/${CONTENT.idContent}`;

    var ratingValue = RATING_VALUE.value;
    var commentary = COMMENTARY.value;

    if (ratingValue < 0 || ratingValue > 10) {
        alert("The rating value must be between 0.0 and 10.0");
        return false;
    }

    const body = {
        "ratingValue": ratingValue,
        "commentary": commentary
    };

    const options = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": CONFIG.TOKEN
        },
        body: JSON.stringify(body)
    };

    fetch(URL, options)
        .then(() => {
            window.location.href = "./content.html";
        })
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
    const DIV = document.createElement('div');

    try {
        let data = await send(URL, OPTIONS, 200);
        if (data !== undefined) {
            Array.from(data).forEach(list => {
                let listContainer = document.createElement('div');
                listContainer.id = list.idList;
                listContainer.innerText = list.listName;
                listContainer.addEventListener('click', function() {
                    addToList(this);
                })
                DIV.appendChild(listContainer);
            })
            CONTAINER.appendChild(DIV)
        }
    } catch (error) {
        alert(error);
    }
}

function showLists() {
    const LISTS_CONTAINER = document.getElementById('list');
    LISTS_CONTAINER.classList.remove('hidden');
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