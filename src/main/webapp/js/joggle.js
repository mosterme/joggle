$().ready(browse('artist'));
$('#clean').click(function(){$('.cur').parent().prev().prevAll().remove()});
$('#clear').click(function(){$('.cur').parent().prevAll().remove(); $('.cur').parent().nextAll().remove()});
$('#back').click(function(){beh()});
$('#stop').click(function(){peh()});
$('#next').click(function(){neh()});
$('#brart').click(function(){browse('artist')});
$('#bralb').click(function(){browse('album')});

function beh() { $('.cur').parent().prev().children().first().click() }
function neh() { $('.cur').parent().next().children().first().click() }

function peh() {
	var p = document.getElementById('player'); p.paused ? p.play() : p.pause() 
	var alt = $('#stop').attr('alt'); var src = $('#stop').attr('src');
	$('#stop').attr('alt', src); $('#stop').attr('src', alt);
}

function queue(what, keyword) {
	$.ajax({ dataType: "jsonp", jsonp: "$callback", url: "./search/" + what + "/" + keyword, success: function(data){ prender(data.d) } });
}

function browse(what) {
	$.ajax({ dataType: "jsonp", jsonp: "$callback", url: "./search/" + what + "/", success: function(data){ brender(what, data.d) } });
}

function brender(what, data) {
	var t = (what == 'album') ? '#talb' : '#tart';
	$('#browser').empty(); $('#browser-nav').empty();
	$(t).tmpl(data).appendTo('#browser');
	$('#browser').listnav({includeAll: false, noMatchText: "nothing", showCounts: false});
	$('.ln-letters a').removeAttr('href');
}

function prender(data) {
	$('#plt').tmpl(data).appendTo('#playlist');
}

function play(id, artist, album, title, track) {
	$('.cur').removeClass('cur');
	$('#'+id).addClass('cur');
	$('#player').attr('src','./stream/' + id);
	$('#player').attr('autoplay','autoplay');
	$('#artist').text(unescape(artist));
	$('#album').text(unescape(album));
	$('#title').text(unescape(title));
	$('#cover').attr('src','./image/' + id);
	document.title = "✰ " + unescape(title) + " ✰ " + unescape(artist) + " ✰"
}
