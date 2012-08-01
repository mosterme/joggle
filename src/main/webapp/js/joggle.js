/* $Id$ */
var albums, artists; $().ready(go());

function go() {
	$.ajax({ dataType: "json", url: "./search/artist/", success: function(data){ artists = data.d; browse('artist') } });
	$.ajax({ dataType: "json", url: "./search/album/",  success: function(data){ albums = data.d } });
	$('#bralb').click(function(){browse('album')});
	$('#brart').click(function(){browse('artist')});
	$('#clean').click(function(){$('.cur').parent().prev().prevAll().remove()});
	$('#clear').click(function(){$('.cur').parent().prevAll().remove(); $('.cur').parent().nextAll().remove()});
	$('#back').click(function(){beh()});
	$('#stop').click(function(){peh()});
	$('#next').click(function(){neh()});
}

// Z Previous Track // X Play/Restart/Unpause // C Pause/Unpause // V Stop // B Next Track

function beh() { $('.cur').parent().prev().children().first().click() }
function neh() { $('.cur').parent().next().children().first().click() }
function peh() { var p = document.getElementById('player'); p.paused ? p.play() : p.pause() }

function queue(what, keyword) {
	$.ajax({ dataType: "json", url: "./search/" + what + "/" + keyword, success: function(data){ $('#plt').tmpl(data.d).appendTo('#playlist') } });
}

function browse(what) {
	$('#browser').empty(); $('#browser-nav').empty();
	if (what == 'album')
	{ 
		$('#talb').tmpl(albums).appendTo('#browser'); $('#bralb').hide(); $('#brart').show();
	}
	else
	{ 
		$('#tart').tmpl(artists).appendTo('#browser'); $('#brart').hide(); $('#bralb').show();
	}
	$('#browser').listnav({includeAll: false, noMatchText: "nothing", showCounts: false});
	$('.ln-letters a').removeAttr('href');
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
	document.title = "✰ " + unescape(title) + " ✰ " + unescape(artist) + " ✰";
}