var form = document.forms[0];

form.template.addEventListener("change", fillContent);
function fillContent() {
    var selectedOption = form.template.options[form.template.selectedIndex];
    form.content.value = selectedOption.attributes.label.textContent;
}
var sendMail = document.getElementById("sendMail");
sendMail.addEventListener("click", function () {
    var inputs = document.getElementsByName("emails");
    for (var i = 0; i < inputs.length; i++) {
        inputs[i].disabled = false;
    }
});

window.onload = function () {
    form.content.value = "";
};