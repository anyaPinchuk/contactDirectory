
    var modal = document.getElementById('myModal');
    var btn = document.getElementById("myBtn");
    var span = document.getElementsByClassName("close")[0];
    btn.addEventListener("click", function () {
        modal.style.display = "block";
    });
    span.addEventListener("click", function () {
        modal.style.display = "none";
    });
    var addPhoneButton = document.getElementById("addPhone");
    addPhoneButton.addEventListener("click", addPhone);
    var deletePhoneBtn = document.getElementById("deletePhone");
    deletePhoneBtn.addEventListener("click", deletePhone);

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
        input4.innerHTML = object.type;
        var td4 = document.createElement('td');
        td4.appendChild(input4);
        tr.appendChild(td4);
        var input5 = document.createElement('p');
        input5.innerHTML = object.comment;
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
                var input = document.querySelector("#hidden" + value);
                input.remove();
                rows[i].remove();
            }
        }
        if (document.querySelectorAll(".rows").length == 0) {
            table.style.visibility = "hidden";
        }
    }


//-------------Handlers--------//
