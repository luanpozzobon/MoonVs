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
                                           <p>${list.listDescription}</p>`;

                MAIN.appendChild(listContainer);
            });
        })
        .catch(error => {
            alert(error.message);
        });
};