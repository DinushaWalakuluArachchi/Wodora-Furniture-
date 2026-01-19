async function sendVerificationCode(){
    
//    console.log("ok");


 const verificationCode = document.getElementById("vcode").value;

    const verifyCode = {
        verificationCode: verificationCode
    };

    const  verifyCodeJson = JSON.stringify(verifyCode);
    const response = await fetch(
            "VerifyAccount",
            {
                method: "POST",
                body: verifyCodeJson,
                header: {
                    "Content-Type": "application/json"
                }

            }
    );
    
     if (response.ok) {
        const  Json = await response.json();

        if (Json.status) {
            if (Json.message === "101") {
                window.location = "SignInNew.html";
            } else {
                console.log(Json.message);
               // document.getElementById("message").innerHTML = "Email Not Found";
            }

            window.location = "index.html";
        } else {
           window.location = "SignInNew.html";
        }
    } else {
      //  document.getElementById("message").innerHTML = "Verification Faild.Try Again Later";
      
      sweetAlert("Error", "Verification Faild.Try Again Later", "error");
    }
    
}

