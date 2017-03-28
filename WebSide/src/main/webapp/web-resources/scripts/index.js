function showContact() {
    var inputs = document.getElementsByTagName("input");
    var id = null;
    for (var i = 0; i < inputs.length; i++) {
        if (inputs[i].checked) {
            id = inputs[i].value;
            break;
        }
    }
    if (id != null) {
        document.location.href = "showContact?id=" + id;
    }
}
var deleteContactBtn = document.getElementById("deleteContactBtn");
var sendMailBtn = document.getElementById("sendMailBtn");

sendMailBtn.addEventListener("click", function () {
    var form = document.forms[0];
    var inputs = document.getElementsByTagName("input");
    var flag = false;
    for (var i = 0; i < inputs.length; i++) {
        if (inputs[i].checked) {
            flag = true;
            break;
        }
    }
    if (flag) {
        form.action = "sendMail";
        form.submit();
    }
});

deleteContactBtn.addEventListener("click", deleteContacts);
function deleteContacts() {
    var inputs = document.getElementsByTagName("input");
    var flag = false;
    for (var i = 0; i < inputs.length; i++) {
        if (inputs[i].checked) {
            flag = true;
            break;
        }
    }
    if (flag) {
        document.forms[0].submit();
    }
}

var next = document.getElementById("next");
var prev = document.getElementById("prev");
next.addEventListener("click", showNextPage);
prev.addEventListener("click", showPrevPage);
function showPrevPage() {
    var strings = document.location.href.split("/");
    var last = strings[strings.length - 1];
    if (last != "contacts") {
        var newPage = Number(last.substr(last.length - 1)) - 1;
        if (newPage != 0){
            document.location.href =  "contacts?page=" + newPage;
        }

    }
}

function showNextPage() {
    var div = document.getElementsByClassName("pages")[0];
    var nodes = div.childNodes;
    var a = nodes[nodes.length - 2];
    var lastPage = a.href.substr(a.href.length - 1);
    var strings = document.location.href.split("/");
    var last = strings[strings.length - 1];
    var newPage = Number(last.substr(last.length - 1)) + 1;
    if (!(newPage > lastPage)){
        document.location.href =  "contacts?page=" + newPage;
    }
}