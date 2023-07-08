function play(id, artist, album, title, track, elem) {
    var old = htmx.find(".cur");
    if (old) htmx.removeClass(old, "cur");
    htmx.addClass(elem, "cur");
    document.title = title + " - " + artist;
}

function next() { 
    var next = htmx.find(".cur + li");
    if (next) htmx.trigger(next, "click");
}
