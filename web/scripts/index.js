const TABS = Array.from(document.querySelectorAll('.tab'));
const MAIN = document.querySelector('main');

function selectTab(element) {
    TABS.forEach(tab => {
        tab.classList.remove('selected');
    })

    element.classList.add('selected');
    insertTemplate(element.id);
}

function insertTemplate(id) {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', `/templates/${id}.html`, true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var parser = new DOMParser();
            var doc = parser.parseFromString(xhr.responseText, 'text/html');

            MAIN.innerHTML = doc.getElementById('template').innerHTML;
        }
    };

    xhr.send();
    var script = document.createElement('script');
    script.setAttribute('src', `/scripts/${id}.js`);
    document.body.appendChild(script);
};