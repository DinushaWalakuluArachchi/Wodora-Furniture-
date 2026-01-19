async function SignUp() {

    const fname = document.getElementById("fname").value;
    const lname = document.getElementById("lname").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const user = {
        fristName: fname,
        lastName: lname,
        email: email,
        password: password
    };

    const userJson = JSON.stringify(user);

    const response = await fetch(
            "SignUp",
            {
                method: "POST",
                body: userJson,
                header: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {//success
        const Json = await response.json();
        if (Json.status) {//if true
            window.location = "verification.html"
            console.log(Json.message);
            sweetAlert("Success", Json.message, "success");
        } else { // when status false

            sweetAlert("Error", Json.message, "error");

            //document.getElementById("message").innerHTML = Json.message;
        }


    } else {
        console.log("");
        sweetAlert("Error", "Registration Faild Plrase Try Again!", "error");

        //document.getElementById("message").innerHTML = "Registration Faild Plrase Try Again!";
    }
}