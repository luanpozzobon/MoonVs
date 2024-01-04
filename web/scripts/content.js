const BASE_URL = 'https://moonvs.fly.dev/'
const POSTER_URL = 'https://image.tmdb.org/t/p/w185'
const SESSION = JSON.parse(sessionStorage.getItem('session'));
const TOKEN = SESSION.token;

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
    genres.innerText += `${g}  `;
})

CONTENT_SECTION.appendChild(poster);
CONTENT_SECTION.appendChild(title);
CONTENT_SECTION.appendChild(overview);
CONTENT_SECTION.appendChild(ratingContainer);
CONTENT_SECTION.appendChild(genres);

const WATCH_SECTION = document.getElementById("watch");

const BUY = CONTENT.watchProvider.buy;
const RENT = CONTENT.watchProvider.rent;
const STREAMING = CONTENT.watchProvider.flatrate;

var buyContainer = document.createElement('div');
var buyTitle = document.createElement('h3');
buyTitle.innerText = "Buy";
buyContainer.appendChild(buyTitle);

Array.from(BUY).forEach(p => {
    var buy = document.createElement('p');
    buy.innerText = p;
    buyContainer.appendChild(buy)
})

var rentContainer = document.createElement('div');
var rentTitle = document.createElement('h3');
rentTitle.innerText = "Rent";
rentContainer.appendChild(rentTitle);

Array.from(RENT).forEach(p => {
    var rent = document.createElement('p');
    rent.innerText = p;
    rentContainer.appendChild(rent)
})

var streamingContainer = document.createElement('div');
var streamingTitle = document.createElement('h3');
streamingTitle.innerText = "Streaming";
streamingContainer.appendChild(streamingTitle);

Array.from(STREAMING).forEach(p => {
    var streaming = document.createElement('p');
    streaming.innerText = p;
    streamingContainer.appendChild(streaming)
})

WATCH_SECTION.lastElementChild.before(buyContainer);
WATCH_SECTION.lastElementChild.before(rentContainer);
WATCH_SECTION.lastElementChild.before(streamingContainer);


function getRating(idContent) {
    const URL = BASE_URL + "rating/";

    const options = {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${TOKEN}`
        }
    };

    fetch(URL + idContent, options)
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

    fetch(URL + `avg-rating/${idContent}`, options)
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
    const URL = BASE_URL + 'rating/';
    const USER_RATING = JSON.parse(sessionStorage.getItem('userRating'));
    if (USER_RATING !== -1) {
        const options = {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${TOKEN}`
            }
        };

        fetch(URL + CONTENT.idContent, options)
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
    const URL = BASE_URL + 'rating/'
    var ratingValue = RATING_VALUE.value;
    var commentary = COMMENTARY.value;

    if (ratingValue < 0 || ratingValue > 10) {
        alert("The rating value must be between 0.0 and 10.0");
        return false;
    }

    const body = {
        "idContent": CONTENT.idContent,
        "ratingValue": ratingValue,
        "commentary": commentary
    };

    const options = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${TOKEN}`
        },
        body: JSON.stringify(body)
    };

    fetch(URL + "rate", options)
        .then(() => {
            window.location.href = "./content.html";
        })
}