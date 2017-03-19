var editInput;
var form;
var addPhoneButton;
var savePhoneButton;
//--------pop Up window photo----------//
var modalPhoto = document.getElementById('myModalPhoto');
var btnPhoto = document.getElementById("photo");
var span = document.getElementsByClassName("close")[0];

btnPhoto.onclick = function () {
    modalPhoto.style.display = "block";
};

span.onclick = function () {
    modalPhoto.style.display = "none";
};
//////////////////////////////////
var attachmentDelete = document.getElementById("attachmentDelete");
attachmentDelete.addEventListener("click", deleteChosenAttachments);
var attachmentBtn = document.getElementById("attachmentBtn");
attachmentBtn.addEventListener("click", function () {
    document.getElementById("myModalAttachment").style.display = "block";
});
document.getElementsByClassName("close")[2].addEventListener("click", function () {
    document.getElementById("myModalAttachment").style.display = "none";
});

/////////////////////////////////////////////////
addPhoneButton = document.getElementById("addPhone");
savePhoneButton = document.getElementById("savePhone");
showEditFormBtn = document.getElementById("showEditFormBtn");

//--------pop Up window phone----------//

var modal = document.getElementById('myModal');
var btn = document.getElementById("myBtn");
var spanPhone = document.getElementsByClassName("close")[1];

btn.onclick = function () {
    modal.style.display = "block";
    form.countryCode.value = "";
    form.operatorCode.value = "";
    form.number.value = "";
    form.type.options[0].selected = "true";
    form.comment.value = "";
    addPhoneButton.style.visibility = "visible";
    savePhoneButton.style.visibility = "hidden";
};

spanPhone.onclick = function () {
    modal.style.display = "none";
};

///-----------Phone----------////
var phones = new Array();
var i = 0;
var object = {};
form = document.forms[0];

function addPhone() {
    if (document.querySelectorAll(".rows").length == 0) {
        table.style.visibility = "visible";
    }
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
var inputID;
function editPhone(input_id) {
    inputID = input_id;
    modal.style.display = "block";
    editInput = document.getElementById("hidden" + input_id);
    addPhoneButton.style.visibility = "hidden";
    savePhoneButton.style.visibility = "visible";
    console.log(editInput);
    var strings;
    if (editInput.name == "hiddensForUpdate"){
        strings = editInput.value.split(";");
        form.countryCode.value = strings[1];
        form.operatorCode.value = strings[2];
        form.number.value = strings[3];
        form.type.value = strings[4];
        form.comment.value = strings[5];
    }
    else{
        strings = editInput.value.split(";");
        form.countryCode.value = strings[0];
        form.operatorCode.value = strings[1];
        form.number.value = strings[2];
        form.type.value = strings[3];
        form.comment.value = strings[4];
    }

}
savePhoneButton.onclick =  function () {
    saveChanges(inputID, form.countryCode.value, form.operatorCode.value, form.number.value, form.type.value, form.comment.value);
};
function addPhoneInTable(object, i) {
    var tbody = document.getElementById('tbody');
    var tr = document.createElement('tr');
    tr.className = "rows";
    var input3 = document.createElement('input');
    input3.type = "hidden";
    var button = document.createElement('button');
    var allHiddenInputs = document.getElementsByClassName("hiddens");
    //проверка на совпадение id
    for (var k = 0; k < allHiddenInputs.length; k++) {
        if (allHiddenInputs[k].id == ("hidden" + i)) {
            i++;
        }
        else break;
    }
    tr.id = "row" + i;
    input3.value = object.countryCode + ";" + object.operatorCode + ";" + object.number + ";" + object.type + ";" + object.comment;
    input3.name = "hiddens";
    input3.id = "hidden" +i;
    var td3 = document.createElement('td');
    var hiddenTR = document.createElement('tr');
    hiddenTR.id = "hiddenRow" + i;
    td3.appendChild(input3);
    hiddenTR.appendChild(input3);
    var input2 = document.createElement('input');
    input2.className = "form-control";
    input2.type = "checkbox";
    input2.value = i;
    var td2 = document.createElement('td');
    button.type = "button";
    button.className = "btn btn-link";
    button.innerHTML = "Edit";
    button.addEventListener("click", function () {
        editPhone(i);
    });
    td2.appendChild(button);
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

var table = document.getElementById('phones');

function deletePhone() {
    var tbody = document.getElementById('tbody');
    var rows = document.querySelectorAll(".rows");
    for (var i = 0; ; i++) {
        if (i == rows.length) {
            break;
        }
        var checkbox = rows[i].childNodes[0].childNodes[1];
        if (checkbox.checked) {
            var value = checkbox.value;
            var row = document.querySelector("#hiddenRow" + value);
            row.remove();
            rows[i].remove();
        }
    }
    if (document.querySelectorAll(".rows").length == 0) {
        table.style.visibility = "hidden";
    }
}
//-------------Handlers--------//

addPhoneButton.addEventListener("click", addPhone);
var deletePhoneBtn = document.getElementById("deletePhone");
deletePhoneBtn.addEventListener("click", deletePhone);


////-------------photo------------///
function savePhoto(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById("photo").src = e.target.result;
        };
        reader.readAsDataURL(input.files[0]);
    }
    var span = document.getElementsByClassName("close")[0];
    span.click();
}
/////----------------Edit phone-----------------////


