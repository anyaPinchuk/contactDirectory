function checkFieldOnSpace(field) {
    return !field.match(/^\s+$/g);
}

function validate() {
    var flag = true;
    for (var i = 1; i < arguments.length; i++) {
        if (!checkFieldOnSpace(arguments[i].value)) {
            markInputAsWrong(arguments[i]);
            flag = false;
        } else markInputAsRight(arguments[i]);
    }
    if (!flag) showMessage(arguments[0], "Red field consists of spaces");
    return flag;
}

function validDate(element, date) {
    if (date.value == "") {
        markInputAsRight(date);
        return true;
    } else if (date.value.match(/^(0?[1-9]|[12][0-9]|3[01])[\-](0?[1-9]|1[012])[\-]\d{4}$/)) {
        // var strings = date.value.split("-");
        // date.value = strings[2] + "-" + strings[1] + "-" + strings[0];
        markInputAsRight(date);
        return true;
    } else if (!date.value.match(/^(0?[1-9]|[12][0-9]|3[01])[\-](0?[1-9]|1[012])[\-]\d{4}$/)) {
        markInputAsWrong(date);
        showMessage(element, "Wrong date, please,<br> use right format like DD-MM-YYYY");
        return false;
    }
}

function validEmail(element, email) {
    if (!/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email.value)) {
        markInputAsWrong(email);
        showMessage(element, "Wrong email, use right format<br> like address@example.com");
        return false;
    } else if(email.value == ""){
        markInputAsWrong(email);
        showMessage(element, "Email can't be null");
    } else return true;
}

function markInputAsWrong(input) {
    input.style.borderColor = "rgba(238, 91, 91, 0.73)";
}

function markInputAsRight(input) {
    input.style.borderColor = "rgba(0, 136, 204, 0.76)";
}


function isEmpty(element, field) {
    if (field.value == "") {
        showMessage(element, "This field can't be empty");
        markInputAsWrong(field);
        return true;
    }
    else {
        if (!checkFieldOnSpace(field.value)) {
            showMessage(element, "Field consists of spaces");
            markInputAsWrong(field);
            return true;
        } else {
            markInputAsRight(field);
            return false;
        }
    }
}


function showMessage(element, msg) {
    document.querySelector(".notifier .message").innerHTML = msg;
    showDiv(element, true);

}
var times = 0;
function showDiv(element, flag) {
    times = 0;
    var start = Date.now();

    var timer = setInterval(function () {
        var timePassed = Date.now() - start;
        if (timePassed > 2000) {
            clearInterval(timer);
            return;
        }
        draw(element, flag, timePassed);
        if (flag) {
            if (++times == 48) {
                setTimeout(function () {
                    showDiv(element, false);
                }, 3000);
            }
        } else if (++times == 49) {
            element.style.opacity = 0;
        }

    }, 40);
}

function draw(element, flag, timePassed) {
    if (flag) {
        element.style.opacity = timePassed / 2000;
    } else {
        element.style.opacity = 1 - timePassed / 2000;
    }
}

