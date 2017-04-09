/**
 * checks if field consists only of spaces
 * @param field
 * @returns {boolean}
 */

function checkFieldOnSpace(field) {
    return !/^\s+$/g.test(field);
}

function validate() {
    var flag = true;
    for (var i = 1; i < arguments.length; i++) {
        if (!checkFieldOnSpace(arguments[i].value)) {
            markInputAsWrong(arguments[i]);
            flag = false;
        } else markInputAsRight(arguments[i]);
    }
    if (!flag) showMessage(arguments[0], "Field '" + arguments[i].name + "' consists of spaces");
    return flag;
}

/**
 * validates date by format DD-MM-YYYY and notifies if date is invalid
 * @param element
 * @param date
 * @returns {boolean}
 */

function validDate(element, date) {
    if (date.value === "") {
        markInputAsRight(date);
        return true;
    } else if (date.value.match(/^(0?[1-9]|[12][0-9]|3[01])[\-](0?[1-9]|1[012])[\-]\d{4}$/)) {
        var today = new Date();
        var myDate = date.value.split("-");
        today.setYear(myDate[2]);
        today.setMonth(myDate[1] - 1);
        today.setDate(myDate[0]);
        console.log(today);
        if(myDate[2] === today.getFullYear() && myDate[1] === today.getMonth() && myDate[0] === today.getDate()) {
            markInputAsRight(date);
            return true;
        } else {
            markInputAsWrong(date);
            showMessage(element, "Such date doesn't exist");
            return false;
        }

    } else if (!date.value.match(/^(0?[1-9]|[12][0-9]|3[01])[\-](0?[1-9]|1[012])[\-]\d{4}$/)) {
        markInputAsWrong(date);
        showMessage(element, "Wrong date, please,<br> use right format like DD-MM-YYYY");
        return false;
    }
}

function validEmail(element, email) {
    if (!/^\w+([\.-\\+]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email.value.trim())) {
        markInputAsWrong(email);
        showMessage(element, "Wrong email, use right format<br> like address@example.com");
        return false;
    } else if (email.value === "") {
        markInputAsWrong(email);
        showMessage(element, "Email can't be null");
    } else return true;
}

function markInputAsWrong(input) {
    input.style.border = "2px solid";
    input.style.borderColor = "rgba(238, 91, 91, 0.73)";
}

function markInputAsRight(input) {
    input.style.borderColor = "rgba(0, 136, 204, 0.76)";
}

/**
 * checks if field is empty and doesn't consist of spaces
 * @param element
 * @param field
 * @returns {boolean}
 */

function isEmpty(element, field) {
    if (field.value === "") {
        showMessage(element, "Field '" + field.name + "' can't be empty");
        markInputAsWrong(field);
        return true;
    }
    else {
        if (!checkFieldOnSpace(field.value)) {
            showMessage(element, "Field '" + field.name + "' consists of spaces");
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

/**
 * count of inputs in function setInterval
 * @type {number}
 */

var times = 0;

/**
 * shows or hides notifier div slowly
 * @param element
 * @param flag
 */

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
            if (++times === 48) {
                setTimeout(function () {
                    showDiv(element, false);
                }, 3000);
            }
        } else if (++times === 49) {
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

