var BASE_URL = `${config.BASE_URL}/lists`;

var getLists = function getLists() {
    const OPTIONS = {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": config.TOKEN
        }
    };

    const PARAMS = new URLSearchParams({
        idUser: config.ID_USER
    }).toString();
    const URL = `${BASE_URL}/get?${PARAMS}`;

    fetch(URL, OPTIONS)
        .then(response => response.json())
        .then(data => {
            Array.from(data).forEach(list => {
                var listContainer = document.createElement('div');
                listContainer.id = list.idList;
                listContainer.innerHTML = `<h2>${list.listName}</h2>
                                           <p>${list.listDescription ?? "Empty Description"}</p>`;
                listContainer.addEventListener("click", function() {
                    openList(this)
                })

                MAIN.appendChild(listContainer);
            });
        })
        .catch(error => {
            alert(error.message);
        });
}();

function newList() {
    const FORM = document.getElementById('new-list-form');
    if (FORM !== null) {
        removeForm();
        return;
    }

    const BUTTON = document.getElementById('new-list-button');
    let xhr = new XMLHttpRequest();
    xhr.open('GET', `/templates/lists.html`, true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let parser = new DOMParser();
            let doc = parser.parseFromString(xhr.responseText, 'text/html');

            let template = document.createElement('div');
            template.id = 'new-list-div';
            template.innerHTML = doc.getElementById('new-list').innerHTML;
            BUTTON.after(template);

            addButtonAnimation();
        }
    };

    xhr.send();
}

function addButtonAnimation() {
    const CREATE_LIST_BUTTON = document.getElementById('create-list-button');

    CREATE_LIST_BUTTON.addEventListener("mousemove", function (event) {
        const mouseX = event.clientX - CREATE_LIST_BUTTON.getBoundingClientRect().left;
        const mouseY = event.clientY - CREATE_LIST_BUTTON.getBoundingClientRect().top;

        CREATE_LIST_BUTTON.style.background = `radial-gradient(circle at ${mouseX}px ${mouseY}px, 
                                                               var(--comp-color1) 0%,
                                                               var(--bg-color) 37.5%,
                                                               var(--bg-color) 100%)`;
    })

    CREATE_LIST_BUTTON.addEventListener("mouseleave", () => {
        CREATE_LIST_BUTTON.style.background = 'var(--bg-color)';
    })
}

async function createList() {
    const URL = `${BASE_URL}/create`;

    const LIST_NAME = document.getElementById('new-list-name').value;
    const LIST_DESCRIPTION = document.getElementById('new-list-description').value;
    const BODY = {
        idUser: config.ID_USER,
        listName: LIST_NAME,
        listDescription: LIST_DESCRIPTION
    };

    const OPTIONS = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': config.TOKEN
        },
        body: JSON.stringify(BODY)
    };

    data = await send(URL, OPTIONS);
    if (data !== undefined) {
        let list = document.createElement('div');
        list.id = data.idList;
        list.innerHTML = `<h2>${data.listName}</h2>
                        <p>${data.listDescription}</p>`;
        list.addEventListener("click", function() {
            openList(this)
        })

        MAIN.appendChild(list);
        removeForm();
    }
}

async function send(URL, OPTIONS) {
    let response;
    await fetch(URL, OPTIONS)
        .then(response => {
            if (response.status < 200 || response.status > 299) {
                throw new Error(response.headers.get('message'));
            }

            return response.json();
        })
        .then(data => {
            response = data;
        })
        .catch(error => {
            alert(error);
        });

    return response
}

function openList(element) {
    const ID = element.id;
    localStorage.setItem('id', ID);

    window.location.href = './pages/list_content.html';
}

function addContent(content) {
    let content_container = document.createElement('div');
    content_container.innerHTML = `<h2>${content.originalTitle}</h2>
                                   <p>${content.overview}</p>`
}

function removeForm() {
    const FORM = document.getElementById('new-list-div');

    MAIN.removeChild(FORM);
}