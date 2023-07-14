function play(elem) {
    htmx.takeClass(elem, "cur");
    document.title = elem.innerText;
}

function next() { 
    var next = htmx.find(".cur + li");
    if (next) htmx.trigger(next, "click");
}
