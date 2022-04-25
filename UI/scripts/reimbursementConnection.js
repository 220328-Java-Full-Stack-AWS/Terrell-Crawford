
//let reimbursementResourceURL = "http://localhost:port/contextpath/resourcepath"; //CHANGE ME!\
//Example backend location:
let reimbursementResourceURL = "http://localhost:8080/ERS/ReimbursementServ";
//Note the context path is set to "/api" make sure to change that in the build config



async function newReimbursement(newReimbursement) {
    let response = await fetch(
        reimbursementResourceURL,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "authToken": localStorage.getItem("authToken")
            },
            body: JSON.stringify(newReimbursement)
        }
    );

    return response;
}

async function getReimbursementById(id) {
    let response = await fetch(
        reimbursementResourceURL,
        {
            method: "GET",
            headers: {
                "rMethodToBeCalled": "id",
                Id: id,
                authToken: localStorage.getItem("authToken")
            }
        }
    );

    return response;
}

async function getReimbursement(status) {
    let response = await fetch(
        reimbursementResourceURL,
        {
            method: "GET",
            headers: {
                "rMethodToBeCalled": "status",
                Status: status,
                authToken: localStorage.getItem("authToken")
            }
        }
    );

    return response;
}


async function updateReimbursement(reimbursement) {
    let response = await fetch(
        reimbursementResourceURL,
        {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "rId": localStorage.getItem("rId"),
                "reimbIsBeing": "updated"
            },
            body: JSON.stringify(reimbursement)
        }
    );

    return response;
}
async function processReimbursement(reimbursement) {
    localStorage.setItem("rMethodToBeCalled", "id");
    let response = await fetch(
        reimbursementResourceURL,
        {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "rId": localStorage.getItem("rId"),
                "reimbIsBeing": "processed",
                "decision": localStorage.getItem("decision"),
                "authToken": localStorage.getItem("authToken")

            },
            body: JSON.stringify(reimbursement)
        }
    );

    return response;
}




async function deleteReimbursement(reimbursement) {
    let response = await fetch(
        reimbursementResourceURL,
        {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                "rId": localStorage.getItem("rId")
            },
            body: JSON.stringify(reimbursement)
        }
    );

    return response;
}