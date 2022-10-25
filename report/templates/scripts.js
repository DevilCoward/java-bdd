/* globals window, document */
let pauseScrollActions = false;

const toggleSettingsDrawer = () => {
    document.getElementById('settingsDrawer').classList.toggle('active');
};

const toggleTagDisplay = () => {
    Array.from(document.getElementsByClassName('tags')).forEach((tagBlock) => {
        tagBlock.classList.toggle('active');
    });
};

const toggleFunctionAccordion = (element) => {
    element.classList.toggle('active');
    const icon = element.getElementsByTagName('i')[0];
    const panel = element.nextElementSibling;
    if (panel.style.maxHeight) {
        panel.style.maxHeight = null;
        icon.classList.remove('fa-angle-down');
        icon.classList.add('fa-angle-right');
    } else {
        panel.style.maxHeight = `${panel.scrollHeight}px`;
        // Close all the other panels
        Array.from(document.getElementsByClassName('feature-button')).forEach((featureButton) => {
            if (element !== featureButton) {
                featureButton.classList.remove('active');
                featureButton.nextElementSibling.style.maxHeight = null;
                const iconToClose = featureButton.getElementsByTagName('i')[0];
                iconToClose.classList.remove('fa-angle-down');
                iconToClose.classList.add('fa-angle-right');
            }
        });
        icon.classList.add('fa-angle-down');
        icon.classList.remove('fa-angle-right');
    }
};

const toggleScenarioButton = (element) => {
    // Clear all the other buttons
    Array.from(document.getElementsByClassName('scenario-button')).forEach((scenarioButton) => {
        scenarioButton.classList.remove('active');
    });
    element.classList.add('active');
};

const scrollTo = (element) => {
    // Pause the scroll actions while we jump to a new location:
    pauseScrollActions = true;
    setTimeout(() => {
        pauseScrollActions = false;
    }, (1000));
    const target = document.getElementById(element.getAttribute('scroll-to-id'));
    target.scrollIntoView({
        behavior: 'smooth',
        block: 'start',
        inline: 'end',
    });
};

const checkVisible = (elm) => {
    const rect = elm.getBoundingClientRect();
    const viewHeight = Math.max(document.documentElement.clientHeight, window.innerHeight);
    return !(rect.bottom < 0 || rect.top - viewHeight >= 0);
};

const getVisibleAnchor = () => {
    let visibleAnchor;
    Array.from(document.getElementsByClassName('anchor active')).some((anchor) => {
        if (checkVisible(anchor)) {
            visibleAnchor = anchor;
            return true;
        }
        return false;
    });
    return visibleAnchor;
};

const updateActiveScenarioWhenScrolling = () => {
    if (pauseScrollActions) return;
    const visibleAnchor = getVisibleAnchor();
    if (visibleAnchor) {
        if (visibleAnchor.getAttribute('scenario-button')) {
            const visibleScenarioButton = document.getElementById(visibleAnchor.getAttribute('scenario-button'));
            if (!visibleScenarioButton.classList.contains('active')) {
                toggleScenarioButton(visibleScenarioButton);
            }
        }
    }
};

const toggleDisplayedFeature = (element) => {
    // Deactivate all features
    Array.from(document.getElementsByClassName('feature-wrapper')).forEach((featureWrapper) => {
        featureWrapper.classList.remove('active');
    });
    // Deactivate all anchors
    Array.from(document.getElementsByClassName('anchor')).forEach((anchor) => {
        anchor.classList.remove('active');
    });
    // Activate selected feature
    const featureWrapper = document.getElementById(element.getAttribute('feature-wrapper-id'));
    featureWrapper.classList.add('active');
    Array.from(featureWrapper.querySelectorAll('.anchor')).forEach((anchor) => {
        anchor.classList.add('active');
    });
};

