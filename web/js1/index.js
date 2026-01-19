
function indexOnloadFuction() {
    CheckSessionCart();
    loadProductData();



}



async function CheckSessionCart() {

    const response = await fetch("CheckSessionCart");
    
    if (response.ok) {

    } else {
       
        sweetAlert("Error", "Somthing went wrong!Try again later", "error");
    }



}


async function loadProductData() {
    const response = await fetch("LoadHomeData");
    if (response.ok) {
        const  json = await response.json();
        if (json.status) {
            console.log(json);

            loadProductIndex(json);
         


        } else {
                    sweetAlert("Error", "Somthing went wrong!Try again later", "error");

            console.log("something went wrong! try again shortly");
        }

    } else {
                sweetAlert("Error", "Somthing went wrong!Try again later", "error");

        console.log("something went wrong! try again shortly");
    }

}


function loadProductIndex(json) {

    const index_product_container = document.getElementById("index-product-container");
    index_product_container.innerHTML = "";

    json.sellerProductList.forEach(item => {
        let product_card = ` 
                   <div class="col-12 col-md-4 col-lg-3 mb-5 mb-md-0">
                        <a class="product-item" href="SingleProductView.html?id=${item.id}">
                            <img src="product-images\\${item.id}\\image1.png" class="img-fluid product-thumbnail">
                            <h3 class="product-title">${item.title}</h3>
                            <strong class="product-price">RS.${new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(item.price)}</strong>

                            <span class="icon-cross">
                                <img src="images/cross.svg" class="img-fluid">
                            </span>
                        </a>
                    </div>

 `;

        index_product_container.innerHTML += product_card;
    });


}



