const TABS = Array.from(document.querySelectorAll('.tab'));

function selectTab(element) {
    TABS.forEach(tab => {
        tab.classList.remove('selected');
    })

    element.classList.add('selected')
}