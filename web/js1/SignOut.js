async function SignOut(){
     const response = await fetch("SignOut");
    if(response.ok){
        const json = await response.json();
        if(json.status){
            window.location = "SignInNew.html";
        }else{
            window.location.reload();
        }
    }else{
        sweetAlert("Error", "Logout Faild!.", "error");
       
    }
    
    
}