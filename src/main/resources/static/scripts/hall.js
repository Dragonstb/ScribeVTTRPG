document.addEventListener('DOMContentLoaded', afterLoadingHall);

function afterLoadingHall() {
    let hallSettingsButton = document.getElementById('hall-settings-btn');
    let hallCampaignsButton = document.getElementById('hall-campaigns-btn');
    let hallLogoutButton = document.getElementById('hall-logout-btn');
    let hallSettingsPanel = document.getElementById('hall-settings-panel');
    let hallCampaingsPanel = document.getElementById('hall-campaigns-panel');
    let hallLogoutPanel = document.getElementById('hall-logout-panel');

    function showHallSettingsPanel() {
        hallSettingsPanel.classList.remove('nodisplay');
        hallCampaingsPanel.classList.add('nodisplay');
        hallLogoutPanel.classList.add('nodisplay');
    }

    function showHallCampaignsPanel() {
        hallSettingsPanel.classList.add('nodisplay');
        hallCampaingsPanel.classList.remove('nodisplay');
        hallLogoutPanel.classList.add('nodisplay');
    }

    function showHallLogoutPanel() {
        hallSettingsPanel.classList.add('nodisplay');
        hallCampaingsPanel.classList.add('nodisplay');
        hallLogoutPanel.classList.remove('nodisplay');
    }

    hallSettingsButton.addEventListener('click', showHallSettingsPanel);
    hallCampaignsButton.addEventListener('click', showHallCampaignsPanel);
    hallLogoutButton.addEventListener('click', showHallLogoutPanel);
}

