function showContact() {
    var inputs = document.getElementsByTagName("input");
    var id;
    var link = document.getElementsByTagName("a")[0];
    for (var i = 0; i < inputs.length; i++) {
        if (inputs[i].checked) {
            id = inputs[i].value;

            break;
        }
    }
    link.href += id;
}
var deleteContactBtn = document.getElementById("deleteContactBtn");
deleteContactBtn.addEventListener("click", deleteContacts);
function deleteContacts() {
    //include check on empty list of inputs
    document.forms[0].submit();
}