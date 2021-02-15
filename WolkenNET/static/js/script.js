var $ = jQuery.noConflict();
var editor;
var isTriggered = false;

/* 
this javascript is only to change the "actpage" attribut on the .cdp div
*/

window.onload = function () {

    var paginationPage = parseInt($('.cdp').attr('actpage'), 10);
    $('.cdp_i').on('click', function () {
        var go = $(this).attr('href').replace('#!', '');
        if (go === '+1') {
            paginationPage++;
        } else if (go === '-1') {
            paginationPage--;
        } else {
            paginationPage = parseInt(go, 10);
        }
        $('.cdp').attr('actpage', paginationPage);
    });
};

$(document).ready(function () {
    $(function () {
        if (editor) {
            if (qs["c"] == "err") {
                var element = document.getElementById("recaptchaerr");
                element.classList.remove("hidden");
            } else {
                var element = document.getElementById("recaptchaerr");
                element.classList.add("hidden");
            }
        }

        $("#fadeEditor").click(function () {
            $("#editorArea").slideDown();
        }
        );
        $('[data-toggle="tooltip"]').tooltip()
    })


    $("#login").on('show.bs.modal', function () {
        $('body').append($('<div id="captcha_container" class="google-cpatcha"></div>'));
        setTimeout(function () {
            grecaptcha.render('captcha_container_login', {
                'sitekey': recaptcha
            });
        }, 1000);
    });

    $("#register").on('show.bs.modal', function () {
        $('body').append($('<div id="captcha_container" class="google-cpatcha"></div>'));
        setTimeout(function () {
            grecaptcha.render('captcha_container_reg', {
                'sitekey': recaptcha
            });
        }, 1000);
    });

    $(function () {

        $('body').append($('<div id="captcha_container" class="google-cpatcha"></div>'));
        setTimeout(function () {
            try {
                grecaptcha.render('captcha_container_post', {
                    'sitekey': recaptcha
                });
            } catch (error) {
            }

        }, 1000);


        if ($('#edi').length == 0) {
            if (loggedin) {
                editor = document.getElementById("edi");
                m = false;
                try {
                    m = editor.classList.contains('bbcode-editor')
                } catch (error) {

                }
                if (m) {

                } else {
                    $('textarea').bbcode();
                    $('body').append($('<div id="captcha_container" class="google-cpatcha"></div>'));
                    setTimeout(function () {
                        try {
                            grecaptcha.render('captcha_container_post', {
                                'sitekey': recaptcha
                            });
                        } catch (error) {
                        }

                    }, 1000);
                }



            }

        }
        if (!loggedin) {
            var userLang = navigator.language || navigator.userLanguage;
            document.getElementById("country").value = userLang;
            if (qs["open"] != null) {
                if (qs["open"] == "login") {
                    console.log("login");
                    $("#login").modal()
                } else if (qs["open"] == "register") {
                    $("#register").modal()
                }
            }
            var l = qs["l"];
            var btn = '<button type="button" class="close" data-dismiss="alert" aria-label="Close"\><span aria-hidden="true">&times;</span\></button\>'
            if (l != null) {
                document.getElementById('alert-login').innerHTML = '<div id="inr" class="alert rlalert alert-danger alert-dismissible fade show" role="alert"\></div>';
                if (l == "li") {
                    document.getElementById('inr').innerHTML = 'Die angegebenen Daten sind Fehlerhaft.' + btn
                } else if (l == "ll") {
                    document.getElementById('inr').innerHTML = 'Dein Gerät wurde für das einloggen kurzzeitig gesperrt.' + btn
                } else if (l == "lub") {
                    document.getElementById('inr').innerHTML = 'Dein Nutzer Account wurde gesperrt, für weitere Informationen schreibe uns eine Email <a href="mailto:info@wolkennet.de">info@wolkennet.de</a>' + btn
                } else if (l == "lnp") {
                    document.getElementById('inr').innerHTML = 'Du hast keine Berechtigung um dich dort zu bewegen' + btn
                } else if (l == "lcf") {
                    document.getElementById('inr').innerHTML = 'Du musst das Captcha ausfüllen' + btn
                }
            }
            var r = qs["r"];
            if (r != null) {
                document.getElementById('alert-register').innerHTML = '<div id="inrr" class="alert rlalert alert-danger alert-dismissible fade show" role="alert"\></div>';
                if (r == "ret") {
                    document.getElementById('inrr').innerHTML = 'Die Email Adresse ist vergeben.' + btn
                } else if (r == "rut") {
                    document.getElementById('inrr').innerHTML = 'Der Nutzername ist vergeben.' + btn
                } else if (r == "rul") {
                    document.getElementById('inrr').innerHTML = 'Der Nutzername ist zu lang.' + btn
                } else if (r == "rus") {
                    document.getElementById('inrr').innerHTML = 'Du darfst keine Leerzeichen in deinem Namen nutzen, nutze doch (!,_,+,/).' + btn
                } else if (r == "rel") {
                    document.getElementById('inrr').innerHTML = 'Die Email Adresse ist zu lang.' + btn
                } else if (r == "rts") {
                    document.getElementById('inrr').innerHTML = 'Dein Passwort ist zu kurz.' + btn
                } else if (r == "rie") {
                    document.getElementById('inrr').innerHTML = 'Deine Email Adresse ist fehlerhaft.' + btn
                } else if (r == "re") {
                    document.getElementById('inrr').innerHTML = 'Ein interner Fehler ist aufgetreten, wir versuchen das so schnell wie möglich zu reparieren.' + btn
                } else if (r == "rd") {
                    document.getElementById('inrr').innerHTML = 'Registrierungen sind momentan deaktiviert, versuche es bitte später nochmal.' + btn
                } else if (r == "rcf") {
                    document.getElementById('inrr').innerHTML = 'Du musst das Captcha ausfüllen.' + btn
                }
            }
        }

    });
});

jQuery(document).ready(function () {
    jQuery("time.timeago").timeago();
});

function onSubmit(token) {
    document.getElementById("rl-form").submit();
}

function safety(forward) {
    if (confirm('Diese Aktion wirklich ausführen?')) {
        window.location.href = forward;
    }
}


var qs = (function (a) {
    if (a == "") return {};
    var b = {};
    for (var i = 0; i < a.length; ++i) {
        var p = a[i].split('=', 2);
        if (p.length == 1)
            b[p[0]] = "";
        else
            b[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
    }
    return b;
})(window.location.search.substr(1).split('&'));