function saveChanges(id, country, operator, number, type, comment) {
    editInput.value = id + ";" + country + ";" + operator + ";" + number + ";" + type + ";" + comment;
    var tr = document.getElementById("row" + id);
    tr.childNodes[1].childNodes[0].innerHTML = "+" + country + " " + operator + " " + number;
    tr.childNodes[2].childNodes[0].innerHTML = type;
    tr.childNodes[3].childNodes[0].innerHTML = comment;
    savePhoneButton.style.visibility = "hidden";
    addPhoneButton.style.visibility = "visible";
    spanPhone.click();
}
//////////////////////////
var indexOfFile = 0;
var tableAttach = document.getElementById("attachments");

function addAttachment() {
    tableAttach.style.visibility = "visible";
    var allHiddenInputs = document.getElementsByName("hiddensForUpdate");
    //проверка на совпадение id
    var attachment = document.getElementById("attachment" + indexOfFile);
    for (var k = 0; k < allHiddenInputs.length; k++) {
        if (allHiddenInputs[k].id == ("hiddenFileInfo$" + indexOfFile)) {
            indexOfFile++;
        }
        else break;
    }
    var div = document.getElementById('forHiddenFiles');
    div.appendChild(attachment);
    var br = document.createElement('br');
    div.appendChild(br);
    document.getElementById("labelFile").insertAdjacentHTML('afterend', "<input accept='application/msword, text/plain," +
        " application/pdf, application/xml, .docx' class='form-control' type='file' name='attachment-"
        + ++indexOfFile + "0'  id='attachment" + indexOfFile + "'/>");
    var comment = document.getElementById("commentFile").value;
    addFileInfoInTable(indexOfFile - 1, attachment.files[0].name, new Date(), comment);
    document.getElementById("myModalAttachment").style.display = "none";
}


function addFileInfoInTable(i, fileName, date, comment) {
    var tbody = document.getElementById("tbodyAttach");
    var tr = document.createElement("tr");
    tr.className = "fileRows";
    var td1 = document.createElement("td");
    var input = document.createElement('input');
    var button = document.createElement('button');
    input.type = "checkbox";
    input.className = "form-control";
    input.value = i;
    button.type = "button";
    button.className = "btn btn-link";
    button.innerHTML = "Edit";
    button.addEventListener("click", function () {
        editAttachment(i);
    });
    td1.appendChild(button);
    td1.appendChild(input);
    tr.appendChild(td1);
    var td2 = document.createElement("td");
    var p1 = document.createElement('p');
    p1.innerHTML = fileName;
    td2.appendChild(p1);
    tr.appendChild(td2);
    var td3 = document.createElement("td");
    var p2 = document.createElement('p');
    p2.innerHTML = date.getDate() + "." + (date.getMonth() + 1) + "." + date.getFullYear();
    td3.appendChild(p2);
    tr.appendChild(td3);
    var td4 = document.createElement("td");
    var p3 = document.createElement('p');
    p3.innerHTML = comment;
    td4.appendChild(p3);
    tr.appendChild(td4);
    var hiddenTR = document.createElement('tr');
    hiddenTR.id = "hiddenFileInfoRow" + i;
    var td = document.createElement("td");
    var hiddenInput = document.createElement('input');
    hiddenInput.type = "hidden";
    hiddenInput.name = "hiddenInfoForInsert";
    hiddenInput.value = i + ";" + fileName + ";" + date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
        + ";" + comment;
    tbody.appendChild(tr);
    hiddenTR.appendChild(hiddenInput);
    tbody.appendChild(hiddenTR);
}

function deleteChosenAttachments() {
    var tbody = document.getElementById('tbodyAttach');
    var rows = document.querySelectorAll(".fileRows");
    for (var i = 0; ; i++) {
        if (i == rows.length) {
            break;
        }
        var checkbox = rows[i].childNodes[0].childNodes[0];
        if (checkbox.checked) {
            var value = checkbox.value;
            var row = document.querySelector("#hiddenFileInfoRow" + value);
            var attachment = document.getElementById("attachment" + value);
            attachment.remove();
            input.remove();
            rows[i].remove();
        }
    }
    if (document.querySelectorAll(".fileRows").length == 0) {
        tableAttach.style.visibility = "hidden";
    }
}
if (document.querySelectorAll(".fileRows").length == 0) {
    tableAttach.style.visibility = "hidden";
}

if (document.querySelectorAll(".rows").length == 0) {
    table.style.visibility = "hidden";
}
function editAttachment(id) {

}