var link = document.getElementById("showContactBtn");
// link.addEventListener("click", function (link) {
//     var inputs = document.getElementsByTagName("input");
//     var id;
//     for (var i = 0; i < inputs.length; i++) {
//         if (inputs[i].checked) {
//             id = inputs[i].value;
//             break;
//         }
//     }
//     link.href = "showContact?id=" + id;
//     //link.submit();
// });
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