const filterTags = () => {
    let filterTag = document.getElementById("tagsTextField").value.toLowerCase();
    document.getElementById("sidenavTitle").innerText = filterTag;
    let isFilterApplied = !(filterTag === "");

    // Update header with tag
    let header = document.getElementById("sidenavTitle");
    if (!(filterTag.charAt(0) === '@')) {
        filterTag = '@' + filterTag;
    }
    header.innerText = isFilterApplied ? filterTag : "All Scenarios";
    header.style.fontSize = "large";

    // Make all visible to reset previous filters
    let navBarFeatureList = document.querySelectorAll('button[feature-wrapper-id]');
    navBarFeatureList.forEach(function (navBarItem) {
        navBarItem.style.display = 'inline-block';
    });
    // Iterate feature wrappers and remove if relevant tag doesn't appear
    if (isFilterApplied) {
        Array.from(document.getElementsByClassName('feature-wrapper')).forEach((featureWrapper
            ) => {
                let featureWrapperHtml = featureWrapper.innerHTML.toLowerCase();
                // Check for filter at the top of the file
                let isWholeFeatureTagged = isTagMatched(featureWrapper.firstChild.nextSibling.textContent, filterTag);
                let isTagPresent = isTagMatched(featureWrapperHtml, filterTag);
                if (!(isWholeFeatureTagged || isTagPresent)) {
                    featureWrapper.classList.remove("active");
                    document.querySelector('button[feature-wrapper-id="' + featureWrapper.id + '"]').style.display = 'none';
                }
            }
        );
    }
    filterScenarios(filterTag);
    openFirstFeature();
};

function isTagMatched(featureWrapperHtml, filterTag) {
    // Find exact tag
    const regex = new RegExp('(' + filterTag + ' )');
    const match = featureWrapperHtml.toLowerCase().match(regex);
    return match != null;
}

const resetScenarios = () => {
    // Make all visible to reset previous filters
    Array.from(document.getElementsByClassName('tags')).forEach((featureTag) => {
        let featureTagButton = featureTag.nextElementSibling;
        let featureTagScenario = featureTagButton.nextElementSibling;
        let featureTagSteps = featureTagScenario.nextElementSibling;
        let featureTagDivider = featureTagSteps.nextElementSibling;
        featureTag.style = "default";
        featureTagButton.style = "default";
        featureTagScenario.style = "default";
        featureTagSteps.style = "default";
        if (featureTagDivider != null) featureTagDivider.style = "default";
    });
};

const resetButtons = () => {
    Array.from(document.getElementsByClassName('anchor')).forEach((anchor) => {
        // Add back to buttons panel if not visible on page
        let buttonToHide = anchor.getAttribute('scenario-button');
        if (document.querySelector('button[id="' + buttonToHide + '"]') != null) {
            document.querySelector('button[id="' + buttonToHide + '"]').style = 'default';
        }
    });
};

const filterScenarios = (filterTag) => {
    // Make all visible to reset previous filters
    resetScenarios();

    // Filter scenarios from pages
    Array.from(document.getElementsByClassName('feature-wrapper')).forEach((feature) => {
        let featureTag = feature.firstChild.nextSibling.textContent;

        let isWholeFeatureTagged = isTagMatched(featureTag, filterTag);

        let scenarios = feature.getElementsByClassName('scenario-body');
        Array.from(scenarios).forEach((scenario) => {
            let scenarioName = scenario.previousElementSibling;
            let scenarioTags = scenarioName.previousElementSibling.previousElementSibling;
            let scenarioButton = scenarioTags === null ? null : scenarioTags.nextElementSibling;
            let entities = scenarioTags === null ? '' : scenarioTags.innerText;

            let isScenarioTagged = isTagMatched(entities, filterTag);

            if (scenarioName.innerText === 'Background:') {     // Background is not a scenario
                return
            }

            if (!(isScenarioTagged || isWholeFeatureTagged)) {   // Remove scenario if not tagged
                scenarioName.style.display = "none";
                scenario.style.display = "none";
                if (entities !== '') scenarioTags.style.display = "none";
                if (scenarioTags !== null) scenarioButton.style.display = "none";
            }
        });
    });

    resetButtons();
    // Filter scenarios from buttons
    Array.from(document.getElementsByClassName('anchor')).forEach((anchor) => {
        // Remove from buttons panel if not visible on page
        if (anchor.style.display === "none") {
            let buttonToHide = anchor.getAttribute('scenario-button');
            if (document.querySelector('button[id="' + buttonToHide + '"]') != null) {
                document.querySelector('button[id="' + buttonToHide + '"]').style.display = 'none';
            }
        }
    });
};

