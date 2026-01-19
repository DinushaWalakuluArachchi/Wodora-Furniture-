async function loadData() {

    // console.log("hari");

    const searchParams = new URLSearchParams(window.location.search);
    //  console.log(searchParams);
    if (searchParams.has("id")) {
        const productId = searchParams.get("id");
        console.log(productId);
        const response = await fetch("LoadSingleProduct?id=" + productId);


        if (response.ok) {
            const json = await response.json();
            console.log(json);

            document.getElementById("image1").src = "product-images\\" + json.sellerproduct.id + "\\image1.png";

            document.getElementById("thumb-image1").src = "product-images\\" + json.sellerproduct.id + "\\image1.png";
            document.getElementById("thumb-image2").src = "product-images\\" + json.sellerproduct.id + "\\image2.png";
            document.getElementById("thumb-image3").src = "product-images\\" + json.sellerproduct.id + "\\image3.png";


            document.getElementById("product-title").innerHTML = json.sellerproduct.title;
            document.getElementById("product-price").innerHTML = new Intl.NumberFormat(
                    "en-US",
                    {minimumFractionDigits: 2}).format(json.sellerproduct.price);

            document.getElementById("product-description").innerHTML = json.sellerproduct.description;

            document.getElementById("color-name").innerHTML = json.sellerproduct.color.name;
            document.getElementById("product-material").innerHTML = json.sellerproduct.material.type;

            //addto cart
            const addtoCartMain = document.getElementById("add-to-cart-main");
            addtoCartMain.addEventListener(
                    "click", (e) => {
//                addtoCartMain(json.sellerproduct.id, document.getElementById("add-to-cart-main").value);
//                addToCart(item.id, 1);

                   addtoCart(json.sellerproduct.id,document.getElementById("quantity").value);
                   

                e.preventDefault();
            });
            //addto cart

        } else {
            window.location = "index.html";
        }

    } else {
        window.location = "index.html";
    }

}

async function addtoCart(productId, qty) {
    console.log(productId + " " + qty);

    const response = await fetch("AddToCart?prId=" + productId + "&qty=" + qty);


    if (response.ok) {
        const Json = await response.json();

        if (Json.status) {
            console.log(Json.message);
             sweetAlert("Success", Json.message, "success");
        } else {
                        sweetAlert("Error", "Error", "error");

        }
    } else {

    }

}

