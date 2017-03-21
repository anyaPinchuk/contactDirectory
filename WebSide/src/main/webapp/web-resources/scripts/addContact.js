//-------------Handlers--------//
var modal = document.getElementById('myModal');
var btn = document.getElementById("myBtn");
var span = document.getElementsByClassName("close")[0];
btn.addEventListener("click", function () {
    modal.style.display = "block";
});
span.addEventListener("click", function () {
    modal.style.display = "none";
});
/////////////////////////////
var addPhoneButton = document.getElementById("addPhone");
addPhoneButton.addEventListener("click", addPhone);
var deletePhoneBtn = document.getElementById("deletePhone");
deletePhoneBtn.addEventListener("click", deletePhone);

////////////////attachment
var attachmentDelete = document.getElementById("attachmentDelete");
attachmentDelete.addEventListener("click", deleteChosenAttachments);
var attachmentBtn = document.getElementById("attachmentBtn");
attachmentBtn.addEventListener("click", function () {
    document.getElementById("myModalAttachment").style.display = "block";
});
document.getElementsByClassName("close")[1].addEventListener("click", function () {
    document.getElementById("myModalAttachment").style.display = "none";
});

//--------pop Up window----------//


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
    if (object.countryCode != "" || object.operatorCode != "" || object.number != "") {
        phones.push(object);
        addPhoneInTable(object, i++);
    }
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
    hiddenTR.id = "hiddenRow" + i;
    td3.appendChild(input3);
    hiddenTR.appendChild(input3);
    var input2 = document.createElement('input');
    input2.className = "form-control";
    input2.type = "checkbox";
    input2.value = i;
    var td2 = document.createElement('td');
    td2.appendChild(input2);
    tr.appendChild(td2);
    var p1 = document.createElement('p');
    var td1 = document.createElement('td');
    p1.innerHTML = "+" + object.countryCode + " " + object.operatorCode + " " + object.number;
    td1.appendChild(p1);
    tr.appendChild(td1);
    var p2 = document.createElement('p');
    p2.innerHTML = object.type;
    var td4 = document.createElement('td');
    td4.appendChild(p2);
    tr.appendChild(td4);
    var p3 = document.createElement('p');
    p3.innerHTML = object.comment;
    var td5 = document.createElement('td');
    td5.appendChild(p3);
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
            var input = document.querySelector("#hiddenRow" + value);
            input.remove();
            rows[i].remove();
        }
    }
    if (document.querySelectorAll(".rows").length == 0) {
        table.style.visibility = "hidden";
    }
}

//----------------Attachment--------//
var indexOfFile = 0;
var tableAttach = document.getElementById("attachments");

function addAttachment() {
    tableAttach.style.visibility = "visible";
    var attachment = document.getElementById("attachment" + indexOfFile);
    console.log(attachment);
    var div = document.getElementById('forHiddenFiles');
    div.appendChild(attachment);
    var br = document.createElement('br');
    div.appendChild(br);
    document.getElementById("labelFile").insertAdjacentHTML('afterend', "<input accept='application/msword, text/plain," +
        " application/pdf, application/xml, .docx' class='form-control' type='file'  name='attachment-"
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
    input.type = "checkbox";
    input.className = "form-control";
    input.value = i;
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
    hiddenInput.id = "hiddenFileInfo" + i;
    hiddenInput.name = "hiddenInfoForInsert";
    hiddenInput.value = i + ";" + fileName + ";" + date.getFullYear() + "-";
    if (Number(date.getMonth() + 1) < 10) {
        hiddenInput.value += "0" + (date.getMonth() + 1) + "-";
    } else {
        hiddenInput.value += (date.getMonth() + 1) + "-";
    }
    if (Number(date.getDate()) < 10) {
        hiddenInput.value += "0" + date.getDate() + ";" + comment;
    } else {
        hiddenInput.value += date.getDate() + ";" + comment;
    }
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
            var input = document.querySelector("#hiddenFileInfoRow" + value);
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
function validateForm() {
    var date = document.getElementById("contactDate");
    if (!date.value.match(/^(0?[1-9]|[12][0-9]|3[01])[\-](0?[1-9]|1[012])[\-]\d{4}$/)){
    } else {
        var strings = date.value.split("-");
        date.value = strings[2] +  "-" + strings[1] + "-" + strings[0];
    }
}
