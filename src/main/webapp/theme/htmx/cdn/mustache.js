!function(t,e){"object"==typeof exports&&"undefined"!=typeof module?module.exports=e():"function"==typeof define&&define.amd?define(e):(t=t||self).Mustache=e()}(this,function(){"use strict"; var t=Object.prototype.toString,e=Array.isArray||function e(n){return"[object Array]"===t.call(n)};function n(t){return"function"==typeof t}function r(t){return t.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g,"\\$&")}function i(t,e){return null!=t&&"object"==typeof t&&e in t}function o(t,e){return null!=t&&"object"!=typeof t&&t.hasOwnProperty&&t.hasOwnProperty(e)}var a=RegExp.prototype.test,s=/\S/;function c(t){var e,n;return e=s,n=t,!a.call(e,n)}var p={"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;","/":"&#x2F;","`":"&#x60;","=":"&#x3D;"},u=/\s*/,h=/\s+/,l=/\s*=/,f=/\s*\}/,d=/#|\^|\/|>|\{|&|=|!/;function g(t){this.string=t,this.tail=t,this.pos=0}function v(t,e){this.view=t,this.cache={".":this.view},this.parent=e}function $(){this.templateCache={_cache:{},set:function t(e,n){this._cache[e]=n},get:function t(e){return this._cache[e]},clear:function t(){this._cache={}}}}g.prototype.eos=function t(){return""===this.tail},g.prototype.scan=function t(e){var n=this.tail.match(e);if(!n||0!==n.index)return"";var r=n[0];return this.tail=this.tail.substring(r.length),this.pos+=r.length,r},g.prototype.scanUntil=function t(e){var n,r=this.tail.search(e);switch(r){case -1:n=this.tail,this.tail="";break;case 0:n="";break;default:n=this.tail.substring(0,r),this.tail=this.tail.substring(r)}return this.pos+=n.length,n},v.prototype.push=function t(e){return new v(e,this)},v.prototype.lookup=function t(e){var r=this.cache;if(r.hasOwnProperty(e))a=r[e];else{for(var a,s,c,p,u=this,h=!1;u;){if(e.indexOf(".")>0)for(s=u.view,c=e.split("."),p=0;null!=s&&p<c.length;)p===c.length-1&&(h=i(s,c[p])||o(s,c[p])),s=s[c[p++]];else s=u.view[e],h=i(u.view,e);if(h){a=s;break}u=u.parent}r[e]=a}return n(a)&&(a=a.call(this.view)),a},$.prototype.clearCache=function t(){void 0!==this.templateCache&&this.templateCache.clear()},$.prototype.parse=function t(n,i){var o=this.templateCache,a=n+":"+(i||y.tags).join(":"),s=void 0!==o,p=s?o.get(a):void 0;return void 0==p&&(p=function t(n,i){if(!n)return[];var o,a,s,p,v,$,w,_,m,b=!1,C=[],k=[],x=[],T=!1,U=!1,j="",P=0;function S(){if(T&&!U)for(;x.length;)delete k[x.pop()];else x=[];T=!1,U=!1}function V(t){if("string"==typeof t&&(t=t.split(h,2)),!e(t)||2!==t.length)throw Error("Invalid tags: "+t);o=RegExp(r(t[0])+"\\s*"),a=RegExp("\\s*"+r(t[1])),s=RegExp("\\s*"+r("}"+t[1]))}V(i||y.tags);for(var I=new g(n);!I.eos();){if(p=I.pos,$=I.scanUntil(o))for(var O=0,A=$.length;O<A;++O)c(w=$.charAt(O))?(x.push(k.length),j+=w):(U=!0,b=!0,j+=" "),k.push(["text",w,p,p+1]),p+=1,"\n"===w&&(S(),j="",P=0,b=!1);if(!I.scan(o))break;if(T=!0,v=I.scan(d)||"name",I.scan(u),"="===v?($=I.scanUntil(l),I.scan(l),I.scanUntil(a)):"{"===v?($=I.scanUntil(s),I.scan(f),I.scanUntil(a),v="&"):$=I.scanUntil(a),!I.scan(a))throw Error("Unclosed tag at "+I.pos);if(_=">"==v?[v,$,p,I.pos,j,P,b]:[v,$,p,I.pos],P++,k.push(_),"#"===v||"^"===v)C.push(_);else if("/"===v){if(!(m=C.pop()))throw Error('Unopened section "'+$+'" at '+p);if(m[1]!==$)throw Error('Unclosed section "'+m[1]+'" at '+p)}else"name"===v||"{"===v||"&"===v?U=!0:"="===v&&V($)}if(S(),m=C.pop())throw Error('Unclosed section "'+m[1]+'" at '+I.pos);return function t(e){for(var n,r,i=[],o=i,a=[],s=0,c=e.length;s<c;++s)switch((n=e[s])[0]){case"#":case"^":o.push(n),a.push(n),o=n[4]=[];break;case"/":(r=a.pop())[5]=n[2],o=a.length>0?a[a.length-1][4]:i;break;default:o.push(n)}return i}(function t(e){for(var n,r,i=[],o=0,a=e.length;o<a;++o)(n=e[o])&&("text"===n[0]&&r&&"text"===r[0]?(r[1]+=n[1],r[3]=n[3]):(i.push(n),r=n));return i}(k))}(n,i),s&&o.set(a,p)),p},$.prototype.render=function t(e,n,r,i){var o=this.getConfigTags(i),a=this.parse(e,o),s=n instanceof v?n:new v(n,void 0);return this.renderTokens(a,s,r,e,i)},$.prototype.renderTokens=function t(e,n,r,i,o){for(var a,s,c,p="",u=0,h=e.length;u<h;++u)c=void 0,"#"===(s=(a=e[u])[0])?c=this.renderSection(a,n,r,i,o):"^"===s?c=this.renderInverted(a,n,r,i,o):">"===s?c=this.renderPartial(a,n,r,o):"&"===s?c=this.unescapedValue(a,n):"name"===s?c=this.escapedValue(a,n,o):"text"===s&&(c=this.rawValue(a)),void 0!==c&&(p+=c);return p},$.prototype.renderSection=function t(r,i,o,a,s){var c=this,p="",u=i.lookup(r[1]);if(u){if(e(u))for(var h=0,l=u.length;h<l;++h)p+=this.renderTokens(r[4],i.push(u[h]),o,a,s);else if("object"==typeof u||"string"==typeof u||"number"==typeof u)p+=this.renderTokens(r[4],i.push(u),o,a,s);else if(n(u)){if("string"!=typeof a)throw Error("Cannot use higher-order sections without the original template");null!=(u=u.call(i.view,a.slice(r[3],r[5]),function t(e){return c.render(e,i,o,s)}))&&(p+=u)}else p+=this.renderTokens(r[4],i,o,a,s);return p}},$.prototype.renderInverted=function t(n,r,i,o,a){var s=r.lookup(n[1]);if(!s||e(s)&&0===s.length)return this.renderTokens(n[4],r,i,o,a)},$.prototype.indentPartial=function t(e,n,r){for(var i=n.replace(/[^ \t]/g,""),o=e.split("\n"),a=0;a<o.length;a++)o[a].length&&(a>0||!r)&&(o[a]=i+o[a]);return o.join("\n")},$.prototype.renderPartial=function t(e,r,i,o){if(i){var a=this.getConfigTags(o),s=n(i)?i(e[1]):i[e[1]];if(null!=s){var c=e[6],p=e[5],u=e[4],h=s;0==p&&u&&(h=this.indentPartial(s,u,c));var l=this.parse(h,a);return this.renderTokens(l,r,i,h,o)}}},$.prototype.unescapedValue=function t(e,n){var r=n.lookup(e[1]);if(null!=r)return r},$.prototype.escapedValue=function t(e,n,r){var i=this.getConfigEscape(r)||y.escape,o=n.lookup(e[1]);if(null!=o)return"number"==typeof o&&i===y.escape?String(o):i(o)},$.prototype.rawValue=function t(e){return e[1]},$.prototype.getConfigTags=function t(n){return e(n)?n:n&&"object"==typeof n?n.tags:void 0},$.prototype.getConfigEscape=function t(n){return n&&"object"==typeof n&&!e(n)?n.escape:void 0};var y={name:"mustache.js",version:"4.2.0",tags:["{{","}}"],clearCache:void 0,escape:void 0,parse:void 0,render:void 0,Scanner:void 0,Context:void 0,Writer:void 0,set templateCache(cache){w.templateCache=cache},get templateCache(){return w.templateCache}},w=new $;return y.clearCache=function t(){return w.clearCache()},y.parse=function t(e,n){return w.parse(e,n)},y.render=function t(n,r,i,o){if("string"!=typeof n){var a;throw TypeError('Invalid template! Template should be a "string" but "'+(e(a=n)?"array":typeof a)+'" was given as the first argument for mustache#render(template, view, partials)')}return w.render(n,r,i,o)},y.escape=function t(e){return String(e).replace(/[&<>"'`=\/]/g,function t(e){return p[e]})},y.Scanner=g,y.Context=v,y.Writer=$,y});