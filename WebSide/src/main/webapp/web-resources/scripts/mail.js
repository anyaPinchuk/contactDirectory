var form = document.forms[0];

form.template.addEventListener("change", fillContent);
function fillContent() {
    var selectedOption = form.template.options[form.template.selectedIndex];
    form.content.value = selectedOption.attributes.label.textContent;
    form.content.disabled = selectedOption.attributes.label.textContent != '';
}

var sendMail = document.getElementById("sendMail");
sendMail.addEventListener("click", function () {
    var element = document.getElementById("notifyDiv");
    if(validate(element,form.subject) && !isEmpty(element,form.content)){
        var inputs = document.getElementsByName("emails");
        for (var i = 0; i < inputs.length; i++) {
            inputs[i].disabled = false;
        }
        form.submit();
    }

});

window.onload = function () {
    form.content.value = "";
};