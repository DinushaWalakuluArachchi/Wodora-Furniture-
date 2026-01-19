
// Payment completed. It can be a successful failure.
payhere.onCompleted = function onCompleted(orderId) {
    //console.log("Payment completed. OrderID:" + orderId);
//    const popup = new Notification();
//    popup.error({
//        message: "Payment completed. OrderID:" + orderId
//    });
    
    console.log("Payment completed. OrderID:" + orderId);

};

// Payment window closed
payhere.onDismissed = function onDismissed() {
    // Note: Prompt user to pay again or show an error page
    console.log("Payment dismissed");
    
};

// Error occurred
payhere.onError = function onError(error) {
    // Note: show an error page
    console.log("Error:" + error);
};






async function loadCheckoutData() {

    //console.log("elakiri");

    const response = await fetch("LoadCheckOutData");

    if (response.ok) {
        const json = await response.json();

        if (json.status) {

            console.log(json);
            const userAddress = json.userAddress;
            const  cityList = json.cityList;
            const cartItems = json.cartList;
            const deliveryTypes = json.deliveryTypes;


            //load city
            let city_select = document.getElementById("city-select");

            cityList.forEach(city => {
                let option = document.createElement("option");
                option.value = city.id;
                option.innerHTML = city.name;
                city_select.appendChild(option);
            });

            const current_address_checkbox = document.getElementById("checkbox1");
            current_address_checkbox.addEventListener("change", function () {


                let first_name = document.getElementById("first-name");
                let last_name = document.getElementById("last-name");
                let line_one = document.getElementById("line-one");
                let line_two = document.getElementById("line-two");
                let postal_code = document.getElementById("postal-code");
                let mobile = document.getElementById("mobile");

                if (current_address_checkbox.checked) {

                    first_name.value = userAddress.user.fname;
                    last_name.value = userAddress.user.lname;
                    city_select.value = userAddress.city.id;
                    city_select.disabled = true;
                    city_select.dispatchEvent(new Event("change"));
                    line_one.value = userAddress.line_01;
                    line_two.value = userAddress.line_02;
                    mobile.value = userAddress.mobile;
                    postal_code.value = userAddress.postalCode;




                } else {
                    first_name.value = "";
                    last_name.value = "";
                    city_select.value = 0;
                    city_select.disabled = false;
                    city_select.dispatchEvent(new Event("change"));
                    line_one.value = "";
                    line_two.value = "";
                    mobile.value = "";
                    postal_code.value = "";


                }
            });


            //loadcartitems


            let st_body = document.getElementById("st-body");
            st_body.innerHTML = "";

            let total = 0;
            let itemCount = 0;

            cartItems.forEach(cart => {

                let content = `<div class="flex items-start">
                            <div class="w-16 h-16 rounded-md bg-gray-100 flex-shrink-0 overflow-hidden">
                                <img src="${"product-images\\" + cart.product.id + "\\image1.png"}" alt="Product" class="w-full h-full object-cover">
                            </div>
                            <div class="ml-4 flex-1">
                                <h3 class="text-sm font-medium text-gray-800">${cart.product.title}</h3>
                               
                                
                            </div>
                            <div class="text-sm font-medium text-gray-800">Rs. ${new Intl.NumberFormat("en-US",
                        {minimumFractionDigits: 2}).format(cart.product.price)}</div>
                        <div class="flex items-center mt-1">
                                  <span class="mx-2 text-sm">${cart.qty}</span>
                        </div>
                        </div>`;
                itemCount += cart.qty;
                let itemSubTotal = Number(cart.qty) * Number(cart.product.price);
                total += itemSubTotal;
                st_body.innerHTML += content;
            });

            document.getElementById("sub-total").innerHTML = `Rs.` + new Intl.NumberFormat("en-US",
                    {minimumFractionDigits: 2}).format(total);

            let shippingCharges = 0;

            city_select.addEventListener("change", (e) => {
                let cityName = city_select.options[city_select.selectedIndex].innerHTML;

                if (cityName === "Kandy") {
                    shippingCharges = deliveryTypes[0].price;

                } else {
                    shippingCharges = deliveryTypes[1].price;

                }


                document.getElementById("shippingChargers").innerHTML = `Rs.` + new Intl.NumberFormat("en-US",
                        {minimumFractionDigits: 2}).format(shippingCharges);

                document.getElementById("total-amount").innerHTML = `Rs.` + new Intl.NumberFormat("en-US",
                        {minimumFractionDigits: 2}).format(shippingCharges + total);

            });





















        } else {
            if (json.message == "empty-cart") {
                             sweetAlert("Info", "Empty cart. please add some product", "info");

                console.log();
                window.location = "index.html";
            } else {
                console.log(json.message);
                sweetAlert("Error", Json.message, "error");

            }
        }

    } else {
        if (response.status == 401) {
            window.location = "SignInNew.html";

        }
    }

}

async function checkout() {
    let checkbox1 = document.getElementById("checkbox1").checked;
    let first_name = document.getElementById("first-name");
    let last_name = document.getElementById("last-name");
    let city = document.getElementById("city-select");
    let line_one = document.getElementById("line-one");
    let line_two = document.getElementById("line-two");
    let postal_code = document.getElementById("postal-code");
    let mobile = document.getElementById("mobile");
    
    
    let data ={
        
         isCurrentAddress: checkbox1,
        fname: first_name.value,
        lname: last_name.value,
        city: city.value,
        line1: line_one.value,
        line2: line_two.value,
        postalCode: postal_code.value,
        mobile: mobile.value
        
    };
    
     let dataJson = JSON.stringify(data);

    const response = await fetch("Checkout", {
        method: "POST",
        header: {
            "Content-Type": "application/json"
        },
        body: dataJson
    });
    
    if (response.ok) {
        const Json = await response.json();
        if (Json.status) {
            console.log(Json);
                         

            //payHereProcess
            payhere.startPayment(Json.payHereJson);
        } else {
          
            sweetAlert("Error", Json.message, "error");
        }
    } else {
        
        console.log();
        sweetAlert("Error", "Somthing went Wrong.Please Try Again Later", "error");
    }
    

}