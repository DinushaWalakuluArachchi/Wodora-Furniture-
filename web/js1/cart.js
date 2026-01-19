async function LoadCartItems() {

    const response = await fetch("LoadCartItem");

    if (response.ok) {

        const Json = await response.json();
        if (Json.status) {
            const cart_item_content = document.getElementById("cart-item-container");
            cart_item_content.innerHTML = "";

            let subProductTotal;
            let total = 0;
            let totalQty = 0;
            Json.cartItems.forEach(cart => {
                subProductTotal = cart.product.price * cart.qty;
                total += subProductTotal;
                totalQty += cart.qty;

                let tableData = `

                      <div class="cart-item flex flex-col md:flex-row border-b pb-6 transition-all duration-300" >
                               <div class="md:w-1/4 mb-4 md:mb-0">
                                <img src="product-images\\${cart.product.id}\\image1.png" 
                                     alt="Modern Sofa" 
                                     class="w-full h-40 object-cover rounded-lg">
                            </div>
                            <div class="md:w-3/4 md:pl-6">
                                <div class="flex justify-between">
                                    <div>
                                        <h3 class="text-lg font-semibold text-gray-800">${cart.product.title}</h3>
                                        <p class="text-gray-600">${cart.product.color}</p>
                                        
                                    </div>
                                    <button class="text-gray-400 hover:text-red-500" onclick="deleteProduct(${cart.product.id});">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>
                                
                                <div class="mt-4 flex flex-col sm:flex-row sm:items-center justify-between">
                                    <div class="flex items-center mb-4 sm:mb-0">
                                        <span class="text-lg font-semibold text-woodora">${new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(cart.product.price)}</span>
                                    </div>
                                    
                                    <div class="flex items-center">
                                        <div class="flex items-center border rounded-md">
                                           <h3 min="1" class="w-12 text-center border-0 focus:ring-0">${cart.qty}</h3>
                                        </div>
                                        <span class="ml-6 text-lg font-semibold text-gray-800">Rs.${new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(cart.product.price)}</span>
                                    </div>
                                </div>
                            </div>
                                    </div>`;

                cart_item_content.innerHTML += tableData;

            });


            document.getElementById("order-subtotal-amount").innerHTML = new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(subProductTotal);
            document.getElementById("order-total-amount").innerHTML = new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(total);
            document.getElementById("order-total-qty").innerHTML = totalQty;



        } else {

            sweetAlert("Info", "loaded...", "info");
        }




    } else {


        sweetAlert("Error", "Cart item failed!...", "error");

    }
}

