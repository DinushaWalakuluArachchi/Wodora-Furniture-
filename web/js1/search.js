async function loadDatas() {
    console.log("okaaaaaaaaaa");

    const popup = new Notification();
    const response = await fetch("LoadData");

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            loadOptions("category", json.categoryList, "type");
            loadOptions("material", json.materialList, "type");
            loadOptions("color", json.colorList, "name");


            updateProductView(json);



        } else {

            sweetAlert("Error", "Somthing went wrong", "error");
        }
    } else {
        sweetAlert("Error", "Somthing went wrong", "error");

    }
}



function loadOptions(prefix, dataList, property) {
    let options = document.getElementById(prefix + "-filter");
    let li = document.getElementById(prefix + "-li");
    options.innerHTML = "";

    dataList.forEach(item => {
        let li_clone = li.cloneNode(true);
        li_clone.querySelector("#" + prefix + "-a").innerHTML = item[property];

        options.appendChild(li_clone);
    });

    const all_li = document.querySelectorAll("#" + prefix + "-filter li");
    all_li.forEach(list => {
        list.addEventListener("click", function () {
            all_li.forEach(y => {
                y.classList.remove("chosen"); // <li class=".."><a>...</a></l>
            });
            this.classList.add("chosen");// <li class="choosen .."><a>...</a></l>
        });
    });
}

async function searchProduct(firstResult) {
    const popup = new Notification();

    const category_name = document.getElementById("category-filter")
            .querySelector(".chosen")?.querySelector("a").innerHTML;

    const material_name = document.getElementById("material-filter")
            .querySelector(".chosen")?.querySelector("a").innerHTML;

    const color_name = document.getElementById("color-filter")
            .querySelector(".chosen")?.querySelector("a").innerHTML;


    const maxprice = document.getElementById("maxprice").innerHTML;
    const minprice = "0";

    const sort_value = document.getElementById("st-sort").value;


//    console.log(category_name);
//    console.log(material_name);
//    console.log(price_range_start);
//    console.log(color_name);
//    console.log(sort_value);

    const data = {
        firstResult: firstResult,
        categoryName: category_name,
        materialName: material_name,
        colorName: color_name,
        priceStart: minprice,
        priceEnd: maxprice,
        sortValue: sort_value

    };


    const dataJSON = JSON.stringify(data);

    const response = await fetch("SearchProducts",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: dataJSON
            });




    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            console.log(json);
            updateProductView(json);

            sweetAlert("Success", "Product Loading Complete...", "success");

        } else {

            sweetAlert("Error", "Somthing went wrong. Please try again later", "error");

        }
    } else {
        sweetAlert("Error", "Somthing went wrong. Please try again later", "error");


    }
}

const st_product = document.getElementById("st-product"); // product card parent node
let st_pagination_button = document.getElementById("st-pagination-button");
let current_page = 0;

function updateProductView(json) {
    const product_container = document.getElementById("st-product-container");
    product_container.innerHTML = "";
    json.sellerproductList.forEach(product => {
        let st_product_clone = st_product.cloneNode(true);// enable child nodes cloning / allow child nodes
        st_product_clone.querySelector("#st-product-a-1").href = "SingleProductView.html?id=" + product.id;
        st_product_clone.querySelector("#st-product-img-1").src = "product-images//" + product.id + "//image1.png";
        st_product_clone.querySelector("#st-product-add-to-cart").addEventListener(
                "click", (e) => {
            addToCart(product.id, 1);
            e.preventDefault();
        });
        st_product_clone.querySelector("#st-product-title-1").innerHTML = product.title;
        st_product_clone.querySelector("#st-product-price-1").innerHTML = new Intl.NumberFormat(
                "en-US",
                {minimumFractionDigits: 2})
                .format(product.price);
        ;
        //append child
        product_container.appendChild(st_product_clone);
    });

    let st_pagination_container = document.getElementById("st-pagination-container");
    st_pagination_container.innerHTML = "";
    let all_product_count = json.allProductCount;
    document.getElementById("all-item-count").innerHTML = all_product_count;
    let product_per_page = 6;
    let pages = Math.ceil(all_product_count / product_per_page);


    if (current_page !== 0) {
        let st_pagination_button_prev_clone = st_pagination_button.cloneNode(true);
        st_pagination_button_prev_clone.innerHTML = "Prev";
        st_pagination_button_prev_clone.addEventListener(
                "click", (e) => {
            current_page--;
            searchProduct(current_page * product_per_page);
            e.preventDefault();
        });
        st_pagination_container.appendChild(st_pagination_button_prev_clone);
    }

    for (let i = 0; i < pages; i++) {
        let st_pagination_button_clone = st_pagination_button.cloneNode(true);
        st_pagination_button_clone.innerHTML = i + 1;
        st_pagination_button_clone.addEventListener(
                "click", (e) => {
            current_page = i;
            searchProduct(i * product_per_page);
            e.preventDefault();
        });

        if (i === Number(current_page)) {
            st_pagination_button_clone.className = "px-3 py-2 rounded-l-md border border-gray-300 bg-white text-gray-500 hover:bg-gray-50";
        } else {
            st_pagination_button_clone.className = "px-3 py-2 rounded-l-md border border-gray-300 bg-white text-gray-500 hover:bg-gray-50";
        }
        st_pagination_container.appendChild(st_pagination_button_clone);
    }

    if (current_page !== (pages - 1)) {
        let st_pagination_button_next_clone = st_pagination_button.cloneNode(true);
        st_pagination_button_next_clone.innerHTML = "Next";
        st_pagination_button_next_clone.addEventListener(
                "click", (e) => {
            current_page++;
            searchProduct(current_page * product_per_page);
            e.preventDefault();
        });
        st_pagination_container.appendChild(st_pagination_button_next_clone);
    }


}




async function addtoCart(productId, qty) {
    console.log(productId + " " + qty);

    const response = await fetch("AddToCart?prId=" + productId + "&qty=" + qty);


    if (response.ok) {
        const Json = await response.json();

        if (Json.status) {
            console.log(Json.message);
            
            sweetAlert("Error", Json.message, "error");
            //sweetAlert("Success", "Product added Success fully.", "success");
        } else {
            sweetAlert("Error", "error", "error");
            console.log("error");
        }
    } else {
        sweetAlert("Error", "Full error!", "error");

        console.log("full error");

    }

}


async function BasicSearch(){
    
  //  console.log(searchInput.value);
    
    const searchInput = document.getElementById("searchInput").value;
    
    const searchData = {
        query: searchInput
    };
    
    const response = await fetch("BasicSearch" ,{
     method: "POST", 
     headers: {
        "Content-Type": "application/json"
      },
       body: JSON.stringify(searchData)
    });
    
    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            updateProductView(json); 
             
        }
    } else {
        sweetAlert("Error", "Somthing went Wrong", "error");
        
    }
    
}

function reset(){
    window.location.reload();
}