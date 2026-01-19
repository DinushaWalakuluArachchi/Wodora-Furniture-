async function SendOtpCode(){

    const email = document.getElementById("email").value;
    const otp = document.getElementById("otp").value;

    console.log(email);

    const FogetPassword = {
        email: email,
        otp: otp
    };

    const FogetPasswordJosn = JSON.stringify(FogetPassword);

    const response = await fetch(
            "ForgetPassword",
            {
                method: "POST",
                body:FogetPasswordJosn,
                header: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const Json = await response.json();
        if (Json.status) {
            sweetAlert("Success", Json.message, "success");
            document.getElementById("otpButton").disabled = false;
            document.getElementById("otp").disabled = false;
            document.getElementById("SendOTP").disabled = true;
            document.getElementById("email").disabled = true;
        } else {
            sweetAlert("Error", Json.message, "error");
        }
    } else {
        sweetAlert("Error", "Unable To Send OTP try Again Later!", "error");
        
    }
}

async function CheckOTP() {
    const email = document.getElementById("email").value;
    const otp = document.getElementById("otp").value;

    const FogetPassword = {
        email: email,
        otp: otp
    };

    const FogetPasswordJosn = JSON.stringify(FogetPassword);

    const response = await fetch(
            "ForgetPassword",
            {
                method: "PUT",
                body: FogetPasswordJosn,
                header: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const Json = await response.json();
        if (Json.status) {

            window.location = "ResetPassword.html";
        } else {
            sweetAlert("Error", Json.message, "error");
        }
    } else {
        sweetAlert("Error", "Unable to Genarate OTP.Try Again Later!", "error");
        ;
    }
}

async function ResetPassword() {
    const newPasword = document.getElementById("new-pw").value;
    const ReEnter = document.getElementById("confirm-pw").value;

    const ChangePassword = {
        newPasword: newPasword,
        ReEnter: ReEnter
    };

    const ChangePasswordJson = JSON.stringify(ChangePassword);

    const response = await fetch(
            "ChangePasssword",
            {
                method: "POST",
                body: ChangePasswordJson,
                header: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const Json = await response.json();
        if (Json.status) {
            sweetAlert(Json.message, "success");
            window.location = "SignInNew.html";
        } else {
            sweetAlert("Error", Json.message, "error");
        }
    } else {
        sweetAlert("Error", "Unable to Genarate OTP.Try Again Later!", "error");
        ;
    }
}
