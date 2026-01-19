
var MaterialList;



async function loadProduct() {
    const response = await fetch("LoadProductData");

    if (response.ok) {//success

        const Json = await response.json();
        if (Json.status) {//if true
            console.log(Json);


            loadSelect("category", Json.categoryList, "type");
            loadSelect("material", Json.materialList, "type");
            loadSelect("color", Json.colorList, "name");







        } else { // when status false
            document.getElementById("message").innerHTML = "Unable to get Product data! Please Try Again Later";
        }


    } else {
        document.getElementById("message").innerHTML = "Unable to get Product data! Please Try Again Later";
    }


}

function loadSelect(selectId, list, property) {
    const Select = document.getElementById(selectId);

    list.forEach(item => {
        const Option = document.createElement("option");
        Option.value = item.id;
        Option.innerHTML = item[property];
        Select.appendChild(Option);
    });
}


async function saveproduct() {



    const title = document.getElementById("title").value;
    const description = document.getElementById("Description").value;
    const categoryId = document.getElementById("category").value;
    const materialId = document.getElementById("material").value;
    const color = document.getElementById("color").value;
    const productQty = document.getElementById("productQty").value;
    const productPrice = document.getElementById("productPrice").value;

    const img1 = document.getElementById("image1").files[0];
    const img2 = document.getElementById("image2").files[0];
    const img3 = document.getElementById("image3").files[0];



    const form = new FormData();
    form.append("title", title);
    form.append("description", description);
    form.append("categoryId", categoryId);
    form.append("materialId", materialId);
    form.append("color", color);
    form.append("productQty", productQty);
    form.append("productPrice", productPrice);
    form.append("img1", img1);
    form.append("img2", img2);
    form.append("img3", img3);

    const response = await fetch(
            " SaveProduct",
            {
                method: "POST",
                body: form
            }
    );



    if (response.ok) {
        const json = await response.json();
        if (json.status) { //true - success


            sweetAlert("Success", "Product Added Succefully.", "success");


        } else {// status false - error
            if (json.message === "please Sign in!") {
                window.location = "SignInNew.html"

            } else {

                sweetAlert("Error", json.message, "error");


            }
        }
    } else {

    }


}
