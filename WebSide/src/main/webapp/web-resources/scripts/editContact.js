var modalPhoto = document.getElementById('myModalPhoto');
var btnPhoto = document.getElementById("photo");
var span = document.getElementsByClassName("close")[0];

btnPhoto.onclick = function () {
    modalPhoto.style.display = "block";
};

span.onclick = function () {
    modalPhoto.style.display = "none";
};

var addPhoneButton = document.getElementById("addPhone");
var savePhoneButton = document.getElementById("savePhone");

//--------pop Up window phone----------//

var modal = document.getElementById('myModal');
var btn = document.getElementById("myBtn");
var spanPhone = document.getElementsByClassName("close")[1];

btn.onclick = function () {
    modal.style.display = "block";
};

spanPhone.onclick = function () {
    modal.style.display = "none";
};

///-----------Phone----------////
var phones = new Array();
var i = 0;
var object = {};
var form = document.forms[0];

function addPhone() {

    object.countryCode = form.countryCode.value;
    object.operatorCode = form.operatorCode.value;
    object.number = form.number.value;
    object.type = form.type.value;
    object.comment = form.comment.value;
    if (object.countryCode != "" || object.operatorCode != "" || object.number != "") {
        phones.push(object);
        addPhoneInTable(object, i++);
    }
}
var editInput;
function editPhone(input) {
    btn.click();
    addPhoneButton.style.visibility = "hidden";
    savePhoneButton.style.visibility = "visible";
    var strings = input.value.split(";");
    form.countryCode.value = strings[0];
    form.operatorCode.value = strings[1];
    form.number.value = strings[2];
    form.number.type = strings[3];
    form.comment.value = strings[4];
    editInput = input;
}

function saveChanges(country, operator, number, type, comment) {
    editInput.value = country + ";" + operator + ";" + number + ";" + type + ";" + comment;
    form.countryCode.value = "";
    form.operatorCode.value = "";
    form.number.value = "";
    form.type.value = "";
    form.comment.value = "";
    var tr = document.getElementById("row" + editInput.id);
    tr.childNodes[1].childNodes[0].innerHTML = "+" + country + " " + operator + " " + number;
    tr.childNodes[2].childNodes[0].innerHTML = type;
    tr.childNodes[3].childNodes[0].innerHTML = comment;
}

function addPhoneInTable(object, i) {
    var tbody = document.getElementById('tbody');
    var tr = document.createElement('tr');
    tr.className = "rows";
    var input3 = document.createElement('input');
    input3.type = "hidden";
    var allHiddenInputs = document.getElementsByClassName("hiddens");
    //проверка на совпадение id
    for (var k = 0; k < allHiddenInputs.length; k++) {
        if (allHiddenInputs[k].id == ("hidden" + i)) {
            i++;
        }
        else break;
    }
    tr.id = "row" + i;
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
    var input1 = document.createElement('p');
    var td1 = document.createElement('td');
    input1.innerHTML = "+" + object.countryCode + " " + object.operatorCode + " " + object.number;
    td1.appendChild(input1);
    tr.appendChild(td1);
    var input4 = document.createElement('p');
    input4.className = "form-control";
    input4.innerHTML = object.type;
    var td4 = document.createElement('td');
    td4.appendChild(input4);
    tr.appendChild(td4);
    var input5 = document.createElement('p');
    input5.className = "form-control";
    input5.innerHTML = object.comment;
    var td5 = document.createElement('td');
    td5.appendChild(input5);
    tr.appendChild(td5);
    tbody.appendChild(tr);
    tbody.appendChild(hiddenTR);
    spanPhone.click();
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
            var input = document.querySelector("#hidden" + value);
            input.remove();
            rows[i].remove();
        }
    }
    if (tbody.children.length == 1) {
        table.style.visibility = "hidden";
    }
}
////-------------photo------------///

function savePhoto(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById("photo").src = e.target.result;
        };
        reader.readAsDataURL(input.files[0]);
    }
    span.click();
}

//-------------Handlers--------//

addPhoneButton.addEventListener("click", addPhone);
var deletePhoneBtn = document.getElementById("deletePhone");
deletePhoneBtn.addEventListener("click", deletePhone);
savePhoneButton.addEventListener("click", saveChanges);