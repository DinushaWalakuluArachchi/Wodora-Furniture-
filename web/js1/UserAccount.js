function loadData() {
    getUserData();
    getCityData();
    productListing();
  //  orderListing();
}

async function getUserData() {
    const response = await fetch("UserAccount");

    if (response.ok) {
        const  json = await response.json();
        document.getElementById("username").innerHTML = `Hello, ${json.fristname} ${json.lastname}`;
        document.getElementById("since").innerHTML = `Woodora Funiture Member Since, ${json.createdAt}`;
        document.getElementById("fname").value = json.fristname;
        document.getElementById("lname").value = json.lastname;
        document.getElementById("currentPassword").value = json.password;

        //  console.log(json);

        if (json.hasOwnProperty("addressList") && json.addressList !== undefined) {

            console.log(json);
            let email;
            let line1;
            let line2;
            let city;
            let pscode;
            let CityId;

            const addressUl = document.getElementById("addressUL");
            json.addressList.forEach(address => {
                email = address.user.email;
                line1 = address.line_01;
                line2 = address.line_02;
                city = address.city.name;
                pscode = address.postalCode;


                CityId = address.city.id;



            });
            console.log(email);
            document.getElementById("addname").innerHTML = `Name : ${json.fristname} ${json.lastname}`;
            document.getElementById("addEmail").innerHTML = `Email: ${email}`;
            document.getElementById("contact").innerHTML = "Phone : 0742986518";


            document.getElementById("line01").value = line1;
            document.getElementById("line02").value = line2;
            document.getElementById("postalCode").value = pscode;
            document.getElementById("citySelect").value = Number(CityId);
            document.getElementById("email").value = email;
        }



    }



}



async function saveChange() {
    const fname = document.getElementById("fname").value;
    const lname = document.getElementById("lname").value;
    const line1 = document.getElementById("line01").value;
    const line2 = document.getElementById("line02").value;
    const postalCode = document.getElementById("postalCode").value;
    const citySelect = document.getElementById("citySelect").value;
    const password = document.getElementById("currentPassword").value;
    const nPassword = document.getElementById("newPassword").value;
    const cPassword = document.getElementById("confirmPassword").value;
    const userDataObject = {
        fristName: fname,
        lastName: lname,
        line1: line1,
        line2: line2,
        posCode: postalCode,
        cityId: citySelect,
        currentPassword: password,
        newPassword: nPassword,
        confirmPassword: cPassword


    };
    const userDataJson = JSON.stringify(userDataObject);
    const response = await fetch(
            "saveChanges",
            {
                method: "PUT",
                body: userDataJson,
                header: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const Json = await response.json();
        if (Json.status) {
            sweetAlert("Success", Json.message, "success");
            getUserData();
            
        } else {
            sweetAlert("Error", Json.message, "error");
        }
    } else {
        sweetAlert("Error", "Profile details Update Fails", "error");

    }


}

async function getCityData() {
    const response = await fetch("CityData");
    if (response.ok) {
        //console.log("hucanw dala");
        const json = await response.json();
        const citySelect = document.getElementById("citySelect");
        json.forEach(city => {
            let option = document.createElement("option");
            option.innerHTML = city.name;
            option.value = city.id;
            citySelect.appendChild(option);
        });
    }
}


async function productListing() {

    const response = await fetch("ProductListing");

    if (response.ok) {
        const Json = await response.json();
        if (Json.status) {

            const productListingLoad = document.getElementById("t-container");
            productListingLoad.innerHTML = "";

            Json.sellerProductList.forEach(product => {

                let tableData = `                             <tr>
                                                                <td>
                                                                    <img src="product-images\\${product.id}\\image1.png" alt="Product" class="img-thumbnail" width="80">
                                                                </td>
                                                                <td>${product.title}</td>
                                                                <td>${product.category.type}</td>
                                                                <td>RS.${new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(product.price)}</td>
                                                                <td>${product.qty}</td>
                                                                
                                                            </tr>`;
                productListingLoad.innerHTML += tableData;

            });

        } else {
             sweetAlert("Error", "error Product Listing !", "error");
        }
    }




}

async function orderListing(){
    
    const response = await fetch("OrderListing");
    
    if (response.ok) {
        const  json = await response.json();
        if (json.status) {
            console.log(json);
            const orderListingLoad = document.getElementById("order-container");
            orderListingLoad.innerHTML = "";
            json.orderItems.forEach(order => {
                
                let tableData = ` <tr>
                                                <td>${order.order.id}</td> 
                                                <td>${order.product.title}</td>
                                                <td>${order.order.createdAt}</td>
                                                <td>${order.qty}</td>
                                                <td>Rs.${order.product.price}.00</td>
                                                <td><span class="badge bg-warning text-dark">${order.orderStatus.value}</span></td>
                                                
                                            </tr>`;
                orderListingLoad.innerHTML += tableData;
          });
        }
        
    }
    
}



    