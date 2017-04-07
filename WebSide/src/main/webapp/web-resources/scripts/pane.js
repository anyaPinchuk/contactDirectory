/**
 * show pane block by clicking on nav-tab
 * @param pane
 */

function showPane(pane) {
    var div;
    pane.className = "active";
    switch (pane.id) {
        case "main": {
            document.getElementById("sub").className = "";
            document.getElementById("address").className = "";
            document.getElementById("phone").className = "";
            document.getElementById("attachment").className = "";
            div = document.getElementById("main_info");
            div.className = "tab-pane fade in active";
            document.getElementById("sub_info").className = "tab-pane fade";
            document.getElementById("address_info").className = "tab-pane fade";
            document.getElementById("phone_info").className = "tab-pane fade";
            document.getElementById("attachment_info").className = "tab-pane fade";
            break;
        }
        case "sub": {
            document.getElementById("main").className = "";
            document.getElementById("address").className = "";
            document.getElementById("phone").className = "";
            document.getElementById("attachment").className = "";
            div = document.getElementById("sub_info");
            div.className = "tab-pane fade in active";
            document.getElementById("main_info").className = "tab-pane fade";
            document.getElementById("address_info").className = "tab-pane fade";
            document.getElementById("phone_info").className = "tab-pane fade";
            document.getElementById("attachment_info").className = "tab-pane fade";
            break;
        }
        case "address": {
            document.getElementById("sub").className = "";
            document.getElementById("main").className = "";
            document.getElementById("phone").className = "";
            document.getElementById("attachment").className = "";
            div = document.getElementById("address_info");
            div.className = "tab-pane fade in active";
            document.getElementById("main_info").className = "tab-pane fade";
            document.getElementById("sub_info").className = "tab-pane fade";
            document.getElementById("phone_info").className = "tab-pane fade";
            document.getElementById("attachment_info").className = "tab-pane fade";
            break;
        }
        case "phone": {
            document.getElementById("sub").className = "";
            document.getElementById("address").className = "";
            document.getElementById("main").className = "";
            document.getElementById("attachment").className = "";
            div = document.getElementById("phone_info");
            div.className = "tab-pane fade in active";
            document.getElementById("sub_info").className = "tab-pane fade";
            document.getElementById("address_info").className = "tab-pane fade";
            document.getElementById("main_info").className = "tab-pane fade";
            document.getElementById("attachment_info").className = "tab-pane fade";
            break;
        }
        case "attachment": {
            document.getElementById("sub").className = "";
            document.getElementById("address").className = "";
            document.getElementById("phone").className = "";
            document.getElementById("main").className = "";
            div = document.getElementById("attachment_info");
            div.className = "tab-pane fade in active";
            document.getElementById("sub_info").className = "tab-pane fade";
            document.getElementById("address_info").className = "tab-pane fade";
            document.getElementById("phone_info").className = "tab-pane fade";
            document.getElementById("main_info").className = "tab-pane fade";
            break;
        }
    }

}