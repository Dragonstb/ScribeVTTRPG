document.addEventListener('DOMContentLoaded', afterLoadingGame);

// class names
const NODISPLAY = "nodisplay";

function afterLoadingGame() {
    let mainMenuBtn = document.getElementById('main-menu-btn');
    let mainMenuPanel = document.getElementById('main-menu-panel');
    let letThemInBtn = document.getElementById('let-them-in-btn');
    let letThemInPanel = document.getElementById('let-them-in-panel');

    function toggleMainMenuVisibility() {
        let wasVisible = !mainMenuPanel.classList.contains(NODISPLAY);
        mainMenuPanel.classList.toggle(NODISPLAY);

        if( wasVisible ) {
            hideLetThemInPanel();
        }
    }

    function toggleLetThemInVisibility() {
        letThemInPanel.classList.toggle(NODISPLAY);
    }

    function hideLetThemInPanel() {
        letThemInPanel.classList.add(NODISPLAY);
    }

    mainMenuBtn.addEventListener('click', toggleMainMenuVisibility);
    letThemInBtn.addEventListener('click', toggleLetThemInVisibility);
}

