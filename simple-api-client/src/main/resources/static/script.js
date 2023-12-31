function toggleDictUpdateMode(element) {

    let dictId = element.parentNode.parentNode.parentNode.children[0].children[0].innerText;
    let dictKey = element.parentNode.parentNode.parentNode.children[2].children[0].innerText;
    let dictVal = element.parentNode.parentNode.parentNode.children[3].children[0].innerText;

    document.getElementById("dict-form").setAttribute("action", "/index/dict_entity/" + dictId + "/" + dictKey);
    document.getElementById("dict-entity-disabled").removeAttribute("onClick");

    document.getElementById("dict-id").value = dictId;
    document.getElementById("dict-key").value = dictKey;
    document.getElementById("dict-value").value = dictVal;
    document.getElementById("dict-entity-disabled").checked = false;

    document.getElementById("dict-id").readOnly = true;
    document.getElementById("dict-key").readOnly = true;
}

function resetDictForm() {
    document.getElementById("dict-form").setAttribute("action", "/index/dict_entity/");
    document.getElementById("dict-id").getElementsByTagName("option")[0].selected = true;
    document.getElementById("dict-key").value = null;
    document.getElementById("dict-value").value = null;
    document.getElementById("dict-entity-disabled").checked = false;
    document.getElementById("dict-entity-disabled").setAttribute("onClick", "return false");

    document.getElementById("dict-id").readOnly = false;
    document.getElementById("dict-key").readOnly = false;
}

function toggleEntityUpdateMode(element) {

    let id = element.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.children[0].children[0].innerText;
    let firstDictKey = element.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.children[1].children[0].innerText;
    let secondDictKey = element.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.children[3].children[0].innerText;
    let textData = element.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.children[5].children[0].innerText;

    document.getElementById("entity-form").setAttribute("action", "/index/entity/" + id);

    document.getElementById("id").value = id;
    document.getElementById("first-dict-key").value = firstDictKey;
    document.getElementById("second-dict-key").value = secondDictKey;
    document.getElementById("some-text-data").value = textData;
}

function resetEntitiesForm() {
    document.getElementById("entity-form").setAttribute("action", "/index/entity/");
}
