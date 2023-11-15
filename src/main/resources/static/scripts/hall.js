document.addEventListener('DOMContentLoaded', afterLoadingHall);

function afterLoadingHall() {
    let hallSettingsButton = document.getElementById('hall-settings-btn');
    let hallCampaignsButton = document.getElementById('hall-campaigns-btn');
    let hallLogoutButton = document.getElementById('hall-logout-btn');
    let hallSettingsPanel = document.getElementById('hall-settings-panel');
    let hallCampaingsPanel = document.getElementById('hall-campaigns-panel');
    let hallLogoutPanel = document.getElementById('hall-logout-panel');

    function showHallSettingsPanel() {
        setTabSelected(hallSettingsButton, true);
        setTabSelected(hallCampaignsButton, false);
        setTabSelected(hallLogoutButton, false);
        setPanelVisible(hallSettingsPanel, true);
        setPanelVisible(hallCampaingsPanel, false);
        setPanelVisible(hallLogoutPanel, false);
    }

    function showHallCampaignsPanel() {
        setTabSelected(hallSettingsButton, false);
        setTabSelected(hallCampaignsButton, true);
        setTabSelected(hallLogoutButton, false);
        setPanelVisible(hallSettingsPanel, false);
        setPanelVisible(hallCampaingsPanel, true);
        setPanelVisible(hallLogoutPanel, false);
    }

    function showHallLogoutPanel() {
        setTabSelected(hallSettingsButton, false);
        setTabSelected(hallCampaignsButton, false);
        setTabSelected(hallLogoutButton, true);
        setPanelVisible(hallSettingsPanel, false);
        setPanelVisible(hallCampaingsPanel, false);
        setPanelVisible(hallLogoutPanel, true);
    }

    /** Sets the tab as selected or unselected. Does not affects the visibility of the tab panel controlled by the tab.
     * @author Dragonstb
     * @since 0.0.3;
     * @param {element} elem Tab
     * @param {bool} selected
     */
    function setTabSelected(elem, selected) {
        if(selected) {
            elem.setAttribute('aria-selected', 'true');
        }
        else {
            elem.setAttribute('aria-selected', 'false');
        }
    }

    /** Sets a tab panel as visible or not visible.
     * @author Dragonstb
     * @since 0.0.3;
     * @param {type} elem
     * @param {type} visible
     * @returns {undefined}
     */
    function setPanelVisible(elem, visible) {
        if(visible) {
            elem.classList.remove('nodisplay');
        }
        else {
            elem.classList.add('nodisplay');
        }
    }

    hallSettingsButton.addEventListener('click', showHallSettingsPanel);
    hallCampaignsButton.addEventListener('click', showHallCampaignsPanel);
    hallLogoutButton.addEventListener('click', showHallLogoutPanel);
}

