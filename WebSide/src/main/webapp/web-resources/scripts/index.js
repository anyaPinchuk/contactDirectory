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
        if (id!=null){
            document.location.href = "showContact?id=" + id;
        }
}
var deleteContactBtn = document.getElementById("deleteContactBtn");
deleteContactBtn.addEventListener("click", deleteContacts);
function deleteContacts() {
    //include check on empty list of inputs
    document.forms[0].submit();
}