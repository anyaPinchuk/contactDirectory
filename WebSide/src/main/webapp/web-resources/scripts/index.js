function validate() {
    var form = document.forms.addContact;
    var name = form.name;
    var surname = form.surname;
    form.submit();
}
//--------pop Up window----------//
var modal = document.getElementById('myModal');
var btn = document.getElementById("myBtn");
var span = document.getElementsByClassName("close")[0];
btn.onclick = function () {
    modal.style.display = "block";
};
span.onclick = function () {
    modal.style.display = "none";
};

///------ phones --------///
var phones = new Array();
var i = 0;
var object = {};
function addPhone() {
    var form = document.forms.addPhone;
    object.countryCode = form.countryCode.value;
    object.operatorCode = form.operatorCode.value;
    object.number = form.number.value;
    object.type = form.type.value;
    object.comment = form.comment.value;
    phones.push(object);
    addPhoneInTable(object, i++);
}
var table = document.getElementById('phones');
table.style.visibility = "hidden";

function addPhoneInTable(object, i) {
    table.style.visibility = "visible";
    var tbody = document.getElementById('tbody');
    var tr = document.createElement('tr');
    tr.id = "row" + i;
    tr.className = "rows";
    var input3 = document.createElement('input');
    input3.type = "hidden";
    input3.id = "hidden" + i;
    input3.value = object.countryCode + ";" + object.operatorCode + ";" + object.number + ";" + object.type + ";" + object.comment;
    input3.name = "hiddens";
    var td3 = document.createElement('td');
    var hiddenTR = document.createElement('tr');
    td3.appendChild(input3);
    hiddenTR.appendChild(input3);
    var input2 = document.createElement('input');
    input2.className = "form-control";
    input2.type = "checkbox";
    input2.value = i;
    var td2 = document.createElement('td');
    td2.appendChild(input2);
    tr.appendChild(td2);
    var input1 = document.createElement('input');
    input1.className = "form-control";
    input1.disabled = "true";
    var td1 = document.createElement('td');
    input1.value = "+" + object.countryCode + " " + object.operatorCode + " " + object.number;
    td1.appendChild(input1);
    tr.appendChild(td1);
    var input4 = document.createElement('input');
    input4.className = "form-control";
    input4.disabled = "true";
    input4.value = object.type;
    var td4 = document.createElement('td');
    td4.appendChild(input4);
    tr.appendChild(td4);
    var input5 = document.createElement('input');
    input5.className = "form-control";
    input5.disabled = "true";
    input5.value = object.comment;
    var td5 = document.createElement('td');
    td5.appendChild(input5);
    tr.appendChild(td5);
    tbody.appendChild(tr);
    tbody.appendChild(hiddenTR);
    span.click();
}

function deletePhone() {
    var tbody = document.getElementById('tbody');
    var rows = document.querySelectorAll(".rows");
    for (var i = 0; ; i++) {
        if (i == rows.length) {
            break;
        }
        var checkbox = rows[i].childNodes[0].childNodes[0];
        if (checkbox.checked) {
            var value = checkbox.value;
            var input =  document.querySelector("#hidden" + value);
            input.remove();
            rows[i].remove();
        }
    }
    if (tbody.children.length == 1) {
        table.style.visibility = "hidden";
    }
}

//-------------Handlers--------//
var addPhoneButton = document.getElementById("addPhone");
addPhoneButton.addEventListener("click", addPhone);
var deletePhoneBtn = document.getElementById("deletePhone");
deletePhoneBtn.addEventListener("click", deletePhone);