async function deleteProduct(productId) {

     if (!confirm("Are you sure you want to delete this product?")) return;
     
     

    const response = await fetch("DeleteProduct", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({id: productId})
    });

    const Json = await response.json();
    if (Json.status) {

//        sweetAlert("Success", "Product deleted successfully", "success");
        window.location.reload();
       
    } else {

        sweetAlert("Error", Json.message, "error");
    }

}


