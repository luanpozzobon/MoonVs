const BASE_URL = 'https://moonvs.fly.dev/content'
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

var rating = document.createElement('p');
rating.innerText = `Tmdb Rating: ${CONTENT.tmdbVoteAvg.toFixed(2)}`

var genres = document.createElement('p');
genres.innerText = "Genres: ";
Array.from(CONTENT.genres).forEach(g => {
    genres.innerText += `${g}  `;
})

CONTENT_SECTION.appendChild(poster);
CONTENT_SECTION.appendChild(title);
CONTENT_SECTION.appendChild(overview);
CONTENT_SECTION.appendChild(rating);
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
buyTitle.innerText = "Rent";
buyContainer.appendChild(rentTitle);

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

WATCH_SECTION.appendChild(buyContainer);
WATCH_SECTION.appendChild(rentContainer);
WATCH_SECTION.appendChild(streamingContainer);