const openFirstFeature = () => {
    // Get first visible button
    const featureButtonList = document.getElementsByClassName('feature-button');
    let firstFeatureButton;
    for (let i = 0; i < featureButtonList.length; i++) {
        if (featureButtonList[i].style.display === "inline-block") {
            firstFeatureButton = featureButtonList[i];
            break;
        }
    }

    // Toggle first visible button
    if (firstFeatureButton) {
        toggleFunctionAccordion(firstFeatureButton);
        toggleDisplayedFeature(firstFeatureButton);
    }
};

const downloadCsv = () => {
    var hiddenElement = document.createElement("a");
    hiddenElement.href = 'data:text/csv;charset=utf-8,' + createCsvFile();
    hiddenElement.target = '_blank';
    hiddenElement.download = 'mobilex-global-tests.csv';
    hiddenElement.click();
};

function createCsvFile() {
    let featureCsv = "";
    Array.from(document.getElementsByClassName('scenario-body')).forEach((scenarioBody) => {
        let wholeFeatureTags = scenarioBody.parentElement.parentElement.firstChild.nextSibling.textContent;
        let feature = scenarioBody.parentElement.previousElementSibling.innerHTML.split(",").join("");
        let scenario = scenarioBody.previousElementSibling.innerHTML.split(",").join("");
        if (scenarioBody.previousElementSibling.previousElementSibling.previousElementSibling != null) {
            let tags = scenarioBody.previousElementSibling.previousElementSibling.previousElementSibling.innerHTML;
            tags = tags.split(",").join("");    // Remove any commas in names for csv formatting
            tags = tags.split('#')[0];          // Remove any comments at the end of tags
            featureCsv += feature + "," + scenario + "," + tags + wholeFeatureTags + "\n";
        }
    });
    return featureCsv;
}

const init = () => {
    // Add listeners for feature buttons
    Array.from(document.getElementsByClassName('feature-button')).forEach((featureButton) => {
        featureButton.addEventListener('click', function click() {
            toggleFunctionAccordion(this);
            toggleDisplayedFeature(this);
            scrollTo(this);
        });
    });

    // Add listeners for scenario buttons
    Array.from(document.getElementsByClassName('scenario-button')).forEach((scenarioButton) => {
        scenarioButton.addEventListener('click', function click() {
            toggleScenarioButton(this);
            scrollTo(this);
        });
    });

    // Make sure the right scenario is active when scrolling
    window.addEventListener('scroll', updateActiveScenarioWhenScrolling, true);

    // Add listeners to settings controls
    const settingsButton = document.getElementById('settingsButton');
    if (settingsButton) {
        settingsButton.addEventListener('click', toggleSettingsDrawer);
    }
    const tagsCheckbox = document.getElementById('tagsCheckbox');
    if (tagsCheckbox) {
        tagsCheckbox.addEventListener('click', toggleTagDisplay);
    }
    const tagsTextField = document.getElementById('tagsTextField');
    tagsTextField.addEventListener('onchange', filterTags);

    // Open the first feature.
    const firstFeatureButton = document.getElementsByClassName('feature-button')[0];
    if (firstFeatureButton) {
        toggleFunctionAccordion(firstFeatureButton);
        toggleDisplayedFeature(firstFeatureButton);
    }
};


init();
