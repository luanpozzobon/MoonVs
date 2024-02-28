const TABS = Array.from(document.querySelectorAll('.tab'));
const MAIN = document.querySelector('main');
const VARIABLE_SCRIPTS = ["search", "lists"];
const TEMPLATES = ["search", "lists"]

function selectTab(element) {
    const ID = element.id;
    TABS.forEach(tab => {
        tab.classList.remove('selected');
    })

    removeTemplate();
    element.classList.add('selected');
    if (TEMPLATES.includes(ID)) {
        insertTemplate(ID);
    }

    insertScript(ID);

    MAIN.id = ID;
}

function insertTemplate(id) {
    let xhr = new XMLHttpRequest();
    xhr.open('GET', `/templates/${id}.html`, true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let parser = new DOMParser();
            let doc = parser.parseFromString(xhr.responseText, 'text/html');

            MAIN.innerHTML = doc.getElementById('template').innerHTML;
        }
    };

    xhr.send();
};

function removeTemplate() {
    MAIN.innerHTML = "";
}

function insertScript(id) {
    cleanScripts();

    let script = document.createElement('script');
    script.setAttribute('src', `/scripts/${id}.js`);
    script.id = id;
    document.body.appendChild(script);
}

function cleanScripts() {
    let scripts = document.querySelectorAll('script');
    scripts.forEach(script => {
        if (VARIABLE_SCRIPTS.includes(script.id)) {
            document.body.removeChild(script);
        };
    });
}